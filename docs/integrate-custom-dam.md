Integrate Smint.io to your custom DAM
=====================================

Using this library, it is easy to integrate Smint.io ECB into any custom
DAM. Since Smint.io usually manages more detailed data about assets, this
is a one-way synchronization: From Smint.io --> custom DAM.


An example implementation can be viewer on github source code
[Github project source
code](https://github.com/smintio/CLAPI-C-Integration-Core-Java/tree/master/smintio-clapi-consumer-integration-application/src/main/java/io/smint/clapi/consumer/integration/app)


Requirements overview
----------------------

* OAuth 2.0 authentication with Smint.io
* *DAM* support for *virtual/hidden* assets or *Compound Asset*
* Java 8



Library entry point
-------------------

The entry point for the library is the class
[SmintIoSynchronization](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/SmintIoSynchronization.html).
To drive the synchronization, the *DAM* plugin needs to create an instance
of this class and provides the custom implementations of the required
interfaces. There is no need to keep a reference to the class, as it will be
stored with the JVM scheduler. However, for a clean shutdown, it is advised
to do so, anyway.


```Java
final ISmintIoSynchronization smintIoSync = new SmintIoSynchronization(
    new SyncTargetFactoryFromDI(
        new MyTokenStorage,
        () -> settings,
        () -> new MySyncJson()
    )
).start();
smintIoSync.initialSync(false);
```



OAuth 2.0  authentication
----------------------------

* Supporting library: <em>smintio-clapi-consumer-integration-authorizer</em>
  ([JavaDoc](smintio-clapi-consumer-integration-authorizer/1/),
  [example](example-oauth.md))


The synchronization process is targetted at a specific company, operating
the DAM. This company is called *tenant* on the Smint.io platform.
Authorization is required to allow the running application to query Smint.io
platform for all assets available.

Authorization is performed utilizing [OAuth 2.0](https://oauth.net/2/).
It involves user interaction to manually grant the authorization. This can
be performed by an DAM administrator, but must be done manually.



Abstraction of the target DAM system
------------------------------------

* Supporting library: <em>smintio-clapi-consumer-integration-core</em>
  ([JavaDoc](smintio-clapi-consumer-integration-core/1/),
  [example](example-core.md))


A synchronization process communicates with the target DAM by a well defined
set of functions, defined in the interface [ISyncTarget](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncTarget.html)

It is organised in four sections

1. providing some insights about the target DAM
   [ISyncTarget#getCapabilities()](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncTarget.html#getCapabilities--)
2. Importing meta data and retrieving target DAM IDs of Smint.io metadata
   keys.
3. Importing asset data
4. Functions to support error handling and flow control
    * [ISyncTarget#beforeGenericMetadataSync()](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncTarget.html#beforeGenericMetadataSync--)
    * [ISyncTarget#afterGenericMetadataSync()](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncTarget.html#afterGenericMetadataSync--)
    * [ISyncTarget#beforeAssetsSync()](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncTarget.html#beforeAssetsSync--)
    * [ISyncTarget#afterAssetsSync()](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncTarget.html#afterAssetsSync--)

 
This is the main implementation to provide by the DAM integration.




How to provide custom implementations
-------------------------------------

The synchronization feature is started with the class `SmintIoSyncrhonization`.
Its constructor takes an instance of a factory that will provide the custom
implementations for the required interfaces. see [Library entry
point](#library-entry-point) above!


The basic interfaces to implement and provide are

* [`ISyncTarget`](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncTarget.html)
* [`ISettingsModel`](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/configuration/models/ISettingsModel.html)
* [`IAuthTokenStorage`](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/configuration/IAuthTokenStorage.html)


There some more interfaces to implement, that cover meta data and OAuth access data.



Job to synchronize from Smint.io to target custom *DAM*
-------------------------------------------------------

* Supporting library: <em>smintio-clapi-consumer-integration-core</em>
  ([JavaDoc](smintio-clapi-consumer-integration-core/1/),
  [example](example-core.md))


A synchronization process can be run regularily. The library takes care of
that. What consumers need to implement, is the proper translation of all
asset data to DAM data. The integration library performs a lot of
abstraction to make this much easier. The library comes integrated with a
timer to schedule a synchronization job every hour. So synchronization
happens regularily.


But this is not enough. By integrating a push service from
[Pusher.com](https://pusher.com), Smint.io is able to send a push
notification for every purchase. So any newly purchased asset will be
synchronized almost immediately to the target DAM.




Asset data
----------

Smint.io uses a more virtual concept of an *asset*. An asset may contain
multiple binary data, eg: images with different resolutions or videos
with additional companion data, like transscripts, images.
Hence there are *Binary Assets* and *Compound Assets* with Smint.io.
The former is a single binary, the latter consists of multiple binaries.

The asset data provided

### A *virtual/hidden* asset

Most often [Digital Asset management (DAM)](https://en.wikipedia.org/wiki/Digital_asset_management)
are focused on single digital assets. Some do not support something
like a *Compound Asset*. So there is a need to provide some
sort of *virtual/hidden* asset, that hold references to all related binary
assets.



Meta data
---------

Smint.io stores a lot of license information in a structured way. It is
necessary to provide all the information needed to users about licensing
of the asset. Each meta data item consists of a Smint.io ID/key, which need
to be mapped to a target DAM ID. In the most simple way, these meta data do
not have a direct reflaction in the target DAM and are just stored as
localized texts. By referencing these with asset data, the DAM will show
the texts along the assets.

Asset data as provided by this library, will reference the meta data
utilizing the target DAM IDs/keys. Hence a 1:1 mapping, maintained by the
`ISyncTarget` instance is required to find the target DAM ID for a Smint.io
key.



