Get started
===========

What is needed
---------------

* Java 8
* dependency to the Smint.io core library added to your project


Todo
----

1. Start-up the sync library

    Utilize class 
    [SmintIoSynchronization](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/SmintIoSynchronization.html)
    and create an instance, providing your (proposed) required implementations:

    ```Java
    final ISmintIoSynchronization smintIoSync = new SmintIoSynchronization(
        new DefaultSyncTargetFactory()
            .setAuthTokenStorage(new MyAuthTokenStorage())
            .setSettingsProvider(() -> settings)
            .setDataFactory(new MySyncTargetDataFactory())
            .setSyncTargetProvider(() -> new MySyncTarget())
    ).start();
    smintIoSync.initialSync(false);
    ```

2. Implement settings provider
  [`ISettingsModel`](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/configuration/models/ISettingsModel.html).
  Settings will be read a lot, so implement some caching technique in case
  you will read the data from the database.

3. Implement storage provider for OAuth access data
  [`IAuthTokenStorage`](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/configuration/IAuthTokenModek.html).

4. implement synchronization target (DAM) data factory
  [`ISyncTargetDataFactory`](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncTargetDataFactory.html).
    ```java
    public class MySyncTargetDataFactory implements ISyncTargetDataFactory {

        @Override
        public ISyncBinaryAsset createSyncBinaryAsset() {
            return new MySyncBinaryAssetImpl();
        }

        @Override
        public ISyncCompoundAsset createSyncCompoundAsset() {
            return new MySyncCompoundAssetImpl();
        }

        @Override
        public ISyncLicenseTerm createSyncLicenseTerm() {
            return new MySyncLicenseTermImpl();
        }

        @Override
        public ISyncReleaseDetails createSyncReleaseDetails() {
            return new MySyncReleaseDetailsImpl();
        }

        @Override
        public ISyncDownloadConstraints createSyncDownloadConstraints() {
            return new MySyncDownloadConstraintsImpl();
        }
    }

    ```

5. (*major*) implement synchronization target (DAM) abstraction [`ISyncTarget`](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncTarget.html).
    ```java
    public class MySyncTarget implements ISyncTarget {

    }

    ```

6. implement the interfaces representing meta data and asset data instances
   see [Packages "target"](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/package-summary.html)

