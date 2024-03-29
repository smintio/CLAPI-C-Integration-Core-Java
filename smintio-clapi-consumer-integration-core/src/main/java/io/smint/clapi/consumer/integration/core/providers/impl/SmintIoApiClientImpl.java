package io.smint.clapi.consumer.integration.core.providers.impl;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

import com.pivovarit.function.ThrowingSupplier;
import com.pivovarit.function.exception.WrappedException;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.vavr.control.Try;

import okhttp3.OkHttpClient;

import io.smint.clapi.consumer.generated.ApiClient;
import io.smint.clapi.consumer.generated.ApiException;
import io.smint.clapi.consumer.generated.api.DownloadsApi;
import io.smint.clapi.consumer.generated.api.MetadataApi;
import io.smint.clapi.consumer.generated.api.TransactionHistoryApi;
import io.smint.clapi.consumer.generated.models.LicenseDownloadConstraints;
import io.smint.clapi.consumer.generated.models.LicenseLanguageEnum;
import io.smint.clapi.consumer.generated.models.LocalizedContentElementDetail;
import io.smint.clapi.consumer.generated.models.LocalizedMetadataElement;
import io.smint.clapi.consumer.generated.models.LocalizedReleaseDetails;
import io.smint.clapi.consumer.generated.models.LocalizedString;
import io.smint.clapi.consumer.generated.models.SyncBinary;
import io.smint.clapi.consumer.generated.models.SyncGenericMetadata;
import io.smint.clapi.consumer.generated.models.SyncLicensePurchaseTransaction;
import io.smint.clapi.consumer.generated.models.SyncLicensePurchaseTransactionQueryResult;
import io.smint.clapi.consumer.generated.models.SyncLicenseTerm;
import io.smint.clapi.consumer.integration.core.LocaleUtility;
import io.smint.clapi.consumer.integration.core.authenticator.IAuthTokenRefreshUtility;
import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoDownloadConstraints;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoGenericMetadata;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoMetadataElement;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoReleaseDetails;
import io.smint.clapi.consumer.integration.core.contracts.impl.SmintIoAssetImpl;
import io.smint.clapi.consumer.integration.core.contracts.impl.SmintIoBinaryImpl;
import io.smint.clapi.consumer.integration.core.contracts.impl.SmintIoDownloadConstraintsImpl;
import io.smint.clapi.consumer.integration.core.contracts.impl.SmintIoGenericMetadataImpl;
import io.smint.clapi.consumer.integration.core.contracts.impl.SmintIoLicenseTermImpl;
import io.smint.clapi.consumer.integration.core.contracts.impl.SmintIoMetadataElementImpl;
import io.smint.clapi.consumer.integration.core.contracts.impl.SmintIoReleaseDetailsImpl;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoSyncJobException;
import io.smint.clapi.consumer.integration.core.providers.ISmintIoApiClient;
import io.smint.clapi.consumer.integration.core.providers.ISmintIoApiDataWithContinuation;


/**
 * Implementing classes utilize the Smint.io CLAPI consumer core library to access Smint.io API and provide sync data.
 *
 * <p>
 * The data to be synchronized by {@link io.smint.clapi.consumer.integration.core.jobs.ISyncJob} need to be fetched from
 * the Smint.io RESTful API. This is done by implementing classes - usually utilizing the available <em>Smint.io Client
 * API Consumer Library</em>.
 * </p>
 */
public class SmintIoApiClientImpl implements ISmintIoApiClient {


    /**
     * The maximum amount of tries to connect to the Smint.io API.
     *
     * <pre>
     *     {@code RETRY_MAX_ATTEMPTS} = {@value #RETRY_MAX_ATTEMPTS}
     * </pre>
     */
    public static final int RETRY_MAX_ATTEMPTS = 5;


    /**
     * The amount of milliseconds to wait before a new try to call the Smint.io API is performed.
     *
     * <p>
     * The waiting duration between next try is doubled on each retry, this is the initial value in milli seconds.
     * </p>
     *
     * <pre>
     *     {@code RETRY_WAIT_FOR_NEXT_RETRY} = {@value #RETRY_WAIT_FOR_NEXT_RETRY}
     * </pre>
     */
    public static final int RETRY_WAIT_FOR_NEXT_RETRY = 2000;


    /**
     * The base URL of the Smint.io API.
     *
     * <p>
     * The placeholder will be replaced with the tenant ID utilizing {@link MessageFormat}
     * </p>
     *
     * <pre>
     * {@code SMINT_IO_API_BASE_URL = }{@value #SMINT_IO_API_BASE_URL}
     * </pre>
     */
    public static final String SMINT_IO_API_BASE_URL = "https://{0}.clapi.smint.io/consumer/v1";


    /**
     * The URL template of an asset URL within the Smint.io API.
     *
     * <p>
     * This template is used to create the Smin.io API URL to the information about an asset. The placeholder will be
     * replaced utilizing {@link MessageFormat} with the following values:
     * </p>
     * <ol start="0">
     * <li>tenant ID (see {@link ISettingsModel#getTenantId()})</li>
     * <li>project UUID (see
     * {@link io.smint.clapi.consumer.generated.models.SyncLicensePurchaseTransaction#getProjectUuid()})</li>
     * <li>content element UUID (see {@link ISmintIoAsset#getContentElementUuid()})</li>
     * </ol>
     *
     * <pre>
     * {@code SMINT_IO_API_BASE_URL = }{@value #SMINT_IO_API_BASE_URL}
     * </pre>
     */
    public static final String SMINT_IO_CONTENT_ELEMENT_URL = "https://{0}.smint.io/project/{1}/content-element/{2}";

    //
    /**
     * The chunks size of the list of assets fetched from the Smint.io platform API.
     *
     * <p>
     * Since the list of assets to sync could be extreme long, it is split into chunks of this maximum fixed size. How
     * many assets are to be synched can not be determined in advance. It depends on activity and delay between
     * synchronization runs.
     * </p>
     *
     * <pre>
     * {@code SMINT_IO_ASSET_LIST_CHUNKSIZE = }{@value #SMINT_IO_ASSET_LIST_CHUNKSIZE}
     * </pre>
     */
    public static final int SMINT_IO_ASSET_LIST_CHUNKSIZE = 10;


    /**
     * Map to translate Smint.io API enumeration key to ISO 639-3 code.
     *
     * @see LicenseLanguageEnum in smintio-clapi-c YAML file
     * @see https://iso639-3.sil.org/code_tables/639/data
     * @see https://en.wikipedia.org/wiki/ISO_639-3
     */
    @SuppressWarnings("serial")
    private static final Map<String, String> MAP_API_LANGUAGE_TO_ISO = new HashMap<String, String>() {
        {
            // Note: null-values will be ignored from mapping.
            // missing keys are just copied as-is.

            // this.put("any", "any"); // no need to add this! missing is copied!
            this.put("language_english", "eng");
            this.put("language_german", "ger");
            this.put("language_french", "fra");
            this.put("language_spanish", "spa");
            this.put("language_mandarin_chinese", "cmn");
            this.put("language_hindustani", "hns");
            this.put("language_arabic", "ara");
            this.put("language_malay", "msa");
            this.put("language_russian", "rus");
            this.put("language_bengali", "ben");
            this.put("language_portuguese", "por");
        }
    };


    private static final Logger LOG = Logger.getLogger(SmintIoApiClientImpl.class.getName());
    private static final RetryRegistry RETRY_REGISTRY = RetryRegistry.of(
        RetryConfig.custom()
            .maxAttempts(RETRY_MAX_ATTEMPTS)
            .intervalFunction(IntervalFunction.ofExponentialBackoff(RETRY_WAIT_FOR_NEXT_RETRY, 2))
            .retryExceptions(Exception.class, ApiException.class)
            .build()
    );


    private final IAuthTokenStorage _authTokenStorage;
    private final Provider<ISettingsModel> _settings;
    private final IAuthTokenRefreshUtility _tokenRefreshUtility;
    private final OkHttpClient _httpClient;
    private MetadataApi _metadataApi;
    private TransactionHistoryApi _transactionApi;
    private DownloadsApi _downloadsApi;

    // CHECKSTYLE OFF: ParameterNumber

    @Inject
    public SmintIoApiClientImpl(
        final Provider<ISettingsModel> settings,
        final IAuthTokenStorage authTokenStorage,
        final IAuthTokenRefreshUtility tokenRefreshUtility,
        final OkHttpClient httpClient,
        @Nullable final MetadataApi smintIoMetadataApi,
        @Nullable final TransactionHistoryApi smintIoTransactionApi,
        @Nullable final DownloadsApi smintIoDownloadsApi
    ) {
        this._settings = settings;
        this._authTokenStorage = authTokenStorage;
        this._tokenRefreshUtility = tokenRefreshUtility;
        this._metadataApi = smintIoMetadataApi;
        this._transactionApi = smintIoTransactionApi;
        this._downloadsApi = smintIoDownloadsApi;
        this._httpClient = httpClient;


        Objects.requireNonNull(settings, "No settings provided to read tenant ID from.");
        Objects.requireNonNull(authTokenStorage, "No auth token storage has been provided to authorize for API.");
        Objects.requireNonNull(tokenRefreshUtility, "Invalid token refresh utility has been provided.");

        if (this._metadataApi == null) {
            this._metadataApi = new MetadataApi();
        }
        if (this._transactionApi == null) {
            this._transactionApi = new TransactionHistoryApi(this._metadataApi.getApiClient());
        }
        if (this._downloadsApi == null) {
            this._downloadsApi = new DownloadsApi(this._metadataApi.getApiClient());
        }
    }

    // CHECKSTYLE ON: ParameterNumber

    @Override
    public ISmintIoGenericMetadata getGenericMetadata() throws ApiException {

        LOG.info("Receiving generic metadata from Smint.io...");
        this.setupClapicOpenApiClient();

        final SyncGenericMetadata syncGenericMetadata = this.retryApiRequest(
            ThrowingSupplier.sneaky(() -> {
                final MetadataApi metadataApi = this.getMetadataApiClient();
                this.setAccessTokenToApi(this.getAuthToken().getAccessToken());
                return metadataApi.getGenericMetadataForSync();
            })
        );


        final ISettingsModel settings = this.getSettings();
        final List<Locale> importLanguages = Arrays.asList(settings.getImportLanguages())
            .stream()
            .map((lang) -> LocaleUtility.covertToISO2Locale(new Locale(lang)))
            .collect(Collectors.toList());

        final SmintIoGenericMetadataImpl smintIoGenericMetadata = new SmintIoGenericMetadataImpl()
            .setContentCategories(
                this.getGroupedMetadataElementsForImportLanguages(
                    importLanguages, syncGenericMetadata.getContentCategories()
                )
            )
            .setContentProviders(
                this.getGroupedMetadataElementsForImportLanguages(importLanguages, syncGenericMetadata.getProviders())
            )
            .setContentTypes(
                this.getGroupedMetadataElementsForImportLanguages(
                    importLanguages, syncGenericMetadata.getContentTypes()
                )
            )
            .setBinaryTypes(
                this.getGroupedMetadataElementsForImportLanguages(
                    importLanguages, syncGenericMetadata.getBinaryTypes()
                )
            )

            .setLicenseTypes(
                this.getGroupedMetadataElementsForImportLanguages(
                    importLanguages, syncGenericMetadata.getLicenseTypes()
                )
            )
            .setReleaseStates(
                this.getGroupedMetadataElementsForImportLanguages(
                    importLanguages, syncGenericMetadata.getReleaseStates()
                )
            )

            .setLicenseExclusivities(
                this.getGroupedMetadataElementsForImportLanguages(
                    importLanguages, syncGenericMetadata.getLicenseExclusivities()
                )
            )
            .setLicenseUsages(
                this.getGroupedMetadataElementsForImportLanguages(
                    importLanguages, syncGenericMetadata.getLicenseUsages()
                )
            )
            .setLicenseSizes(
                this.getGroupedMetadataElementsForImportLanguages(
                    importLanguages, syncGenericMetadata.getLicenseSizes()
                )
            )
            .setLicensePlacements(
                this.getGroupedMetadataElementsForImportLanguages(
                    importLanguages, syncGenericMetadata.getLicensePlacements()
                )
            )
            .setLicenseDistributions(
                this.getGroupedMetadataElementsForImportLanguages(
                    importLanguages, syncGenericMetadata.getLicenseDistributions()
                )
            )
            .setLicenseGeographies(
                this.getGroupedMetadataElementsForImportLanguages(
                    importLanguages, syncGenericMetadata.getLicenseGeographies()
                )
            )
            .setLicenseIndustries(
                this.getGroupedMetadataElementsForImportLanguages(
                    importLanguages, syncGenericMetadata.getLicenseIndustries()
                )
            )
            .setLicenseLanguages(
                Arrays.stream(
                    this.getGroupedMetadataElementsForImportLanguages(
                        importLanguages, syncGenericMetadata.getLicenseLanguages()
                    )
                ).map(
                    (elem) -> new SmintIoMetadataElementImpl()
                        .setValues(elem.getValues())
                        .setKey(this.convertApiLanguage(elem.getKey()))
                ).toArray(ISmintIoMetadataElement[]::new)
            )
            .setLicenseUsageLimits(
                this.getGroupedMetadataElementsForImportLanguages(
                    importLanguages, syncGenericMetadata.getLicenseUsageLimits()
                )
            );

        LOG.info("Received generic metadata from Smint.io");
        return smintIoGenericMetadata;
    }


    @Override
    public ISmintIoApiDataWithContinuation<ISmintIoAsset[]> getAssets(
        final String continuationUuid, final boolean includeCompoundAssets, final boolean includeBinaryUpdates
    ) throws ApiException {

        LOG.info("Receiving assets from Smint.io...");
        final ISmintIoApiDataWithContinuation<ISmintIoAsset[]> result = this
            .loadAssets(continuationUuid, includeCompoundAssets, includeBinaryUpdates);

        LOG.info(
            () -> "Received "
                + (result != null && result.getResult() != null ? result.getResult().length : 0)
                + " assets from Smint.io"
        );

        return result;
    }


    /**
     * Provide the settings as passed to the constructor.
     *
     * @return the settings or {@code null} as it has been provided to the constructor.
     */
    public ISettingsModel getSettings() {
        return this._settings.get();
    }


    /**
     * Provide the authentication token as passed to the constructor.
     *
     * @return the token data or {@code null} as it has been provided to the constructor.
     */
    public IAuthTokenModel getAuthToken() {
        final IAuthTokenStorage storage = this.getAuthTokenStorage();
        return storage != null ? storage.getAuthData() : null;
    }


    /**
     * Provide the authentication token storage as passed to the constructor.
     *
     * @return the token storage or {@code null} as it has been provided to the constructor.
     */
    public IAuthTokenStorage getAuthTokenStorage() {
        return this._authTokenStorage;
    }


    /**
     * Provide the Smint.io platform API client read from {@link #getMetadataApiClient()}.
     *
     * @return the API client or {@code null} if none has been provided with the metadata API client.
     */
    public ApiClient getApiClient() {
        return this.getMetadataApiClient().getApiClient();
    }


    /**
     * Provide the metadata API client as passed to the constructor.
     *
     * @return the API client or {@code null} as it has been provided to the constructor.
     */
    public MetadataApi getMetadataApiClient() {
        return this._metadataApi;
    }


    /**
     * Provide the metadata API client as passed to the constructor.
     *
     * @return the API client or {@code null} as it has been provided to the constructor.
     */
    public TransactionHistoryApi getTransactionApiClient() {
        return this._transactionApi;
    }


    private void setupClapicOpenApiClient() {

        final ISettingsModel settings = this.getSettings();
        final IAuthTokenModel authToken = this.getAuthToken();
        final ApiClient apiClient = this.getApiClient();

        Objects.requireNonNull(settings, "No settings available to read tenant ID from.");
        Objects.requireNonNull(authToken, "No authentication available token to authorize for API.");
        Objects.requireNonNull(apiClient, "Failed to get hold on the Smint.io API client.");

        final String baseURL = MessageFormat.format(SMINT_IO_API_BASE_URL, settings.getTenantId());
        LOG.fine(() -> "Using Smint.io base API URL of " + baseURL);

        apiClient.setBasePath(baseURL);
        this.useSameApiClientWithAllApi(apiClient);
        this.setAccessTokenToApi(authToken.getAccessToken());


        if (this._httpClient != null) {
            apiClient.setHttpClient(this._httpClient);
        }
    }


    private ISmintIoMetadataElement[] getGroupedMetadataElementsForImportLanguages(
        final List<Locale> importLanguages, final List<LocalizedMetadataElement> localizedMetadataElements
    ) {

        if (localizedMetadataElements != null) {

            return localizedMetadataElements.stream()
                .filter(
                    (elem) -> elem != null && elem.getCulture() != null && !elem.getCulture().isEmpty()
                        && elem.getMetadataElement() != null
                )
                .filter(
                    (elem) -> {
                        final Locale language = LocaleUtility.covertToISO2Locale(new Locale(elem.getCulture()));
                        return importLanguages == null || importLanguages.size() == 0
                            || importLanguages.contains(language)
                            || Locale.ENGLISH.equals(language);
                    }
                )
                .collect(Collectors.groupingBy((elem) -> elem.getMetadataElement().getKey()))
                .entrySet()
                .stream()
                .map(
                    (group) -> {

                        Map<Locale, String> availableLocalizedTexts = group.getValue()
                            .stream()
                            .collect(
                                Collectors.toMap(
                                    (elem) -> LocaleUtility.covertToISO2Locale(
                                        new Locale(this.convertApiLanguage(elem.getCulture()))
                                    ),
                                    (elem) -> elem.getMetadataElement().getName()
                                )
                            );

                        availableLocalizedTexts = this.addLanguageFallback(importLanguages, availableLocalizedTexts);
                        return new SmintIoMetadataElementImpl()
                            .setKey(group.getKey())
                            .setValues(availableLocalizedTexts);
                    }
                )
                .toArray(SmintIoMetadataElementImpl[]::new);
        }

        return null;
    }


    private Map<Locale, String[]> getGroupedValuesForImportLanguages(
        final List<Locale> importLanguages, final List<LocalizedMetadataElement> localizedString
    ) {

        if (localizedString != null) {

            final Map<Locale, String[]> availableTexts = localizedString.stream()
                .filter(
                    (elem) -> elem != null && elem.getCulture() != null && !elem.getCulture().isEmpty()
                        && elem.getMetadataElement() != null
                )
                .filter(
                    (elem) -> {
                        final Locale language = LocaleUtility.covertToISO2Locale(new Locale(elem.getCulture()));
                        return importLanguages == null || importLanguages.size() == 0
                            || importLanguages.contains(language)
                            || Locale.ENGLISH.equals(language);
                    }
                )
                .collect(
                    Collectors.groupingBy(
                        (elem) -> LocaleUtility.covertToISO2Locale(new Locale(elem.getCulture()))
                    )
                )
                .entrySet()
                .stream()
                .collect(
                    Collectors.toMap(
                        (entry) -> entry.getKey(),
                        (entry) -> entry.getValue().stream()
                            .map((localizedItem) -> localizedItem.getMetadataElement().getName())
                            .filter((name) -> name != null && !name.isEmpty())
                            .toArray(String[]::new)
                    )
                );


            return this.addLanguageFallback(importLanguages, availableTexts);
        }

        return null;
    }


    private Map<Locale, String[]> getGroupedUrlValuesForImportLanguages(
        final List<Locale> importLanguages, final List<LocalizedMetadataElement> localizedString
    ) {

        if (localizedString != null) {

            final Map<Locale, String[]> availableTexts = localizedString.stream()
                .filter(
                    (elem) -> elem != null && elem.getCulture() != null && !elem.getCulture().isEmpty()
                        && elem.getMetadataElement() != null
                )
                .filter(
                    (elem) -> {
                        final Locale language = LocaleUtility.covertToISO2Locale(new Locale(elem.getCulture()));
                        return importLanguages == null || importLanguages.size() == 0
                            || importLanguages.contains(language)
                            || Locale.ENGLISH.equals(language);
                    }
                )
                .collect(
                    Collectors.groupingBy(
                        (elem) -> LocaleUtility.covertToISO2Locale(new Locale(elem.getCulture()))
                    )
                )
                .entrySet()
                .stream()
                .collect(
                    Collectors.toMap(
                        (entry) -> entry.getKey(),
                        (entry) -> entry.getValue().stream()
                            .map((localizedItem) -> localizedItem.getMetadataElement().getUrl())
                            .filter((url) -> url != null && !url.isEmpty())
                            .toArray(String[]::new)
                    )
                );


            return this.addLanguageFallback(importLanguages, availableTexts);
        }

        return null;
    }


    private Map<Locale, String> getValuesForImportLanguages(
        final List<Locale> importLanguages, final List<LocalizedString> localizedStrings
    ) {

        if (localizedStrings != null) {

            final Map<Locale, String> availableTexts = localizedStrings.stream()
                .filter(
                    (elem) -> elem != null && elem.getCulture() != null && !elem.getCulture().isEmpty()
                        && elem.getValue() != null
                )
                .filter(
                    (elem) -> {
                        final Locale language = LocaleUtility.covertToISO2Locale(new Locale(elem.getCulture()));
                        return importLanguages == null || importLanguages.size() == 0
                            || importLanguages.contains(language)
                            || Locale.ENGLISH.equals(language);
                    }
                )
                .collect(
                    Collectors.toMap(
                        (elem) -> LocaleUtility.covertToISO2Locale(new Locale(elem.getCulture())),
                        (elem) -> elem.getValue()
                    )
                );

            return this.addLanguageFallback(importLanguages, availableTexts);
        }

        return null;
    }


    private <T> Map<Locale, T> addLanguageFallback(
        final List<Locale> importLanguages, final Map<Locale, T> availableValues
    ) {

        if (importLanguages == null || importLanguages.size() == 0) {
            return availableValues;
        }

        if (availableValues == null || availableValues.size() == 0) {
            return null;
        }

        final boolean needEnglishFallback = importLanguages != null && importLanguages.size() > 0
            && !importLanguages.contains(Locale.ENGLISH);


        // copy over all languages to import
        final Map<Locale, T> result = new HashMap<>();
        for (final Locale importLanguage : importLanguages) {

            if (availableValues.containsKey(importLanguage)) {
                result.put(importLanguage, availableValues.get(importLanguage));

            } else if (needEnglishFallback && availableValues.containsKey(Locale.ENGLISH)) {

                // Add English as fallback for all languages not available
                result.put(importLanguage, availableValues.get(Locale.ENGLISH));
            }
        }
        return result;
    }


    private <T> T retryApiRequest(final Supplier<T> func) throws ApiException {

        return Retry.decorateTrySupplier(

            RETRY_REGISTRY.retry("retryApiRequestSupplier"),

            () -> Try.ofSupplier(func)
                .recoverWith(
                    WrappedException.class, (wrappedError) -> {

                        // extract wrapped exception first
                        return Try.failure(wrappedError.getCause());
                    }
                )
                .recoverWith(ApiException.class, (apiError) -> {

                    // try to re-authenticate if necessary
                    LOG.log(Level.SEVERE, "Error communicating to Smint.io", apiError);

                    if (apiError.getCode() == HttpURLConnection.HTTP_FORBIDDEN
                        || apiError.getCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                        try {
                            final IAuthTokenModel authData = this._tokenRefreshUtility
                                .refreshOAuthToken(this.getAuthToken());
                            this.getAuthTokenStorage().storeAuthData(authData);
                            this.setAccessTokenToApi(authData.getAccessToken());
                        } catch (final SmintIoAuthenticatorException authError) {

                            LOG.log(Level.WARNING, "Failed to authenticate with Smint.io platform API.", authError);
                            return Try.failure(authError);
                        }
                    }

                    return Try.failure(apiError);
                })

        ).get().get();
    }


    private ISmintIoApiDataWithContinuation<ISmintIoAsset[]> loadAssets(
        final String continuationUuid, final boolean includeCoundAssets, final boolean includeBinaryUpdates
    ) throws ApiException {

        this.setupClapicOpenApiClient();

        final SyncLicensePurchaseTransactionQueryResult syncLptQueryResult = this.retryApiRequest(
            ThrowingSupplier.sneaky(
                () -> {
                    final TransactionHistoryApi transactionApi = this.getTransactionApiClient();
                    this.setAccessTokenToApi(this.getAuthToken().getAccessToken());
                    return transactionApi
                        .getLicensePurchaseTransactionsForSync(continuationUuid, SMINT_IO_ASSET_LIST_CHUNKSIZE);
                }
            )
        );


        if (syncLptQueryResult == null) {
            return new SmintIoApiDataWithContinuationImpl<ISmintIoAsset[]>()
                .setResult(new ISmintIoAsset[0]);

        } else if (syncLptQueryResult.getCount() == 0) {
            return new SmintIoApiDataWithContinuationImpl<ISmintIoAsset[]>()
                .setContinuationUuid(syncLptQueryResult.getContinuationUuid())
                .setResult(new ISmintIoAsset[0]);
        }


        // convert the assets and filter for assets to be ignored or other invalid assets
        final List<SyncLicensePurchaseTransaction> validAssets = syncLptQueryResult.getLicensePurchaseTransactions()
            .stream()
            .filter((lpt) -> lpt != null && lpt.getContentElement() != null)
            .collect(Collectors.toList());

        // are there any valid assets that are not to be ignored for sync?
        final boolean hasAnyAssets = validAssets.size() > 0;

        // convert to synchronizable assets
        final ISmintIoAsset[] result = validAssets
            .stream()
            .filter((lpt) -> lpt.getCanBeSynced() == null || lpt.getCanBeSynced().booleanValue())
            .map((lpt) -> this.convertApiAsset(lpt, includeCoundAssets, includeBinaryUpdates))
            .filter((asset) -> asset != null)
            .toArray(ISmintIoAsset[]::new);


        return new SmintIoApiDataWithContinuationImpl<ISmintIoAsset[]>()
            .setResult(result)
            .setContinuationUuid(syncLptQueryResult.getContinuationUuid())
            .setHasAssets(hasAnyAssets);
    }


    private ISmintIoLicenseTerm[] getLicenseTerms(
        final List<Locale> importLanguages, final SyncLicensePurchaseTransaction lpt
    ) {

        if (lpt.getLicenseTerms() == null || lpt.getLicenseTerms().size() == 0) {
            return null;
        }


        return lpt.getLicenseTerms().stream()
            .map(
                (licenseTerm) -> new SmintIoLicenseTermImpl()
                    .setSequenceNumber(licenseTerm.getSequenceNumber())
                    .setName(this.getValuesForImportLanguages(importLanguages, licenseTerm.getName()))
                    .setExclusivities(this.convertFromListToStringArray(licenseTerm.getExclusivities()))
                    .setAllowedUsages(this.convertFromListToStringArray(licenseTerm.getAllowedUsages()))
                    .setRestrictedUsages(this.convertFromListToStringArray(licenseTerm.getRestrictedUsages()))
                    .setAllowedSizes(this.convertFromListToStringArray(licenseTerm.getAllowedSizes()))
                    .setRestrictedSizes(this.convertFromListToStringArray(licenseTerm.getRestrictedSizes()))
                    .setAllowedPlacements(this.convertFromListToStringArray(licenseTerm.getAllowedPlacements()))
                    .setRestrictedPlacements(this.convertFromListToStringArray(licenseTerm.getRestrictedPlacements()))
                    .setAllowedDistributions(this.convertFromListToStringArray(licenseTerm.getAllowedDistributions()))
                    .setRestrictedDistributions(
                        this.convertFromListToStringArray(licenseTerm.getRestrictedDistributions())
                    )
                    .setAllowedGeographies(this.convertFromListToStringArray(licenseTerm.getAllowedGeographies()))
                    .setRestrictedGeographies(this.convertFromListToStringArray(licenseTerm.getRestrictedGeographies()))
                    .setAllowedIndustries(this.convertFromListToStringArray(licenseTerm.getAllowedIndustries()))
                    .setRestrictedIndustries(this.convertFromListToStringArray(licenseTerm.getRestrictedIndustries()))
                    .setAllowedLanguages(this.convertApiLanguages(licenseTerm.getAllowedLanguages()))
                    .setRestrictedLanguages(this.convertApiLanguages(licenseTerm.getRestrictedLanguages()))
                    .setUsageLimits(this.convertFromListToStringArray(licenseTerm.getUsageLimits()))

                    .setToBeUsedUntil(licenseTerm.getToBeUsedUntil())
                    .setValidFrom(licenseTerm.getValidFrom())
                    .setValidUntil(licenseTerm.getValidUntil())
                    .setIsEditorialUse(licenseTerm.getIsEditorialUse())

            ).toArray(ISmintIoLicenseTerm[]::new);
    }


    private ISmintIoDownloadConstraints getDownloadConstraints(final SyncLicensePurchaseTransaction lpt) {
        if (lpt == null || lpt.getLicenseDownloadConstraints() == null) {
            return null;
        }

        final LicenseDownloadConstraints restricts = lpt.getLicenseDownloadConstraints();
        return new SmintIoDownloadConstraintsImpl()
            .setMaxUsers(restricts.getEffectiveMaxUsers())
            .setMaxDownloads(restricts.getEffectiveMaxDownloads())
            .setMaxReuses(restricts.getEffectiveMaxReuses());
    }


    private ISmintIoReleaseDetails getReleaseDetails(
        final List<Locale> importLanguages, final SyncLicensePurchaseTransaction lpt
    ) {
        if (lpt == null || lpt.getContentElement() == null || lpt.getContentElement().getReleaseDetails() == null) {
            return null;
        }

        final LocalizedReleaseDetails releaseDetails = lpt.getContentElement().getReleaseDetails();

        return new SmintIoReleaseDetailsImpl()
            .setModelReleaseState(releaseDetails.getModelReleaseState())
            .setPropertyReleaseState(releaseDetails.getPropertyReleaseState())
            .setProviderAllowedUseComment(
                this.getValuesForImportLanguages(
                    importLanguages, releaseDetails.getProviderAllowedUseComment()
                )
            )
            .setProviderReleaseComment(
                this.getValuesForImportLanguages(
                    importLanguages, releaseDetails.getProviderReleaseComment()
                )
            )
            .setProviderUsageConstraints(
                this.getValuesForImportLanguages(
                    importLanguages, releaseDetails.getProviderUsageConstraints()
                )
            );
    }


    private ISmintIoAsset convertApiAsset(
        final SyncLicensePurchaseTransaction apiAsset,
        final boolean includeCoundAssets,
        final boolean includeBinaryUpdates
    ) {

        if (apiAsset == null || apiAsset.getContentElement() == null) {
            return null;
        }


        final ISettingsModel settings = this.getSettings();
        final List<Locale> importLanguages = Arrays.asList(settings.getImportLanguages())
            .stream()
            .map((lang) -> LocaleUtility.covertToISO2Locale(new Locale(lang)))
            .collect(Collectors.toList());


        Boolean isEditorialUse = null;

        List<SyncLicenseTerm> licenseTerms = apiAsset.getLicenseTerms();
        if (licenseTerms == null) {
            licenseTerms = new ArrayList<>();
        }


        for (final SyncLicenseTerm licenceTerm : licenseTerms) {

            // make sure we do not store editorial use information if no information is there!
            if (licenceTerm.getIsEditorialUse() != null) {
                if (licenceTerm.getIsEditorialUse().booleanValue()) {
                    // if we have a restrictions, always indicate

                    isEditorialUse = true;

                } else if (isEditorialUse == null) {
                    // if we have no restriction, only store, if we have no other restriction
                    isEditorialUse = false;
                }
            }
        }

        final LocalizedContentElementDetail contentElement = apiAsset.getContentElement();
        final SmintIoAssetImpl asset = new SmintIoAssetImpl()
            .setContentElementUuid(contentElement.getUuid())
            .setLicensePurchaseTransactionUuid(apiAsset.getUuid())
            .setCartPurchaseTransactionUuid(apiAsset.getCartPurchaseTransactionUuid())
            .setState(apiAsset.getState())
            .setContentProvider(contentElement.getProvider())
            .setContentType(contentElement.getContentType())
            .setName(this.getValuesForImportLanguages(importLanguages, contentElement.getName()))
            .setDescription(this.getValuesForImportLanguages(importLanguages, contentElement.getDescription()))
            .setKeywords(this.getGroupedValuesForImportLanguages(importLanguages, contentElement.getKeywords()))
            .setContentCategory(contentElement.getContentCategory())

            .setReleaseDetails(this.getReleaseDetails(importLanguages, apiAsset))
            .setCopyrightNotices(
                this.getValuesForImportLanguages(importLanguages, contentElement.getCopyrightNotices())
            )

            .setProjectUuid(apiAsset.getProjectUuid())
            .setProjectName(this.getValuesForImportLanguages(importLanguages, apiAsset.getProjectName()))

            .setCollectionUuid(apiAsset.getCollectionUuid())
            .setCollectionName(this.getValuesForImportLanguages(importLanguages, apiAsset.getCollectionName()))

            .setLicenseeUuid(apiAsset.getLicenseeUuid())
            .setLicenseeName(apiAsset.getLicenseeName())
            .setLicenseType(apiAsset.getOffering() != null ? apiAsset.getOffering().getLicenseType() : null)
            .setLicenseText(
                this.getValuesForImportLanguages(
                    importLanguages,
                    apiAsset.getLicenseText() != null
                        ? apiAsset.getLicenseText().getEffectiveText()
                        : null
                )
            )

            .setLicenseUrls(
                this.getGroupedUrlValuesForImportLanguages(importLanguages, apiAsset.getOffering().getLicenseUrls())
            )

            .setLicenseTerms(this.getLicenseTerms(importLanguages, apiAsset))
            .setDownloadConstraints(this.getDownloadConstraints(apiAsset))

            .setIsEditorialUse(isEditorialUse)
            .setHasRestrictiveLicenseTerms(
                apiAsset.getHasPotentiallyRestrictiveLicenseTerms() != null
                    ? apiAsset.getHasPotentiallyRestrictiveLicenseTerms()
                    : false
            )
            .setPurchasedAt(apiAsset.getPurchasedAt())
            .setCreatedAt(apiAsset.getCreatedAt())
            .setLastUpdatedAt(
                apiAsset.getLastUpdatedAt() != null ? apiAsset.getLastUpdatedAt()
                    : apiAsset.getCreatedAt() != null ? apiAsset.getCreatedAt() : OffsetDateTime.now()
            );


        try {
            asset.setSmintIoUrl(
                new URL(
                    MessageFormat.format(
                        SMINT_IO_CONTENT_ELEMENT_URL,
                        settings.getTenantId(),
                        apiAsset.getProjectUuid(),
                        contentElement.getUuid()
                    )
                )
            );
        } catch (final MalformedURLException excp) {
            LOG.log(Level.WARNING, "Invalid Smint.io asset URL!", excp);
        }


        // get the binaries for this asset
        if (apiAsset.getCanBeSynced()) {

            List<SyncBinary> binaries = null;
            try {
                binaries = this.retryApiRequest(
                    ThrowingSupplier.sneaky(() -> {
                        this.setAccessTokenToApi(this.getAuthToken().getAccessToken());
                        return this._downloadsApi.getLicensePurchaseTransactionBinariesForSync(
                            asset.getCartPurchaseTransactionUuid(),
                            asset.getLicensePurchaseTransactionUuid()
                        );
                    })
                );
            } catch (final ApiException excp) {
                LOG.log(
                    Level.SEVERE,
                    "Failed to read binaries of content UUID " + asset.getUuid() + " from Smint.io API: ", excp
                );
            }

            final List<ISmintIoBinary> assetBinaries = new ArrayList<>();
            if (binaries != null) {

                if (!includeCoundAssets && binaries.size() > 1) {
                    throw new SmintIoSyncJobException(
                        SmintIoSyncJobException.SyncJobError.Generic,
                        "SyncTarget does not support compound assets!"
                    );
                }


                for (final SyncBinary binary : binaries) {


                    if (!includeBinaryUpdates && binary.getVersion() != null && binary.getVersion() > 1) {
                        // binary version update
                        throw new SmintIoSyncJobException(
                            SmintIoSyncJobException.SyncJobError.Generic,
                            "SyncTarget does not support binary updates!"
                        );
                    }


                    final SmintIoBinaryImpl convertedBinary = new SmintIoBinaryImpl()
                        .setVersion(binary.getVersion() != null ? binary.getVersion() : 0)
                        .setUuid(binary.getUuid())
                        .setContentType(binary.getContentType())
                        .setBinaryType(binary.getBinaryType())
                        .setName(this.getValuesForImportLanguages(importLanguages, binary.getName()))
                        .setDescription(this.getValuesForImportLanguages(importLanguages, binary.getDescription()))
                        .setUsage(this.getValuesForImportLanguages(importLanguages, binary.getUsage()))
                        .setRecommendedFileName(binary.getRecommendedFileName());

                    if (binary.getCulture() != null && !binary.getCulture().isEmpty()) {
                        convertedBinary.setLocale(new Locale(this.convertApiLanguage(binary.getCulture())));
                    }

                    try {
                        final URL downloadURL = new URL(binary.getDownloadUrl());
                        convertedBinary.setDownloadUrl(downloadURL);

                    } catch (final MalformedURLException excp) {
                        LOG.log(Level.WARNING, "Invalid Smint.io asset binary URL: " + binary.getDownloadUrl(), excp);
                    }

                    assetBinaries.add(convertedBinary);
                }

                if (assetBinaries.size() > 0) {
                    asset.setBinaries(assetBinaries.toArray(new ISmintIoBinary[assetBinaries.size()]));
                }
            }
        }

        return asset;
    }


    private String[] convertFromListToStringArray(final List<String> listOfItems) {
        return listOfItems != null && listOfItems.size() > 0 ? listOfItems.toArray(new String[listOfItems.size()])
            : null;
    }


    /**
     * converts API language enumeration names to ISO 639-3 codes.
     *
     * @param apiLanguages some API enumeration names or {@code null}
     * @return the converted names or {@code null}
     */
    private String[] convertApiLanguages(final List<String> apiLanguages) {
        if (apiLanguages == null || apiLanguages.size() == 0) {
            return null;
        }

        final String[] convertedLanguages = apiLanguages.stream()
            .map((apiLanguage) -> MAP_API_LANGUAGE_TO_ISO.containsKey(apiLanguage) ? MAP_API_LANGUAGE_TO_ISO.get(apiLanguage) : apiLanguage
            )
            .filter((language) -> language != null)
            .toArray(String[]::new);

        return convertedLanguages != null && convertedLanguages.length > 0 ? convertedLanguages : null;
    }

    private String convertApiLanguage(final String apiLanguage) {

        if (apiLanguage == null) {
            return null;
        }

        final List<String> langs = new ArrayList<>();
        langs.add(apiLanguage);

        final String[] converted = this.convertApiLanguages(langs);
        return converted != null && converted.length > 0 ? converted[0] : null;
    }


    private void setAccessTokenToApi(final String token) {
        Objects.requireNonNull(token, "Acess token is invalid (<null>)");

        if (this._metadataApi != null) {
            this._metadataApi.getApiClient().setAccessToken(token);
        }
        if (this._transactionApi != null) {
            this._transactionApi.getApiClient().setAccessToken(token);
        }
        if (this._downloadsApi != null) {
            this._downloadsApi.getApiClient().setAccessToken(token);
        }
    }


    private void useSameApiClientWithAllApi(final ApiClient apiClient) {
        final ApiClient client = apiClient == null ? this.getApiClient() : apiClient;

        if (this._metadataApi != null) {
            this._metadataApi.setApiClient(client);
        }
        if (this._transactionApi != null) {
            this._transactionApi.setApiClient(client);
        }
        if (this._downloadsApi != null) {
            this._downloadsApi.setApiClient(client);
        }
    }
}
