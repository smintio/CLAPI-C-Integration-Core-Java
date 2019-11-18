// MIT License
//
// Copyright (c) 2019 Smint.io GmbH
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
// documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice (including the next paragraph) shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// SPDX-License-Identifier: MIT

package io.smint.clapi.consumer.integration.core.contracts;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Map;


/**
 * Defines a license term composing restrictions and allowances.
 *
 * <h2>Localized texts</h2>
 * <p>
 * All texts of this license are localized (see {@link #getName()}. However, not all texts are available in all
 * languages. To display such a text need to take this into consideration. In case a text is not available in the target
 * language, a default language should be used and if the text is not available with it, the first language in the list
 * should be used instead.
 * </p>
 *
 * @see io.smint.clapi.consumer.integration.core.target.ISyncLicenseTerm
 */
public interface ISmintIoLicenseTerm {


    /**
     * Provides the sequence number of this license term, which is unique only for the current tenant.
     *
     * <p>
     * License terms use a Smint.io sequence number to be unique for a single tenant. License that become invalid (see
     * {@link #getValidUntil()} are not deleted but kept for archiving reasons. New license terms are created with a
     * next available sequence number. There is no ID for license terms which is unique with Smint.io platform.
     * </p>
     *
     * <p>
     * A sequence number is a positive value. All values below {@code 0} are considered invalid and indicate an unknown
     * value.
     * </p>
     *
     * @return the new sequence number or equal to below {@code 0}.
     */
    int getSequenceNumber();


    /**
     * Get the localized name of this license in multiple languages.
     *
     * @return the name of the license in multiple languages.
     */
    Map<Locale, String> getName();


    /**
     * Provides a list of exclusive rights that are granted and on that the restrictions in this instance applies to.
     *
     * <p>
     * An exclusivity type defines a scope for all restrictions and allowances in this instance.
     * </p>
     *
     * <p>
     * Each value is a simple reading text, valid with the Smint.io platform. Its value is arbitrary and uses mixed
     * cases.
     * </p>
     *
     * <p>
     * Non-exhaustive example:
     * </p>
     * <ul>
     * <li>{@code any}</li>
     * <li>{@code exclusive_usage}</li>
     * <li>{@code exclusive_size}</li>
     * <li>{@code exclusive_placement}</li>
     * <li>{@code exclusive_distribution}</li>
     * <li>{@code exclusive_geography}</li>
     * <li>{@code exclusive_industry}</li>
     * <li>{@code exclusive_language}</li>
     * </ul>
     *
     * <p>
     * Although a known set of values are used, some might be changed and more be added in the near future without any
     * further notice. So implementing classes must support an arbitrary value.
     * </p>
     *
     * @return the enumeration name of exclusivity granted or {@code null}.
     */
    String[] getExclusivities();


    /**
     * Provides a list of allowed usages of the related asset.
     *
     * <p>
     * Each value is a simple reading text, valid with the Smint.io platform. Its value is arbitrary and uses mixed
     * cases.
     * </p>
     *
     * <p>
     * Non-exhaustive example: {@code any}, {@code advertising_any}, {@code advertising_display_any},
     * {@code advertising_display_billboard}, {@code advertising_display_non_pos}, {@code advertising_display_pos},
     * {@code advertising_print_any}, {@code advertising_print_directory}, {@code advertising_print_event_program},
     * {@code advertising_print_freestanding_insert}, {@code advertising_print_magazine_newspaper},
     * {@code advertising_tv_any}, {@code advertising_tv_commercial}, {@code advertising_tv_infomercial},
     * {@code advertising_advertorial_any}, {@code digital_media_any}, {@code digital_media_app},
     * {@code digital_media_corporate_promotional}, {@code digital_media_commercial_blog},
     * {@code digital_media_digital_advertisement}, {@code digital_media_email_marketing},
     * {@code digital_media_oem_product}, {@code digital_media_social_media}, {@code film_video_tv_any},
     * {@code film_video_tv_all_media_promo}, {@code film_video_tv_broadcast_network_online_promo},
     * {@code film_video_tv_educational_documentary_programming}, {@code film_video_tv_film_documentary},
     * {@code film_video_tv_film_non_documentary_insert_flash_graphic_element}, {@code film_video_tv_film_documentary},
     * {@code film_video_tv_film_non_documentary_insert_flash_graphic_element},
     * {@code film_video_tv_film_non_documentary_set_decor}, {@code film_video_tv_film_title_sequence},
     * {@code film_video_tv_music_video}, {@code film_video_tv_news_programming},
     * {@code film_video_tv_non_editorial_program_prop_set_decor}, {@code film_video_tv_non_scripted_programming},
     * {@code film_video_tv_on_air_promo}, {@code film_video_tv_scripted_programming},
     * {@code film_video_tv_title_sequence}, {@code film_video_tv_trailer_theatrical_promo}, {@code internal_use_any},
     * {@code internal_use_brochure_newsletter_collateral}, {@code internal_use_intranet_email_video_presentation},
     * {@code internal_use_nonprofit_museum_display}, {@code internal_use_nonprofit_museum_electronic_display},
     * {@code internal_use_theatrical_performance}, {@code internal_use_wall_decor}, {@code marketing_promotional_any},
     * {@code marketing_promotional_brochure_direct_mail}, {@code marketing_promotional_calendar_greeting_card},
     * {@code marketing_promotional_corporate_report}, {@code marketing_promotional_external_presentation},
     * {@code marketing_promotional_giveaways}, {@code marketing_promotional_newsletter_press_event_programs},
     * {@code marketing_promotional_press_release}, {@code marketing_promotional_sales_cd_dvd_video},
     * {@code marketing_promotional_single_sheet_postcards}, {@code marketing_promotional_travel_brochure},
     * {@code publishing_editorial_any}, {@code publishing_editorial_book},
     * {@code publishing_editorial_custom_contract}, {@code publishing_editorial_magazine},
     * {@code publishing_editorial_newspaper}, {@code publishing_editorial_retail_book},
     * {@code publishing_editorial_text_book}, {@code retail_product_packaging_any},
     * {@code retail_product_packaging_check_credit_atm_cards}, {@code retail_product_packaging_event_ticket},
     * {@code retail_product_packaging_greeting_card}, {@code retail_product_packaging_packaging_covers_tags},
     * {@code retail_product_packaging_phone_store_transit_cards},
     * {@code retail_product_packaging_product_design_software_games},
     * {@code retail_product_packaging_product_design_games_toys}, {@code retail_product_packaging_retail_calendar},
     * {@code retail_product_packaging_retail_miscellaneous_novelty}, {@code retail_product_packaging_retail_poster},
     * {@code retail_product_packaging_retail_stationery_postcards}, {@code retail_product_packaging_trading_cards}
     * </p>
     *
     * <p>
     * Although a known set of values are used, some might be changed and more be added in the near future without any
     * further notice. So consuming classes must support an arbitrary value.
     * </p>
     *
     * @return a list of Smint.io enumeration values or {@code null}.
     */
    String[] getAllowedUsages();


    /**
     * Provides a list of restricted/denied usages of the related asset.
     *
     * <p>
     * Uses the same values as {@link #getAllowedUsages()}.
     * </p>
     *
     * @return a list of Smint.io enumeration values or {@code null}.
     */
    String[] getRestrictedUsages();


    /**
     * Provides a list of allowed sizes for the related asset.
     *
     * <p>
     * Each value is a simple reading text, valid with the Smint.io platform. Its value is arbitrary and uses mixed
     * cases.
     * </p>
     *
     * <p>
     * Non-exhaustive example: {@code any}, {@code ad_any}, {@code ad_eigth}, {@code ad_quarter}, {@code ad_half},
     * {@code ad_full}, {@code banner_any}, {@code banner_side}, {@code page_any}, {@code page_sixteenth},
     * {@code page_eigth}, {@code page_quarter}, {@code page_half}, {@code page_full}, {@code page_double},
     * {@code page_wrap_around}, {@code resolution_any}, {@code resolution_low}, {@code resolution_medium},
     * {@code resolution_high}, {@code size_any}, {@code size_small}, {@code size_medium}, {@code size_large},
     * {@code size_mural}
     * </p>
     *
     * <p>
     * Although a known set of values are used, some might be changed and more be added in the near future without any
     * further notice. So implementing classes must support an arbitrary value.
     * </p>
     *
     * @return a list of Smint.io enumeration values or {@code null}.
     */
    String[] getAllowedSizes();


    /**
     * Provides a list of restricted/denied allowed sizes of the related asset.
     *
     * <p>
     * Uses the same values as {@link #getAllowedSizes()}.
     * </p>
     *
     * @return a list of Smint.io enumeration values or {@code null}.
     */
    String[] getRestrictedSizes();


    /**
     * Provides a list of allowed placements for the related asset.
     *
     * <p>
     * Each value is a simple reading text, valid with the Smint.io platform. Its value is arbitrary and uses mixed
     * cases.
     * </p>
     *
     * <p>
     * Non-exhaustive example: {@code any}, {@code circulation_any}, {@code circulation_once},
     * {@code circulation_twice}, {@code circulation_episode_one}, {@code circulation_season_half},
     * {@code circulation_season_full}, {@code circulation_season_three}, {@code circulation_season_five},
     * {@code circulation_followers_under_one_million}, {@code circulation_followers_above_one_million},
     * {@code circulation_release_first}, {@code circulation_release_secondary}, {@code exhibit_any},
     * {@code exhibit_web}, {@code page_any}, {@code page_home}, {@code page_front}, {@code page_cover},
     * {@code page_secondary}, {@code page_inside}, {@code page_repeated}, {@code page_additional_material},
     * {@code placement_any}, {@code placement_prominent}, {@code placement_prominent_multiple},
     * {@code placement_background}, {@code placement_background_multiple}, {@code type_any}, {@code type_documentary},
     * {@code type_educational}, {@code type_feature_entertainment}
     * </p>
     *
     * <p>
     * Although a known set of values are used, some might be changed and more be added in the near future without any
     * further notice. So implementing classes must support an arbitrary value.
     * </p>
     *
     * @return a list of Smint.io enumeration values or {@code null}.
     */
    String[] getAllowedPlacements();


    /**
     * Provides a list of restricted/denied placement for the related asset.
     *
     * <p>
     * Uses the same values as {@link #getAllowedPlacements()}.
     * </p>
     *
     * @return a list of Smint.io enumeration values or {@code null}.
     */
    String[] getRestrictedPlacements();


    /**
     * Provides a list of allowed kind of distribution for the related asset.
     *
     * <p>
     * Each value is a simple reading text, valid with the Smint.io platform. Its value is arbitrary and uses mixed
     * cases.
     * </p>
     *
     * <p>
     * Non-exhaustive example: {@code any}, {@code channel_any}, {@code channel_app},
     * {@code channel_closed_circuit_programming}, {@code channel_digital}, {@code channel_email},
     * {@code channel_festival}, {@code channel_print}, {@code channel_theatrical}, {@code channel_theatrical_limited},
     * {@code channel_tv}, {@code channel_web}, {@code device_any}, {@code device_mobile}, {@code device_portable},
     * {@code device_tablet}, {@code external}, {@code e_greeting}, {@code location_any}, {@code location_one},
     * {@code location_two}, {@code location_three}, {@code location_ten}, {@code media_any}, {@code media_digital},
     * {@code media_print}, {@code social_media_any}, {@code social_media_one}, {@code social_media_two},
     * {@code third_party_rights}
     * </p>
     *
     * <p>
     * Although a known set of values are used, some might be changed and more be added in the near future without any
     * further notice. So implementing classes must support an arbitrary value.
     * </p>
     *
     * @return a list of Smint.io enumeration values or {@code null}.
     */
    String[] getAllowedDistributions();


    /**
     * Provides a list of restricted/denied kind of distribution for the related asset.
     *
     * <p>
     * Uses the same values as {@link #getAllowedDistributions()}.
     * </p>
     *
     * @return a list of Smint.io enumeration values or {@code null}.
     */
    String[] getRestrictedDistributions();


    /**
     * Provides a list of allowed geographic regions to use the related asset within.
     *
     * <p>
     * Each value is a simple reading text, valid with the Smint.io platform. Its value is arbitrary and uses mixed
     * cases.
     * </p>
     *
     * <p>
     * Non-exhaustive example: {@code any}, {@code africa_any}, {@code africa_algeria}, {@code africa_angola},
     * {@code africa_egypt}, {@code africa_ethiopia}, {@code africa_kenya}, {@code africa_morocco},
     * {@code africa_nigeria}, {@code africa_south_africa}, {@code africa_sudan}, {@code africa_tanzania},
     * {@code africa_tunisia}, {@code antarctica_any}, {@code asia_any}, {@code asia_china}, {@code asia_india},
     * {@code asia_indonesia}, {@code asia_japan}, {@code asia_russia}, {@code asia_saudi_arabia},
     * {@code asia_south_korea}, {@code asia_thailand}, {@code asia_turkey}, {@code asia_iran},
     * {@code asia_united_arab_emirates}, {@code australia_any}, {@code australia_australia},
     * {@code australia_new_zealand}, {@code europe_any}, {@code europe_andorra}, {@code europe_austria},
     * {@code europe_belgium}, {@code europe_denmark}, {@code europe_estonia}, {@code europe_finland},
     * {@code europe_france}, {@code europe_germany}, {@code europe_ireland}, {@code europe_italy},
     * {@code europe_latvia}, {@code europe_liechtenstein}, {@code europe_lithuania}, {@code europe_luxembourg},
     * {@code europe_netherlands}, {@code europe_norway}, {@code europe_poland}, {@code europe_portugal},
     * {@code europe_russia}, {@code europe_spain}, {@code europe_switzerland}, {@code europe_sweden},
     * {@code europe_turkey}, {@code europe_united_kingdom}, {@code north_america_any}, {@code north_america_canada},
     * {@code north_america_mexico}, {@code north_america_united_states}, {@code south_america_any},
     * {@code south_america_argentina}, {@code south_america_brazil}, {@code south_america_chile},
     * {@code south_america_colombia}, {@code south_america_peru}, {@code south_america_venezuela}
     * </p>
     *
     * <p>
     * Although a known set of values are used, some might be changed and more be added in the near future without any
     * further notice. So implementing classes must support an arbitrary value.
     * </p>
     *
     * @return a list of Smint.io enumeration values or {@code null}.
     */
    String[] getAllowedGeographies();


    /**
     * Provides a list of restricted/denied geographic regions to use the related asset.
     *
     * <p>
     * Uses the same values as {@link #getAllowedGeographies()}.
     * </p>
     *
     * @return a list of Smint.io enumeration values or {@code null}.
     */
    String[] getRestrictedGeographies();


    /**
     * Provides a list of allowed industries to use the related asset within.
     *
     * <p>
     * Each value is a simple reading text, valid with the Smint.io platform. Its value is arbitrary and uses mixed
     * cases.
     * </p>
     *
     * <p>
     * Non-exhaustive example: {@code any}, {@code industry_automotive}, {@code industry_alcohol},
     * {@code industry_banking}, {@code industry_beauty}, {@code industry_beverages}, {@code industry_cannabis},
     * {@code industry_consumer}, {@code industry_drugs}, {@code industry_ecommerce}, {@code industry_education},
     * {@code industry_electronics}, {@code industry_engineering}, {@code industry_energy}, {@code industry_explosives},
     * {@code industry_financial}, {@code industry_fintech}, {@code industry_fmcg}, {@code industry_food},
     * {@code industry_gaming}, {@code industry_gambling}, {@code industry_gas}, {@code industry_governmental},
     * {@code industry_healthcare}, {@code industry_infrastructure}, {@code industry_insurance},
     * {@code industry_jewelry}, {@code industry_learning}, {@code industry_legal}, {@code industry_lottery},
     * {@code industry_manifacturing}, {@code industry_media}, {@code industry_non_profit}, {@code industry_nuclear},
     * {@code industry_oil}, {@code industry_online}, {@code industry_pornography}, {@code industry_raw_materials},
     * {@code industry_real_estate}, {@code industry_religion}, {@code industry_retail}, {@code industry_sex},
     * {@code industry_software}, {@code industry_technology}, {@code industry_telecommunications},
     * {@code industry_tobacco}, {@code industry_tourism}, {@code industry_transportation}, {@code industry_travel},
     * {@code industry_weapons}
     * </p>
     *
     * <p>
     * Although a known set of values are used, some might be changed and more be added in the near future without any
     * further notice. So implementing classes must support an arbitrary value.
     * </p>
     *
     * @return a list of Smint.io enumeration values or {@code null}.
     */
    String[] getAllowedIndustries();


    /**
     * Provides a list of restricted/denied industries to use the related asset within.
     *
     * <p>
     * Uses the same values as {@link #getAllowedGeographies()}.
     * </p>
     *
     * @return a list of Smint.io enumeration values or {@code null}.
     */
    String[] getRestrictedIndustries();


    /**
     * Provides a list of allowed languages to use the related asset with.
     *
     * <p>
     * Each value is an <a href="https://en.wikipedia.org/wiki/ISO_639-3">ISO 639-3</a> language code.
     * {@code java.util.Locale} is not used, as it contains a geographical component, too.
     * </p>
     *
     * @return a list of ISO 639-3 values or {@code null}.
     */
    String[] getAllowedLanguages();


    /**
     * Provides a list of restricted/denied languages to use the related asset with.
     *
     * <p>
     * Each value is an <a href="https://en.wikipedia.org/wiki/ISO_639-3">ISO 639-3</a> language code.
     * {@code java.util.Locale} is not used, as it contains a geographical component, too.
     * </p>
     *
     * @return a list of ISO 639-3 values or {@code null}.
     */
    String[] getRestrictedLanguages();


    /**
     * Provides a list of usage limits to apply to the usage of the related asset.
     *
     * <p>
     * Each value is a simple reading text, valid with the Smint.io platform. Its value is arbitrary and uses mixed
     * cases. Each value denotes a certain limit upon the usage of the asset.
     * </p>
     *
     * <p>
     * Non-exhaustive example: {@code any}, {@code quantity_any}, {@code quantity_one_copy},
     * {@code quantity_up_to_10_total_circulation}, {@code quantity_up_to_25_total_circulation},
     * {@code quantity_up_to_50_total_circulation}, {@code quantity_up_to_100_total_circulation},
     * {@code quantity_up_to_250_total_circulation}, {@code quantity_up_to_500_total_circulation},
     * {@code quantity_up_to_1000_total_circulation}, {@code quantity_up_to_2500_total_circulation},
     * {@code quantity_up_to_5000_total_circulation}, {@code quantity_up_to_10000_total_circulation},
     * {@code quantity_up_to_25000_total_circulation}, {@code quantity_up_to_50000_total_circulation},
     * {@code quantity_up_to_100000_total_circulation}, {@code quantity_up_to_250000_total_circulation},
     * {@code quantity_up_to_500000_total_circulation}, {@code quantity_up_to_1000000_total_circulation},
     * {@code quantity_up_to_2000000_total_circulation}, {@code quantity_up_to_3000000_total_circulation},
     * {@code display_any}, {@code display_up_to_5_displays}, {@code display_up_to_10_displays},
     * {@code display_up_to_25_displays}, {@code display_up_to_50_displays}, {@code display_up_to_100_displays},
     * {@code display_up_to_250_displays}, {@code display_up_to_500_displays}, {@code copy_any}, {@code copy_one_copy},
     * {@code copy_two_copies}, {@code copy_three_copies}, {@code copy_four_copies}, {@code copy_five_copies},
     * {@code copy_up_to_10_copies}, {@code copy_up_to_50_copies}, {@code copy_up_to_100_copies},
     * {@code copy_up_to_500_copies}, {@code copy_up_to_1000_copies}, {@code copy_up_to_5000_copies},
     * {@code copy_up_to_10000_copies}
     * </p>
     *
     * <p>
     * Although a known set of values are used, some might be changed and more be added in the near future without any
     * further notice. So implementing classes must support an arbitrary value.
     * </p>
     *
     * @return a list of Smint.io enumeration values or {@code null}.
     */
    String[] getUsageLimits();


    /**
     * Provides the date when making use of the related asset can be started.
     *
     * @return the date this asset is allowed to be used after or {@code null} if no restriction apply.
     */
    OffsetDateTime getValidFrom();


    /**
     * Provides the date until making use of the related asset must be stopped.
     *
     * @return the date before this asset is allowed to be used but not after. {@code null} if no restriction apply.
     */
    OffsetDateTime getValidUntil();


    /**
     * Provides the date until making use of the related asset for the first time must be started.
     *
     * <p>
     * Some content providers prohibit <em>sharding</em>, meaning buying a lot of exclusive content but not making use
     * of it. This behavior would block others from exclusive content without effectively making use of the content
     * itself. Blocking other is the main achievement. So a licensor might require the licensee to make use of the asset
     * before a specific date.
     * </p>
     *
     * @return the date before this asset must be used for the first time. {@code null} if no restriction apply.
     */
    OffsetDateTime getToBeUsedUntil();


    /**
     * Provides a flag that this term restricts the use of the related asset to editorial usage.
     *
     * <p>
     * Until a value has been passed to this function, it can not be determined whether the asset is for editorial use
     * only or not. There might be some content provider that do not provide this information on some assets. It simply
     * is impossible to tell. The default assumption and its derived legal action depends on the synchronization target.
     * Smint.io can not give any advise how to handle such content.
     * </p>
     *
     * @return {@link Boolean#TRUE} in case the term restricts for <em>Editorial Use</em> only and {@link Boolean#FALSE}
     *         otherwise. If this can not be determined, than {@code null} is being returned.
     */
    Boolean isEditorialUse();
}

