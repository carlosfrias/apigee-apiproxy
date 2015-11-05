package apigee

import wslite.http.auth.HTTPBasicAuthorization
import wslite.rest.ContentType
import wslite.rest.RESTClient

/**
 * Created by carlosfrias on 10/29/15.
 */
class Apigee {

    ApigeeProfile profile
    ApiProxyService apiProxy
    TargetServersService targetServers

    def authorization(RESTClient restClient) {
        assert profile, "Please provide an instance of ApigeeProfile"
        assert restClient, "Please provide an instance of RESTClient"
        profile.with {
            restClient.httpClient.sslTrustAllCerts = true
            if (clientId && clientSecret) {
                println "Authorization Header set with clientid and clientsecret"
                restClient.authorization = new HTTPBasicAuthorization(
                        username: clientId,
                        password: clientSecret
                )
            } else {
                assert username, "Please login"
                assert password, "Please login"
                println "Authorization Header set with username and password"
                restClient.authorization = new HTTPBasicAuthorization(
                        username: username,
                        password: password
                )
            }
        }
        restClient
    }

    def login(username, password) {
        assert username, "Please provide the username"
        assert password, "Please provide the password"
        if (!profile)
            profile = new ApigeeProfile()
        profile.username = username
        profile.password = password
    }

    def getApiProxyService() {
        new ApiProxyService(apigee: this)
    }

    def getTargetServersService() {
        new TargetServersService(apigee: this)
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
