package apigee

import apigee.security.BearerTokenAuthorization
import wslite.http.auth.HTTPBasicAuthorization
import wslite.rest.ContentType
import wslite.rest.RESTClient

/**
 * Created by carlosfrias on 10/29/15.
 */
class Apigee {

    ApigeeProfile profile
    ApigeeApiProxyService apiProxy = new ApigeeApiProxyService(apigee: this)
    ApigeeKeystoreTruststoreService keystore = new ApigeeKeystoreTruststoreService(apigee: this)
    ApigeeTargetServersService targetServers = new ApigeeTargetServersService(apigee: this)

    def authorization(RESTClient restClient) {
        assert profile, "Please provide an instance of ApigeeProfile"
        assert restClient, "Please provide an instance of RESTClient"
        restClient.httpClient.sslTrustAllCerts = true
        assert profile?.username, "Please login"
        assert profile?.password, "Please login"
        println "Authorization Header set with username: $profile.username and password: $profile.password"
        restClient.authorization = new HTTPBasicAuthorization(
                username: profile.username,
                password: profile.password
        )
        restClient
    }

    def authorization(bearerToken, RESTClient restClient) {
        assert profile, "Please provide an instance of ApigeeProfile"
        assert restClient, "Please provide an instance of RESTClient"
        restClient.httpClient.sslTrustAllCerts = true
        assert profile?.username, "Please login"
        assert profile?.password, "Please login"
        println "Authorization Header set with username: $profile.username and password: $profile.password"
        restClient.authorization = new BearerTokenAuthorization(
                token: bearerToken
        )
        restClient
    }

    def getParams() {
        def params = [:]
        params.query = [:]
        params.headers = [:]
        params.headers.Accept = ContentType.ANY.acceptHeader
        params
    }

    def login(username, password) {
        assert username, "Please provide the username"
        assert password, "Please provide the password"
        if (!profile)
            profile = new ApigeeProfile()
        profile.username = username
        profile.password = password
    }

    def getRetrieveApiKey() {
        def restClient = authorization(new RESTClient(profile.retrieveApiKeyEndpointURL))
        def params = [:]
        params.headers = [:]
        params.headers.'Accept' = "${ContentType.JSON}"
        params.headers.'Content-Type' = "${ContentType.JSON}"
        restClient.get(params)
    }
}
