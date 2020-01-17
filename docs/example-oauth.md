OAuth example
=============

To access the Smint.io API for fetching all assets, authentication is
needed. Authentication utilizes the [OAuth 2.0 standard](https://oauth.net/2/).


All you need is in the Smint.io library
---------------------------------------

To help you implement the authentication, Smint.io provides everything you need.
There is a separate package for it, that will help you a great deal.
See the [JavaDoc ISmintIoOAuthAuthorizer](smintio-clapi-consumer-integration-authorizer/1/io/smint/clapi/consumer/integration/core/authenticator/ISmintIoOAuthAuthorizer.html)
and the [Smint.io example Application authorizer](https://github.com/smintio/CLAPI-C-Integration-Core-Java/blob/master/smintio-clapi-consumer-integration-application/src/main/java/io/smint/clapi/consumer/integration/app/authenticator/SystemBrowserAuthenticator.java).

Utilize the package like that:

```groovy
    dependencies {
        // ...
        implementation (":smintio-clapi-consumer-integration-core:[1.8.0,2.0.0)")
        implementation (":smintio-clapi-consumer-integration-authorizer:[1.8.0,2.0.0)")
        // ...
    }

```

```xml
    <dependencies>

        <!-- ... -->

        <dependency>
            <groupId>io.smint</groupId>
            <artifactId>smintio-clapi-consumer-integration-core</artifactId>
            <version>[1.8.0,2.0.0)</version>
        </dependency>

        <dependency>
            <groupId>io.smint</groupId>
            <artifactId>smintio-clapi-consumer-integration-authorizer</artifactId>
            <version>[1.8.0,2.0.0)</version>
        </dependency>

        <!-- ... -->

    </dependencies>
```


How it works
------------

Stripped down to the most important part, OAuth works like this:

1. The user's browser need to open the authenticaion URL to Smint.io site.
2. The user will authenticate there and authorize the target DAM to access his Smint.io data.
3. Smint.io will redirect the browser to the target DAM's URL, passing
  the authorization data via GET-parameters.


To implement these steps you will:

1. Provide a global context for the HTTP servlet, like a static member in a class or such thing.
   The HTTP servlet need to access *the very same* authorizer instance that create the Smint.io
   authorization URL, as it contains some data to validate the received authorization data.
    ```Java
    public abstract class GlobalSmintIoServerContext {
        public ISmintIoOAuthAuthorizer smintioAuthorizer;
    }
    ```
2. Create the authentication URL and pass it to the user's browser
    (see [Smint.io example application implementation](https://github.com/smintio/CLAPI-C-Integration-Core-Java/blob/master/smintio-clapi-consumer-integration-application/src/main/java/io/smint/clapi/consumer/integration/app/ExampleApplication.java)).
    ```java

    GlobalSmintIoServerContext.smintioAuthorizer =
        new SmintIoOAuthAuthorizer(() -> mySettings, tokenStorage);

    final URL urlForUserBrowser = createAuthorizationUrl();

    ```
3. Wait for the user to finish authorization.
4. Provide an HTTP endpoint to receive authorization data and pass it to the Smint.io authorizer
   instance created previously
   (see [JavaDoc ISmintIoOAuthAuthorizer](smintio-clapi-consumer-integration-authorizer/1/io/smint/clapi/consumer/integration/core/authenticator/ISmintIoOAuthAuthorizer.html)).
    ```java
     public class ReceiveOAuthDataFromServiceViaBrowser extends HttpServlet {

         protected void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {

             GlobalSmintIoServerContext.smintioAuthorizer.analyzeAuthorizationData(
                 request.getParameterMap()
             );
             GlobalSmintIoServerContext.smintioAuthorizer = null;
         }

     }
    ```
