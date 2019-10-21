Smint.io CLAPI-C Java integration core library
==============================================

[![MIT](https://img.shields.io/github/license/smintio/CLAPI-C-Integration-Core.svg)](https://opensource.org/licenses/MIT)

The Smint.io Content Licensing Consumer Integration Core package provides a
common codebase for integration to
[Digital Asset management (DAM)](https://en.wikipedia.org/wiki/Digital_asset_management),
Web2Print, WCM or other systems, written in Java.



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

- Nikolaus Rosenmayr, Smint.io Smarter Interfaces GmbH
- Reinhard Holzner, Smint.io Smarter Interfaces GmbH

© 2019 [Smint.io Smarter Interfaces GmbH](https://www.smint.io)

Licensed under the [MIT License](https://opensource.org/licenses/MIT)
