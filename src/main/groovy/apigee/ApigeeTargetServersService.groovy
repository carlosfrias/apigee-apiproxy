package apigee

import wslite.rest.ContentType
import wslite.rest.RESTClient

/**
 * Created by carlosfrias on 10/29/15.
 */
class ApigeeTargetServersService {

    Apigee apigee

    def getRestClient() {
        apigee.authorization(new RESTClient(apigee.profile.organizationURL))
    }

    def createTargetServer(targetServerName) {
        assert targetServerName, 'Please provide a target server name'
        def params = apigee.params
        params.path = apigee.profile.targetServerURI
        params.headers.'Content-Type' = ContentType.JSON.acceptHeader
        restClient.post(params)
    }

    def getListAll() {
        def params = apigee.params
        params.path = apigee.profile.targetServerURI
        params.headers.'Content-Type' = ContentType.JSON.acceptHeader
        restClient.get(params)
    }

    def getTargetServer(targetServername) {
        def params = apigee.params
        params.path = "${apigee.profile.targetServerURI}/$targetServername"
        params.headers.'Content-Type' = ContentType.JSON.acceptHeader
        restClient.get(params)
    }

    def deleteTargetServer(targetServername) {
        def params = apigee.params
        params.path = "${apigee.profile.targetServerURI}/$targetServername"
        params.headers.'Content-Type' = ContentType.JSON.acceptHeader
        restClient.delete(params)
    }
}
