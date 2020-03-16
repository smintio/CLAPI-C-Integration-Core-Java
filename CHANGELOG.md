Changelog
==================================


!WARNING!
---------

Do not use releases prior to 1.9.0 anymore. This release has integrated a newer
Smint.io API SDK 1.4.5, which had introduced breaking changes to URL configuration.
Although no breaking changes of this project (integration library) have been
introduced, predecessor versions are not going to work with the Smint.io API.


release/v1.9.0 (2020-03-16)
---------------------------

### Fix

* (build) upgrade CheckStyle to version 8.29.

### Other

* (BECAUSE OF BREAKING API)  Update Smint.io API SDK to 1.4.5. ([cad0671](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/2749dc65c2895e22289bf5c04b9586f3115f6d17))


---

Previous releases
============================================

*do not use previous releases! They won't work with the Smint.io API!*

---


release/v1.8.3 (2020-01-28)
-------------------------------------------------

### Fix

* (asset) set property release state on asset. ([9a0662c](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/9a0662cae8faaa61d48e1944ec70e875c9182871))
* Require implementation of ISyncJobDataStorage. ([161688e](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/161688e41279819498c2379e54eb760bcb1c9cf6))
* Improve docs for required ISyncJobDataStorage. ([f6be51e](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/f6be51ed14afe163d54b6d43ea44a619a17ec822))

### Other

* Set theme jekyll-theme-cayman. ([2749dc6](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/2749dc65c2895e22289bf5c04b9586f3115f6d17))


release/v1.8.2 (2020-01-17)
----------------------------------

### New

* Provide "run" task for gradle to run example application. ([974b3db](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/974b3db7a90a0b56c563e4c9ead90887887c20c6))

### Changes

* Replace deprecated gradle config. ([b7745dd](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/b7745dd36eb59f8cd18fd5b49f9890d4e4fa2b8e))
* Remove unused code. ([dabd43b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/dabd43b81853243c05b5cd69c3d8de2b5331e526))

### Fix

* Force pusher service to re-connect upon connection exception. ([5dcbc95](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/5dcbc958643612afc0b793c0a4577fb4a8818fdb))
* Keep Pusher re-connecting on network error. ([d71793f](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/d71793f7acd22d599f8f267b65564bddc925d914))


release/v1.8.1 (2020-01-07)
----------------------------------

### New

* (BREAKING) Add license URLs which contain links to further information on licenses (e.g. provider license term pages, links to documents etc.)
  ([00a7df0](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/00a7df01e35ddc53496ad17612a14ae719f484fa))
  ([1780b64](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/1780b6484db98ec7b9b8964a02a18639670f4439))
* Consume rolled-up license texts instead of handling license options separately (no changes needed on consumer side)
  ([00a7df0](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/00a7df01e35ddc53496ad17612a14ae719f484fa))

### Changes

* (BREAKING) Remove license options to reduce complexity in overall metadata structure set-up
  ([00a7df0](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/00a7df01e35ddc53496ad17612a14ae719f484fa))

### Fix

* Add allowed geographies to the asset converter - was missing before
  ([00a7df0](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/00a7df01e35ddc53496ad17612a14ae719f484fa))
* Fix documentation of license term sequence number
  ([00a7df0](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/00a7df01e35ddc53496ad17612a14ae719f484fa))
* Content type now delivers correct value - was always NULL before
  ([0dd244c](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0dd244c5e5147710c42dfd21d36821a1a574d46a))
* Binary type was set to wrong value
  ([311b487](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/311b4874cce115021d87b5e512b1c012403605a8))

### Dependencies

* Update Smint.io CLAPI-C client version to 1.4.5 to handle new API domain structure of Smint.io
* Update Smint.io CLAPI-C client version to 1.4.4 to enable consumption of rolled-up license texts
  ([00a7df0](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/00a7df01e35ddc53496ad17612a14ae719f484fa))


release/v1.8.0 (2020-01-05)
----------------------------------

**skipped due to deployment issues**



release/v1.7.2 (2019-12-30)
----------------------------------

### New

* Make PusherService testable and provide a test case. ([31ca011](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/31ca0114856a0cce1191018aa5abb1856cb23bcd))

### Changes

* Make startNotificationService() async enabling waiting for connection. ([ae9514e](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/ae9514e831a38e29a26e62057da6c0cd69bc4b2b))

### Fix

* Example application runs twice to check updates to assets. ([1780b64](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/1780b6484db98ec7b9b8964a02a18639670f4439))
* Add Pusher logging with channel name when executing jobs. ([f754306](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/f75430654507d10f82a95a8d57ab92c2d4856f15))
* Subscribe to Pusher channel prior to connecting. ([845afe6](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/845afe6be84b2cbafd5b84e8eef8d47c1f45b294))
* Sleep on stop() for 1ms to clean up of events in PusherService. ([1a51f28](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/1a51f2890d5b290d6e1e9762629519ace0f55c88))
* Use PrivateChannelEventListener instead of lambda for channel listener. ([3143efc](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/3143efc5e4a1c9560fca7d03d1f1ebe611dfdd5b))
* Add custom application key to PusherService for testing. ([498c5d4](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/498c5d4009fba9d2a4605c8ec33aab9da8aa50c4))
* Make PusherService support multiple jobs. ([0354548](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0354548ca5574ff7aa86c28d19a944ab89c59cc3))
* Do not validate unused import language setting with PushService. ([6e06399](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/6e0639939fe273c2dd9508c471d26556f69f4da8))
* Use recommendedFileName only for downloaded asset file names. ([3f3a833](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/3f3a8333476c3abc44cb7cb71c8eea3f3fefe22a))


release/v1.7.1 (2019-12-19)
----------------------------------

### Changes

* Rename to "hasRestrictiveLicenseTerms()" <-- from "hasLicenseTerms()" ([9388dbf](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/9388dbfba8736f29aa151ec61f70de095d1ec73c))
* Loop the job execution queue until the queue is empty. ([ec111b0](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/ec111b0bd12352053269b2474ce9af3b3ad5261d))
* Job scheduler use new internal flag for running state. ([ee59deb](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/ee59deb43b10aea7e497014546d1226f01523093))
* Remove unused funcs to wait for queued jobs. ([bc73fd1](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/bc73fd14bdde344d4ef4b8551bb50a5a7c46d65e))

### Fix

* Implement additional checks for the target provided instances in SmintIoSynchronization. ([7f9f91c](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/7f9f91c560e3dd3b7ab61cb0adef8e6f169b8400))
* Testing of SmintIoAuthenticator, which checked for missing value. ([142d488](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/142d48822b3c62df6942e157b9ffff7a51dc2889))
* Implicit conversion from Boolean to boolean failed, because of "null" value. ([9398c52](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/9398c520955597f7a0f131545e4053665bc42cee))
* Set refreshed access token for loading assets. ([2d31a8b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/2d31a8b8e44118eb5f64c66c1d4a5f3cdcef8325))

### Other

* Bump version to 1.7.1. ([0ed3eaa](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0ed3eaa50f2783b239889ac3fd1944e8fc3b6c88))
* * Fix semantic differences to C# in SmintIoAuthenticatorImpl. ([dcf6b09](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/dcf6b09786911d3f95c9c5a806f3dedcd234f3ac))


release/v1.7.0 (2019-12-16)
----------------------------------

### New

* #2 provide default implementations in ISyncTargetCapabilities. ([e1b7ed2](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/e1b7ed25f5e942c135895f4cf3a7ab0cd87982e0))
* Introduce "LocaleUtility" to convert to ISO 639-1 language codes. ([8b52d73](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/8b52d73d38c9332ce956eac11d4a8b9a27fd0967))

### Changes

* (BREAKING) #2 use base class SyncAsset for assets. ([0f194e5](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0f194e50b9312b44fc27969c25fdf6bf509a6a01))
* Reduce visibility of some internal classes to "package" ([f3ca035](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/f3ca03572c45e2f5ecf3067f7a4d1d5ba9aba7c7))
* #2 remove unused function ISyncAsset.setDownloadUrl() ([01c4bac](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/01c4bacaba1177eb721c923ac3f54c03d6ad25df))
* #2 introduce wrapper classes for ISyncAsset, caching some data for sync job. ([5d04265](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/5d0426599af6edd01144f17a1df727c99fd94f58))
* #2 remove unused ISyncCompoundAsset.getAssetParts() ([cf233c9](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/cf233c98538e802ba303db31c64741f1e480c41a))
* #2 remove obsolete function ISyncAsset.getTargetAssetUuid() ([970d761](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/970d7610e130d887f7606fd17be40c00e71dcd8c))
* #2 remove obsolete function ISyncAsset.setLicensePurchaseTransactionUuid() ([7b4bd79](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/7b4bd7944f6c48eaf31fcece3d7c0c430da391f8))
* #2 rename to ISyncAsset.setTransactionUuid() from .setUuid() ([316f93b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/316f93b109447cf56c7570379facb9ab6c2997b1))

### Fix

* Improve JSON serialization of "AssetParts" in BaseSyncAsset. ([d0b294a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/d0b294a14271ded40692f0ff5f9a24e880c845a5))
* Mark "transient" fields in BaseSyncAsset. ([2793a8f](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/2793a8f977ffdaba50f6e5a834660297a97191d3))
* Reset thread pool in native scheduler when stopped. ([fb3d490](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/fb3d49016cbfe16a1b114a736e6d2177a3ed7832))
* Pom.xml use fixed versions for submodule dependencies. ([98692de](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/98692de8c780880610f88fbb82185c00211f7e9a))
* Use English as fallback if requested import language has no meta-data values. ([74b6d7f](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/74b6d7f4bfe8156da79bf038cf195d3ce08744ff))
* Globally convert import languages to ISO 639-3 codes. ([8259503](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/82595037c27cecc3681340a05e88bb887d7359b3))
* Use new version of Smint.io consumer API library. ([72d1054](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/72d1054f0f0f640dc1fccc54da0be17ba072ae75))
* Adapt README to new Azure definition of maven repo access. ([49387dc](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/49387dc7fee3d9e78b291910c799947384e8e707))
* Bump version of resilience4j to 1.2.0. ([a871300](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a871300f2074599456719ba53e74b67db4143dd6))
* #2 remove unused function ISyncAsset.getName() ([b7f6832](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/b7f68329a27f27fc3dff7b7b64f780e0075d57ec))
* #2 correct check for empty localized asset name. ([3aee7aa](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/3aee7aaefb791860c331c30c248152813b188684))

### Other

* Fix version in README.md. ([a1c1291](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a1c1291e5a0e44de5adcdf6be930cff4294f5987))


release/v1.6.0 (2019-12-06)
----------------------------------

### New

* Use internal callback on finished jobs in SmintIoSynchronization. ([9d04e05](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/9d04e05f23d72afb7eb2632d29346a16b4880f61))
* Introduce "notifyWhenFinished" on job execution queue. ([fea85f2](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/fea85f27da5d217ed2e74c0f08441a9b08cba9c0))
* Introduce "scheduleForImmediateExecution" on scheduler. ([26dc57a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/26dc57add35a65b925e335c47398133f5bf359d9))
* Provide default constructor for AuthTokenJsonConverter. ([a10743b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a10743b7d93b4d5bafa43755b486e7c74104dd3e))
* (doc) provide some basic information how to get started. ([32875a0](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/32875a06a7a1f89e04453c280ec1885e6d9d5e6e))
* README.md add description to sub projects and link to github.io documentation. ([b6dbb78](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/b6dbb789937e7795d4f7f2177b57622e79044155))

### Changes

* (BREAKING) remove "initialSync()" add "triggerSync()" on ISmintIoSynchronization. ([6694304](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/6694304cdaf8118b25d3e94e57e487051fd00a55))
* Start first scheduled job immediately upon run() ([df3e48c](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/df3e48c8a67a5a22198b062c6d739a3ebfd246e5))
* Rename DefaultSyncTargetFactory <- from SyncTargetFactoryFromDI. ([482282a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/482282a3e635548e054bd3d97e98097d0a9edcad))
* (BREAKING) remove unused functions in ISyncBinaryAsset. ([5b0d9f0](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/5b0d9f047d43e9532dd0648878c82fd63e0a1870))
* (BREAKING) remove ID mapping from ISyncTarget. ([f49b237](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/f49b23705437fea9f46c1ba04e23e47f179d80e1))
* Introduce ISyncTargetDataFactory. ([3a3e922](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/3a3e922c58a508ec6dba29cd899d2d1b52ca7748))
* (build) move maven publish to central "build.gradle" ([1952041](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/19520413ac9db2956d801ab17d8667774daba6f4))
* (2) rename sub projects to longer name "smintio-clapi-consumer-integration*" ([e79113c](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/e79113c390e44c888207976f78b1be31348085e7))
* Rename sub projects to longer name "smintio-clapi-consumer-integration*" ([6b798e8](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/6b798e8f67577a35b480866b1d3545747f1df8b9))
* (build) remove "-SNAPSHOT" from manually generated jar names. ([feb81ec](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/feb81ecc29716457ef4a002d09ac6fb84dff542a))

### Fix

* Native scheduler test cases + NullPointer exception. ([9ba9610](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/9ba9610279e91f2ffea08deba36de0f4e2b79a69))
* (javadoc) recreate JavaDoc. ([11da9b1](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/11da9b1ba74176c5272dadcf866fc410b0efe16e))
* Catch all RuntimeException when executing job in SmintIoSynchronization. ([e449490](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/e449490671386dc07ea2a66afc9ae1b01134672b))
* Shutdown native JVM scheduler if no more jobs. ([0dc6e64](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0dc6e648dc439023515fa596b5b98b6d688d9fa4))
* Pass exception upwards when downloading binary fails. ([463517e](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/463517eefdb751ee3e6621ec3cbff2af737b14a0))
* (javadoc) remove timestamp from generated HTML files. ([45a49ff](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/45a49ff55889364760b50c4c32884d80759c9813))
* (javadoc) make JavaDoc from gradle and maven look the same. ([289d280](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/289d28003743ba9594721d07775f885904eadf48))
* (eclipse) use new projects names for dependencies. ([e12c99e](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/e12c99eeb9dc42e328485d65f8d659ab8976edaa))
* (doc) regenerate JavaDoc with maven. ([7653d25](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/7653d25977b975985de812aaee4dd2f9a20c86e0))
* (publish) tweaking package description for publishing. ([92614a1](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/92614a1b2595b6f3c7fbc1be811b392a6d68042b))
* (build) use full path for checkstyle configuration. ([296d0ed](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/296d0edc6e77c5c1053e88fe87c4b7aaa6619a08))
* (build) use correct artifact ID for root and sub-project. ([bb1c59d](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/bb1c59d4fb68058f559843cbf627a6fcd13fa683))
* (build) remove deprecated setting for maven assembly plugin. ([5d6e166](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/5d6e166b07093d8bbf6c0d372c1ce4b5c647f2da))
* (build) use proper major version with pom.xml. ([f16e5fc](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/f16e5fcf730229df959a5c130ba71286fccb5935))
* (publish) do not publish non-existent .jar of root project. ([c7ff04d](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/c7ff04df175d09f53a6ad6336ad6811d8c66220b))
* (publish) use correct URL for artifact repository. ([09445dd](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/09445dd76edb7ee7eae64e422188119ae9ac4632))
* Improve java doc overview description. ([f074c9a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/f074c9ac79903615fec03b70cb5bba31be4fd3d3))
* Use correct maven version of resilience4j-retry. ([812b5d6](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/812b5d63b9b8944b6d331d9d9e22248078bfd4f6))

### Other

* * Fix text. ([9ab0c70](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/9ab0c707d954f3b3992bcda9bdd677d59b0388f3))
* * Fix text. ([b799057](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/b79905751983a5452c7188558068cd5b5dcf8a96))
* Set theme jekyll-theme-slate. ([5c1a8e3](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/5c1a8e35c9b7269dca7449d1a057addb4110bd38))
* * Fix copyright notice. ([04713c7](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/04713c7baa31dd69ded49d89997a69619d6362fb))
* * Fix README.md. ([9de1438](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/9de14384abe9594ebf653c0e5d0be923b953e634))


release/v1.5.0 (2019-11-29)
----------------------------------

### New

* Initial set up of CI with Azure Pipelines. ([726e6a4](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/726e6a444fdb5559d60f6fd3b212f1438f9a7f47))
* Adding default appsettings.json. ([b6aa88d](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/b6aa88d81c5c8925acd5ac9bdcee81190767936b))
* Make example app download all assets. ([bb88d59](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/bb88d596b951a805add7b613987d9a43ec85899a))
* Make example app store all data as JSON file. ([225cafa](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/225cafa946f080d0845d5ba2898ebe75dddc3dcd))
* Add "success.html" page for OAuthorizer web service. ([de5dd4d](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/de5dd4da1e88ba8a7e3ea347b6741477643b8598))
* Prepare yet missing download binaries from API. ([17fef66](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/17fef66d500d0d64c5aa0a97499763ce455a881e))
* (doc) add package-info for authenticator module. ([e8a65b5](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/e8a65b5be6a76bb424618bdb09208317ebdea3cf))
* (test) add mire API client tests. ([73b73cc](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/73b73cc5a7294c8a865fe2d4f5cdfb78ffbeb594))
* Implement AuthTokenStorageWithNotify enforcing "notifyAll()" on IAuthTokenStorage. ([b15e0b2](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/b15e0b20fe50c1101d3a9efae35bd9d88f375af6))
* Introduce general OAuth authorizer SmintIoOAuthAuthorizer. ([7ec00e5](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/7ec00e54064f955756dbe9ebfa083c941692eccf))
* Add getter/setter for OkHttpClient in SmintIoAuthenticatorImpl. ([8f249c7](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/8f249c7cef3161c95b9c37c0ccab1b59af0ecb8c))
* Introduce sample application for CLI sync. ([152812e](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/152812e68144817b5386c57ede732b545555c32b))
* Add Gson provider to Guice DI injector. ([54c8a77](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/54c8a7782000d3b49bbf13215e499e250ef99825))
* Introduce asset downloader BinaryAssetDownloader. ([3a6254c](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/3a6254c01fdc85a08c3aa3a19a2a8738f888ad89))
* Basic implement ISmintIoApiClient - need testing. ([9ad798e](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/9ad798e0ed6ce4913c11eff81665243a42cb4890))
* First version of JavaDocs. ([f74667c](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/f74667c972164c9a2cb03e6376da3961d5aceb55))
* Implement simplified CLAPI models. ([e3b7515](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/e3b7515fd1ca71bfa97a1931ff9b97ffe153e6ab))
* Use new Smint.io CLAPI consumer library 1.4.2. ([351c998](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/351c9981b5613d001a73c268cf274bc8eadd01ab))
* Implement OAuth Authenticator for refreshing access token. ([b324df3](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/b324df3a9f41c15e14b6d942f7a020cdb00b467a))
* Add ISmintIoAuthenticator for refreshing OAuth access tokens. ([8ef2a24](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/8ef2a24537d92713969bbf40072d125cbe92e978))
* Introduce IPushNotificationService and implement for Pusher.com. ([beb5bf7](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/beb5bf760d44d60d5af3f745b4ac1802a141c23b))
* Use Guice DI injector - no external DI. ([f127fdb](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/f127fdb71be0d6b23a9d70c78723a7aa6a8d841a))
* Implement SmintIoSynchronization to schedule sync jobs. ([0764db6](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0764db6c53d96a8b8f3db94516046d93040b7a8f))
* Introduce ISmintIoSyncFactory as a wrapper for DI. ([a7f690d](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a7f690def52f6355ff0805da84394c075079ca17))
* Implement EJB scheduler with PlatformSchedulerProvider. ([afb37ff](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/afb37ffdfbcca6dc812f57e18d69454bbe9244c9))
* Add test infratstructure for "smintio-clapi-c-integration-j2e" ([6258978](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/62589788504fb9127ab165b03bc3ad9423d7cebd))
* Introduce IPlatformSchedulerProvider. ([0ffc0a9](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0ffc0a9907acf3bf1610fd30ff826aaf9a67aad2))
* Introduce j2ee companion library. ([1b9f637](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/1b9f6377c469aed1294b02fdba5c1208c546ac85))
* Implement JDK native scheduler. ([58ffa5c](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/58ffa5c77961c5ac3c0594ce35285bd5ed07ee56))
* Implement AuthToken file storage for app. ([0314d5e](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0314d5ed34f6a252bfb9a101c2a0a30b301f90b7))
* Provide JSON converter for IAuthTokenModel. ([6f3d440](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/6f3d440a73a1e5b20eeb21845a7a8297982c2940))
* Introduce new library for Java app based sync "smintio-clapi-c-integration-application" ([7b80557](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/7b805575fcb4e1e22e1e0f326ac83587323f8d0c))
* Add default POJO implementation of IAuthTokenModel. ([92ee934](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/92ee934edae14f9a03221f57f8c1f81b10eda12c))
* Define and implement ISyncJob. ([d1a7a2f](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/d1a7a2f7a052160db5582c4d33d6e8c55c2c6cb6))
* Introduce  ISMintIoGenericMetadata. ([ac3a4ba](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/ac3a4ba1a5839f70a800a44d99882eea8a32a4bc))
* Remove getRedirectUri() from ISettingsModel. ([d975c17](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/d975c17b45a366d4c458bd5df114258e10f52096))
* Add interfaces of port of C# library. ([2d38b3d](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/2d38b3d7fe5a462ee2b01c5bcbe050794621e646))
* Add Eclipse project settings. ([fbc4c8a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/fbc4c8ad473f9dbe800206dbc8ac26f1ba1a77bf))
* Add Eclipse code formatter settings. ([70a824b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/70a824b5abb33de9095913a87141953aaf54c73b))

### Changes

* Make checkstyle optional for the moment. ([bb3091a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/bb3091a71bdd0a52aa90e4ff14ea699042fdfa32))
* Use settings Provider to read current settings all the time. ([8754417](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/8754417cdc9bdd0babaafefdb4a6426fa035d032))
* Make SmintIoAuthenticatorException extend "RuntimeException" converting to unchecked exception. ([fb8845b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/fb8845b11878949dca38f1e7b804ad8aaeff5552))
* Rename to "getTargetAssetBinaryUuid()" <-- from "getTargetBinaryAssetUuid()" ([dde8a1d](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/dde8a1d21530d1cf8a585efbf3ee0bec0e5e34e6))
* Use exponential value retrying accessing Smint.io API. ([1fa8c57](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/1fa8c573a32418e4c3309aeaf6897dba2eb04dbc))
* Make SmintIoSyncJobException extend "RuntimeException" converting to unchecked exception. ([860fe13](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/860fe1390857292d33a9a56cfdef827f92091478))
* Remove HTTP logging interceptor, reducing trace logs. ([0ab6a0d](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0ab6a0df01ec8543fed1e3781b55bdf7c42cdca9))
* Re-use already downloaded binary file on re-run. ([bae8669](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/bae8669a4bb788273b39125d843d68a807ecf135))
* Only use access token with Smint.io when downloading binaries. ([3c331d7](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/3c331d708d87cf6cad7dd63f9c16d0b28a2170f3))
* Remove downloading file from ISmintIoBinary. ([a2a1f1f](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a2a1f1f62052c8536815b07c7ea8fe89966747f6))
* Use null for unset download constraints instead of "-1" ([1c9469f](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/1c9469f8e816a31211b51953bf26d6f8a7e8ef79))
* Enforce conversion of API languages to ISO 639-3 codes. ([06bd231](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/06bd2316c7eb95b90f22820385b3d525a24de0f2))
* Delete unused AuthTokenStorageWithCache. ([7a88f88](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/7a88f88b173474d905b25105f1af0bf54236f21c))
* SminIoApiClient extact asset conversion into separate function. ([a4eac54](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a4eac54f3566f59277666b47923af69fbfe6caca))
* Use form body instead JSON when refreshing access token. ([79489be](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/79489bee11b7393360074a986e292faee72e885d))
* Bind ISettingsModel to instance, forcing it to be singleton. ([53955ba](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/53955ba339185d190fc0621ee358fb63fe562f61))
* Add logging interceptor to OkHttpClient in Guice module. ([c5f6450](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/c5f6450eb942147ab0e5f98d93d1236438429135))
* Implement a cache of 10 seconds into FileModelStorage. ([be492f0](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/be492f0debd835338b27208a14df715311394d46))
* Throw exception if invalid OAuth token data to be stored. ([22da0af](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/22da0afa618f36131463bc92eaec7fdc4ec1d096))
* Simplify SyncGuiceModule, more config less code. ([0c970d7](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0c970d7ec225c666c50d648e5f813d62f1cc5f72))
* Remove wait/notify on IAuthTokenStorage - too complex. ([c8354ff](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/c8354ff549dfc0007de5c400a273acf3f0d22f2e))
* Introduce SystemBrowserAuthenticator start user interaction by local browser. ([df0828b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/df0828b0e344e55a5d57f72abfb38db7e5f9905e))
* Make authorizer call notifyAll() on token storge, too. ([5dcf715](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/5dcf7154436d9b8fed1c27792c38f62590f81837))
* Rename Eclipse projects, shorting the name. ([7382d04](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/7382d04607306f53cd3e4daa3217a2fc988636d0))
* ISettingsModel rename OAuth functions to name "OAuth" add redirect URL. ([813b887](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/813b8879e4cec06a7500692dd4fc64ce06de0625))
* Provide SyncJobDataStorage implementation. ([c8e0e4d](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/c8e0e4d94d05be524b76dee2c6e7d5cf9ff66dbf))
* Pimp ISyncJobExecutionQueue adding base job control functions. ([1f2152a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/1f2152a962e8c4be34189d399ce3be47a469a18a))
* Use Fluent Interface with ISmintIoSynchronization. ([1b4aad2](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/1b4aad252acbe449734d668663ae1d2278e0a4b2))
* Remove "doSync()" from ISmintIoSynchronization. ([e5bbe69](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/e5bbe69bd1b7f17f54c148b52a9151c1c6469cff))
* Introduce ISyncJobExecutionQueue to synchronize job runs avoding collision. ([d4986d6](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/d4986d623bf50f8bd24f5252b345be827b6829fd))
* Update created Javadoc to reflect recent changes. ([3c99225](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/3c99225d245c056793ccb05dcd131fb914489c38))
* ISyncTarget + DefaultSyncJob prepare new ISync* data. ([2663897](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/2663897b23ab46c2bd22d023325758466a465a9d))
* Create converter between ISmintIoAsset -> ISyncAsset (common base ISyncDataType) ([50f7ede](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/50f7ede6e4ccaf91e88b44d9305724ad1c74578f))
* Introduce new sync data ISync* (ISyncAsset) ([ecdcdad](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/ecdcdad30e833a54934c89167d5c7e32d88370ac))
* Rename to "UNKNOWN" from "UNLIMITED" in ISmintIoDownloadConstraints. ([8254d76](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/8254d76e7a897217e35a7995fe8f7d84852ea281))
* Make "isEditorialUse()" return "Boolean" object instead primitive type. ([4524046](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/4524046aa9f7239956de450cb8c328d963f52bd6))
* Add "getUuid()" to  ISmintIoAsset. ([0faa220](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0faa220fbc722c3649157c0dd3f0b49a3d9311a6))
* ISmintIo* use "Locale" in localized texts. ([b3a2b74](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/b3a2b746e984d2d952164c84d89d2f0281888fff))
* ISmintIoAsset rename to "getContentCategory()", "getContentProvider()" ([d925014](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/d92501474b22a5f332a5771f7ae15827cb0ce709))
* Use URL for asset download URL. ([ab775c0](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/ab775c020bf1f9698e0bca2a9f72a6a37a4249ad))
* Adapt code to renaming of ISmintIoGenericMetadata. ([25d6e43](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/25d6e4382d43f011c4fccc19380f71c38eedfeab))
* Rename to ISmintIoGenericMetadata <-- from ISMint... ([1c1a9c4](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/1c1a9c405b2793b25654830b7edb18f335dcc942))
* Adapt because scheduler was moved to "services" ([b74b6ea](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/b74b6ea6b9686e6b62c04d800606e4ca61fc3dae))
* Move scheduler to package "services" ([bc794f8](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/bc794f8b10388f6b8eebb15a9da7e78f48fd0d5b))
* Adapt renaming ISyncTargetFactory. ([85d8e2f](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/85d8e2f302c4b2683fc563e955cf13a36daf2cd9))
* Rename to ISyncTargetFactory <-- from ISyncFactory. ([2535b7d](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/2535b7d2c7e4b6138b68faf8bd7892dde77ff7e2))
* Adapt package of NativeThreadPoolScheduler. ([de7a3be](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/de7a3bebf7466e1be24d7e24bd9259b883b7966e))
* Move NativeThreadPoolScheduler into core library. ([1aa736b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/1aa736bd900d0eac10bc3ce59b6d91d8508b0c71))
* Split NativeThreadPoolScheduler into base class and JDK specific code. ([8c6844f](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/8c6844fb525f3b5c38ab016843ba80f98d871f88))
* Use Locale instead of String for language. ([d376588](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/d376588e607e0a55c7e43d780774063ac1bdea50))
* Provide download File with ISmintIoBinary. ([b73966a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/b73966a7bf72131793d54db278acc9973896c76f))
* Use arrays instead of Lists. ([f6c811b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/f6c811b91afcc054310a4eae22ab7948b0737e52))
* Use sync API and remove use of interface "Future" ([7df52ff](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/7df52ff67c8cab83f6a71e55087ed8c006a803ec))
* Rename files to "I*Storage" <- from "I*Provider" ([d822fb9](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/d822fb96ff46973f6f87b9706ed60f56ae22f4d7))
* Provide storage method with IAuthTokenProvider. ([944c9db](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/944c9dbd8e1f8b4f3f2c2e27f82a31f033d87592))
* After rename to ISyncJobDataProvider change code to reflect. ([3b20acf](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/3b20acfd9995789401d8b691980c8e86d8e70050))
* Rename to ISyncJobDataProvider <- ISyncProcessProvider. ([08e4fba](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/08e4fba9f43a93e3d69540ff932f8151631e6134))

### Fix

* PusherService implement token/settings validation. ([dea331b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/dea331bf430b3c05db42529bc1c80c624854a377))
* Make series of chunk/batch loading of assets abort only at the end. ([088c7c8](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/088c7c8141ede0b3902037110dda7d3710115152))
* Make "createSingleton" really check for existing singleton in SmintIoApiForScribe. ([74ef1b8](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/74ef1b88ddf7e45488295ed0d38bcf91a6bdb942))
* Remove obsolete import in ISmintIoOAuthAuthorizer. ([564de30](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/564de3082f9b2b6a259b2727115ff27dc43ed000))
* Javadoc errors and warnings. ([a0b386d](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a0b386d5eec1bff5b2d0e13c6135e3b153a93415))
* Make use if continuation ID when downloading chunks of asset infos. ([ba79289](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/ba7928978e05f40f94c1d7a6eba32668639e2f7b))
* Make javadoc list index properly start at "0" with ISmintIoAsset. ([479ed96](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/479ed966d34257bb11e4e0945dae84127bea6093))
* On binary download delete the temporary file on JVM exit. ([090a3a2](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/090a3a25f407c4f591616e0c089d67ac4eb2feb3))
* Close HTTP response on binary download avoding memory leaks. ([0450971](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/045097118d92d5974b8544f7c9d9f95541d018ad))
* Increase logging with binary asset downloader. ([a5d07b4](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a5d07b4abb1ae49540939c9165819bba097e40c9))
* Close endless loop with binary downloader and buffer input, too. ([a0fc0e0](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a0fc0e0d3565771af1881b7f6f735e1c0eeb80eb))
* Add missing "maxUsers" to download constraints. ([ed9553b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/ed9553be6295a72c7f1c4b9e5f167db450de9f88))
* Ignore empty localized texts during conversion of API data. ([560145a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/560145a5c86c84aadb251ba3b3b4dd7e7debcf2b))
* Store to correct JSON key in example application + some checkstyle checks. ([f8496bd](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/f8496bd5faf4d5b4a2895b0287b157e033ff398e))
* Add missing collection name, item name for converted API asset. ([a23ce75](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a23ce753d2bce40c4ab3c07a8f7b8c889113373a))
* Converting API asset license term flag used invalid comparison ofr "validFrom" ([366493a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/366493a5c0301a30db07f37b987e650113bb0c59))
* Remove double conversion of license term name after other fixes. ([9aa9b95](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/9aa9b95fe92ecb4308291b1ac8de0d2466420f7b))
* Javadoc ISyncTarget add info about meta data importing. ([9737a29](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/9737a29c9869fcbc0688694e32c2c89709f48cf4))
* Check for null pointer at implicit conversion to primitive type. ([97ee497](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/97ee497543a7c17f6ec1140c713d5f0c1d12de8e))
* Reshape SMINT_IO_CONTENT_ELEMENT_URL to fox invalid place holder. ([90e5f0b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/90e5f0b9fe5deaf5d86cc8f976d46488685fe391))
* Null pointer access while converting binary assets. ([a0c2930](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a0c2930df8b801005040ecb0cc0b139dfb8bfc48))
* Make metadata elements use Loacle and group converted metadata by ID (reverse) ([8b5ea19](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/8b5ea19d201b6b0bc0092e00e0f7a227cfb68703))
* Use globally configures Gson with example application. ([6bf7776](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/6bf7776d385913d3fdcdc2434afc6d0c6ea89d05))
* Use "expires_in" in response from OAuth token endpoint. ([598aa23](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/598aa230faf24a154409b649571d4436fe3c5ff3))
* Proper convert API data to internal data. ([4950388](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/4950388a966a3a9cdc5250331013c1e1152582a2))
* Write pretty print JSON with Gson. ([decdee2](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/decdee27c927939426a6860529b32620d63bcbc1))
* Make Smint.io ApiClient use fresh AccessToken. ([da67fb7](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/da67fb7b8cc4ec35fa13d69e9b39669fba9d3994))
* Detect error in refresh token responses in SmintIoAuthenticatorImpl. ([a0f5a03](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a0f5a036aea10e882915f3c54e7d42cbd0c182ff))
* Make converstion of API metadata contain values. ([fafc93c](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/fafc93c4ef625abf1745fd413a8c614f79cd3f45))
* Make return value of "brefore*()" in ISyncTarget interpreted correctly. ([0f5878e](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0f5878e761eed4344d9b8aa4d9c7a5a7386b0cbc))
* Wrap job's wait/notify with "synchronized" ([e67af33](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/e67af337ab598cabc11736bfe96ed1a74adc827f))
* Remove endless loop when reading JSON file. ([1822f82](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/1822f820e94b77b8c1c8d60227f1e35f5d873e8f))
* Some minimal javadoc issues. ([56adee6](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/56adee67a8c12ec762c0d9fecc84caa4fb41824e))
* Acquire OAuth tokens in example application. ([edb4efb](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/edb4efbc6bdca961e2309d247c08a8389ef64d09))
* Use IAuthTokenStorage instead of directly injected IAuthTokenModel. ([45ffc51](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/45ffc51ea54701fe6695cbcef6f2b32e8d05473b))
* Fix: add @Inject to constructor of SmintIoDownloadProviderImpl. ([90372e3](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/90372e3d271bef33d20c7be2ee772724f7275dd0))
* Provide ISyncTarget via Guice DI injector. ([a56ce70](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a56ce70f37bf22dcdb5500811a0bbd9026f3f865))
* Provide ISettingsModel via Guice DI injector. ([53abc4a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/53abc4a3ab99c48dd16d0473a0f914480505987b))
* Provide ITokenAuthModel via Guice DI injector. ([c095996](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/c0959964661c0e28d940acaf4e7b4ec0b182f0b2))
* Add OkHttpClient to Guice module. ([92e618a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/92e618a06b4177ec1b3996be41285cdcb00414d7))
* Change return type to ISyncCompoundAsset instead of ISyncBinaryAsset. ([5e3cf7d](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/5e3cf7d87aa6c1aeafadaab6741458d337a95f92))
* Convert Smint.io API language names to ISO 639-3 codes. ([641e853](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/641e85310a5e581b152cfb2458d4bb7e19bd4c3a))
* Remove caching auth storage from default usage. ([35fa668](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/35fa66825cdbc2e849ba16e7e350a5cd61a6c075))
* Add "smintio-clapi-c-integration*/target" to .gitignore. ([471ce3a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/471ce3ab7dd1aa7a2642ffb855f915b7efce650b))
* Split code line in SmintIoApiClientImpl to limit line length. ([98ff4e6](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/98ff4e624c0c67d3899aafed5adc0ff5f7e6a22d))
* (doc) provide Javadoc for ISmintio* interfaces in package "contract" ([5fefbd2](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/5fefbd20e01446257d473fbbe8d2348c1ec0a21c))
* (build) use correct maven URL for CLAPI-C library. ([2564df1](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/2564df13bdc2c8387aac2f0d062a5885f2bb12d8))
* (build) define version for "maven-project-info-reports-plugin" ([c8a9524](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/c8a9524c5fe9d63ccb4b1017e57b0863d3171631))
* (build) use mockito up to < 4.0. ([b83ca5c](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/b83ca5c0f5af09366506138f2415e91990ade324))
* (build) add JCenter repository. ([3ef2783](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/3ef27832251bf00bb9a415225d8eb2d80f91f676))
* Make javadoc generation happen again. ([608d5e7](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/608d5e77bd00a964942e12ab7b994239a20977ab))
* (test) use Mockito instead of JMockit. ([e122c27](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/e122c27b91b2b2dadaee7891c01620ae4a964aaf))
* Checkstyle - tighten requirements for Javadoc style. ([a6209d6](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/a6209d685fc0bce95b68531950e3b90419d0ca10))
* Add disables test J2eeScheduler. ([2d8371c](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/2d8371c417ebd7d7ca259762d8078665bbce7351))
* Lint - make linter (checkstyle) happy. ([95ae4ea](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/95ae4ead8ac37c0d2549aa5edc00468a1dee1dae))
* Remove "test/resources" from Eclipse classpath. ([8335299](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/8335299dda2d40b3afa9f197bdfa542dcd23ac6c))
* Unit test setup difference pom.xml/build.gradle. ([f06e93b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/f06e93bf4ee10e7ea13621177cf627438ce1ef2f))
* Add "src/test" directory to Eclipse project of smintio-clapi-c-integration-application. ([b55ab99](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/b55ab9991c5cefd5f3630917308ad554ad5e95e4))
* Javadoc in ISyncJobDataModel. ([54c9045](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/54c90456c8c28c041bca511be72d0e778e94e025))
* Improve some Javadoc documentation. ([c114c7c](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/c114c7cd843f6628e7341fb4c3b7a3bbc94b2668))
* Add missing setSyncProcessModelAsync() to ISyncProcessProvider. ([474be6b](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/474be6bcf20231311437a4584b3b81d0947463f2))
* Improve documentation for settings, token and OAuth. ([0368d12](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0368d12381138d31965b92b241ad274a069b23cf))
* ISmintIoSynchronization rename start/stop. ([dd0c62a](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/dd0c62aa55b269956c21c5c022db2ea37f67363f))

### Other

* Bump version to 1.5.0. ([1e3dcbd](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/1e3dcbd17477f30b72a8cc3e6f7e20f6e7a07726))
* * Remove checks for parameters in download constraints (semantic interpretation might be faulty) ([0c1769e](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/0c1769ec0712211dead778c07cf65f0a9add3b48))
* Revert: on binary download delete the temporary file on JVM exit. ([c966080](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/c9660803de3f53afa797004ac37547a97f7b97dd))
* * Fix typo * Fix comment. ([d6f4ed3](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/d6f4ed3489eebca52cba64717c87b0afab8c9dbc))
* Fixup EJB. ([2653ffb](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/2653ffb4f25d03e3aeec685b01e13803cc13cdcc))
* Add dependency to Smint.io client API consumer library (CLAPI-C) ([7941c88](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/7941c88da39f1dd0bf603a51f64a2906db9dd2b0))
* Initial project setup for Java with test and checkstyle. ([1497456](https://github.com/smintio/CLAPI-C-Integration-Core-Java/commit/1497456a8e0a9a48fb9aefd54cf3b0709ab774ab))


