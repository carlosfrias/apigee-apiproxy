package apigee

import wslite.rest.ContentType
import wslite.rest.RESTClient

/**
 * Created by carlosfrias on 10/29/15.
 */
class TargetServersService {

    Apigee apigee
    ApigeeProfile profile
    String targetServer

    def restClient() {
        apigee.authorization(new RESTClient(profile.organizationURL))
    }

    def getTargetServerURL() {
        "${profile.organizationURL}/environments/${profile.envName}/targetservers"
    }

    def createTargetServer() {
        restClient.post() {
            type ContentType.JSON
            json {
                name
                host targetServerURL
                isEnabled true
                port 443
                SSLInfo(
                        enabled: true,
                        clientAuthEnabled: true,
                        keyStore: 'none',
                        trustStore: 'none',
                        keyAlias: 'key_alias',
                        ignoreValidationErrors: 'true',
                        cipers('cipher1', 'cipher2'),
                        protocols('protocol1', 'protocol2')
                )
            }
        }

    }
}
