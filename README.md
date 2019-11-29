Smint.io CLAPI-C Java integration core library
==============================================

[![MIT](https://img.shields.io/github/license/smintio/CLAPI-C-Integration-Core.svg)](https://opensource.org/licenses/MIT)

The Smint.io Content Licensing Consumer Integration Core package provides a
common codebase for integration to
[Digital Asset management (DAM)](https://en.wikipedia.org/wiki/Digital_asset_management),
Web2Print, WCM or other systems, written in Java.


Current version is: 1.5.0


Requirements
------------

### *Smint.io Content Licensing Consumer API library* - add credentials to load the library

This library depends on the *Smint.io Content Licensing Consumer API library ("CLAPI-C")*, that will handle
all connection to the RESTful Smint.io API. Access to the *CLAPI-C* library is
restricted. Get in contact with [Smint.io](https://www.smint.io) and request
access. You will need to sign an NDA first.

You will need an account with Microsoft Visual Studio cloud offerings, as
the CLAPI-C library is hosted there.

#### Required steps:

-   login to your [Microsoft Visual Studio account](https://visualstudio.microsoft.com/)
-   create an [user access token](https://docs.microsoft.com/en-us/azure/devops/organizations/accounts/use-personal-access-tokens-to-authenticate?view=azure-devops)


If you use [gradle](https://gradle.org/) as your build system, you need to
do the following to load the required and restricted Smint.io client API
library.

-   in order to avoid adding the access token to your git repository, use a
    plugin like [`b3er.local.properties` plugin](https://plugins.gradle.org/plugin/com.github.b3er.local.properties)
-   as an example take a copy of the file [`local.properties.template`](./local.properties.template)
    and use it as  `local.properties` and replace the text *FILL_IN_USER_ACCESS_TOKEN*
    with your token.
    Ensure you do not add this file to your code versioning system!
    [git](https://en.wikipedia.org/wiki/Git) provides
    [gitignore](https://git-scm.com/docs/gitignore) file for this.
-   Now you might want to add the following repository to the list of your
    repositories:

    ```
    maven {
        url 'https://smintio.pkgs.visualstudio.com/_packaging/CLAPIC-API-Clients/maven/v1'
        credentials {
            username "AZURE_ARTIFACTS"
            password System.getenv("AZURE_ARTIFACTS_ENV_ACCESS_TOKEN") ?: "${azureArtifactsGradleAccessToken}"
        }
    }
    ```


If you are using maven instead, then the steps are generally the same, but
the files and changes differ:

- Add credentials to your user [`settings.xml`](https://maven.apache.org/settings.html#Servers) inside the
    &lt;servers&gt; element.
    Ensure you do not add this file to your code versioning system!
    [git](https://en.wikipedia.org/wiki/Git) provides
    [gitignore](https://git-scm.com/docs/gitignore) file for this.

    ```
    <server>
      <id>smintio-visualstudio-com-smintio-clapic-api-clients</id>
      <username>CLAPIC-API-Clients</username>
      <!-- Treat this auth token like a password. Do not share it with anyone, including Microsoft support. -->
      <password>FILL_IN_USER_ACCESS_TOKEN</password>
    </server>
    ```

- Add this to your project `pom.xml` inside the `<repositories>` elements.

    ```
    <repository>
      <id>smintio-visualstudio-com-smintio-clapic-api-clients</id>
      <url>https://smintio.pkgs.visualstudio.com/_packaging/CLAPIC-API-Clients/maven/v1</url>
      <releases>
        <enabled>true</enabled>
      </releases>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
    </repository>
    ```



Implemented features
--------------------

- Acquiring access and refresh token from Smint.io
- Synchronization of all required Smint.io generic metadata
- Synchronization of all required content and license metadata
- Support for compound assets (aka „multipart“ assets)
- Handling of updates to license purchase transactions that have already been synchronized before
- Live synchronization whenever an asset is being purchased on Smint.io
- Regular synchronization
- Exponential backoff API consumption pattern



Contribution
------------

### Build Tool

This project makes use of [Maven](https://maven.apache.org) or [gradle](https://gradle.org/).
You may choose, which ever you like best. However, in case you add more dependencies, ensure
both are still working.


### JavaDoc documentation

JavaDoc documentation pages can be created using either of these commands.
Please provide proper documentation for all your added code or add one in case you
find some missing.

```
gradle javadoc
```
```
mvn javadoc:javadoc
```


### Code style

A code style is being enforced via [Checkstyle](https://checkstyle.sourceforge.io/)
utility. The rules are derived from
[Google Java Style](https://checkstyle.sourceforge.io/styleguides/google-java-style-20180523/javaguide.html).

In order to test your code against the style, use following commands:

```
gradle checkstyleMain checkstyleTest
```
```
mvn checkstyle:checkstyle
```

To help you getting your styles right, there is a configuration for the ecplise
code style *Formatter*
(file [`eclipse-smintio-code-formatter.xml`](config/checkstyle/eclipse-smintio-code-formatter.xml)) and for the
code style *Clean-up*
(file
[`eclipse-smintio-codestyle.xml`](config/checkstyle/eclipse-smintio-codestyle.xml)). Both files
are in subdirectory `config/checkstyle`




### Contributors

- Nikolaus Rosenmayr, Smint.io GmbH
- Reinhard Holzner, Smint.io GmbH

© 2019 [Smint.io GmbH](https://www.smint.io)

Licensed under the [MIT License](https://opensource.org/licenses/MIT)
