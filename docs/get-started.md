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
    [SmintIoSynchronization](smintio-clapi-c-integration-core/1/io/smint/clapi/consumer/integration/core/SmintIoSynchronization.html)
    and create an instance, providing your (proposed) required implementations:

    ```Java
    final ISmintIoSynchronization smintIoSync = new SmintIoSynchronization(
        new SyncTargetFactoryFromDI(
            new MyTokenStorage,
            () -> settings,
            () -> new MySyncTarget()
        )
    ).start();
    smintIoSync.initialSync(false);
    ```

2. Implement settings provider
  [`ISettingsModel`](smintio-clapi-c-integration-core/1/io/smint/clapi/consumer/integration/core/configuration/models/ISettingsModel.html).
  Settings will be read a lot, so implement some caching technique in case
  you will read the data from the database.

3. Implement storage provider for OAuth access data
  [`IAuthTokenStorage`](smintio-clapi-c-integration-core/1/io/smint/clapi/consumer/integration/core/configuration/IAuthTokenModek.html).

4. (*major*) implement synchronization target (DAM) abstraction [`ISyncTarget`](smintio-clapi-c-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncTarget.html).
    ```java
    public class MySyncTarget implements ISyncTarget {

    }

    ```

5. implement the interfaces representing meta data and asset data instances
   see [Packages "target"](smintio-clapi-c-integration-core/1/io/smint/clapi/consumer/integration/core/target/package-summary.html)

