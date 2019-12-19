Smint.io CLAPI-C Java integration core library
==============================================

[![MIT](https://img.shields.io/github/license/smintio/CLAPI-C-Integration-Core.svg)](https://opensource.org/licenses/MIT)

The Smint.io Content Licensing Consumer Java Integration Core package provides a
common codebase for integration to
[Digital Asset management (DAM)](https://en.wikipedia.org/wiki/Digital_asset_management),
Web2Print, WCM or other systems, written in Java.


Current version is: 1.7.1

see documentation: https://smintio.github.io/CLAPI-C-Integration-Core-Java/


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




Adding as dependency to your project
------------------------------------

The artifacts are available from Microsoft's Azure service. The following
libraries are available. Basically, what you will need is
<em>smintio-clapi-consumer-integration-core</em>. If some support for getting
OAuth authorization with Smint.io right, you may additional use
<em>smintio-clapi-consumer-integration-authorizer</em>.

* core library: <em>smintio-clapi-consumer-integration-core</em>
  ([JavaDoc](https://smintio.github.io/CLAPI-C-Integration-Core-Java/smintio-clapi-consumer-integration-core/1/))

  It provides all tasks to perform the synchronization with Smint.io. But
  you need to implement OAuth authorization with Smint.io API.

* OAuth helper: <em>smintio-clapi-consumer-integration-authorizer</em>
  ([JavaDoc](https://smintio.github.io/CLAPI-C-Integration-Core-Java/smintio-clapi-consumer-integration-authorizer/1/))

  This will help, to implement OAuth authorization with Smint.io API.


* Application companion: <em>smintio-clapi-consumer-integration-application</em>
  ([JavaDoc](https://smintio.github.io/CLAPI-C-Integration-Core-Java/smintio-clapi-consumer-integration-application/1/))

  Contains an example application and a helper web server to ask the user
  to perform the necessary manual authorization with OAuth.
  If your application is a stand-alone application, this might be useful.


* EJB special overlay (untested): <em>smintio-clapi-consumer-integration-j2ee</em>
  ([JavaDoc](https://smintio.github.io/CLAPI-C-Integration-Core-Java/smintio-clapi-consumer-integration-j2ee/1/))

  In case this library will be used with an
  [EJB](https://en.wikipedia.org/wiki/Enterprise_JavaBeans) application,
  some variants of utility classes need to be replaced. The replacement
  implementation of these classes are part of this library here.



### Using gradle

Using gradle, you may add the following to your `build.gradle` file to use
the core library.

```

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()

        maven {
            url "https://smintio.pkgs.visualstudio.com/_packaging/CLAPIC-API-Clients/maven/v1"
            name "CLAPIC-API-Clients"
            credentials {
                username "smintio"
                password "${smintIoAzureAccessToken}"
            }
        }
    }


    dependencies {
         compile ("io.smint:smintio-clapi-consumer-integration-core:[1.5.0,2.0.0)")
         compile ("io.smint:smintio-clapi-consumer-integration-authorizer:[1.5.0,2.0.0)")
    }

```

The access token is stored in the gradle property `smintIoAzureAccessToken`.
Of course, it would be straight forward to add the property to the file
`gradle.properties`. Nevertheless preferable you should avoid to add the token to
your version control system. Otherwise everybody coud use extract and use it.
So use something like the plugin
[`com.github.b3er.local.properties`](https://github.com/b3er/gradle-local-properties-plugin),
which utilizes a file `local.properties`. This local file must not be added
to your version control system (eg: git).


### Using Maven

Using maven, you may add the following to your `pom.xml` file to use
the core library.

```xml

    <repositories>
        <!-- ... -->
        <repository>
            <id>CLAPIC-API-Clients</id>
            <url>https://smintio.pkgs.visualstudio.com/_packaging/CLAPIC-API-Clients/maven/v1</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

    <dependencies>
        <!-- ... -->
        <dependency>
            <groupId>io.smint</groupId>
            <artifactId>smintio-clapi-consumer-integration-core</artifactId>
            <version>[1.6.0,2.0.0)</version>
        </dependency>
        <dependency>
            <groupId>io.smint</groupId>
            <artifactId>smintio-clapi-consumer-integration-authorizer</artifactId>
            <version>[1.6.0,2.0.0)</version>
        </dependency>

    </dependencies>

```






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
        url "https://smintio.pkgs.visualstudio.com/_packaging/CLAPIC-API-Clients/maven/v1"
        name "CLAPIC-API-Clients"
        credentials {
            username "smintio"
            password "${smintIoAzureAccessToken}"
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
      <id>CLAPIC-API-Clients</id>
      <username>smintio</username>
      <!-- Treat this auth token like a password. Do not share it with anyone, including Microsoft support. -->
      <password>FILL_IN_USER_ACCESS_TOKEN</password>
    </server>
    ```

- Add this to your project `pom.xml` inside the `<repositories>` elements.

    ```
    <repository>
      <id>CLAPIC-API-Clients</id>
      <url>https://smintio.pkgs.visualstudio.com/_packaging/CLAPIC-API-Clients/maven/v1</url>
      <releases>
        <enabled>true</enabled>
      </releases>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
    </repository>
    ```




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


Topics of interest
------------------

### Study the Smint.io Integration Guide

Please also study the Smint.io Integration Guide which has been provided to you when you signed up as a Smint.io Solution Partner.


### That's it!

If there is any issues do not hesitate to drop us an email to [support@smint.io](mailto:support@smint.io) and we'll be happy to help!

Contributors
------------

- Nikolaus Rosenmayr, Smint.io GmbH
- Reinhard Holzner, Smint.io GmbH

© 2019 [Smint.io GmbH](https://www.smint.io)

Licensed under the [MIT License](https://opensource.org/licenses/MIT)