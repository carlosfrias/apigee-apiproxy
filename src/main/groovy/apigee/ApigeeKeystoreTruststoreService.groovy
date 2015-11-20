package apigee

import wslite.rest.ContentType
import wslite.rest.RESTClient

/**
 * Created by carlosfrias on 11/9/15.
 */
class ApigeeKeystoreTruststoreService {

    Apigee apigee

    def getRestClient() {
        apigee.authorization(new RESTClient(apigee.profile.organizationURL))
    }

    def getUpdateStoreName(storeName) {
        "${apigee.profile.keystoreTruststoreURI}/${storeName}"
    }

    def getListAll() {
        def params = apigee.params
        params.path = apigee.profile.keystoreTruststoreURI
        params.headers.'Content-Type' = "${ContentType.JSON}"
        restClient.get(params)
    }

    def delete(storeName) {
        assert storeName, "Please provide the Keystore or Truststore name to be deleted."
        def params = apigee.params
        params.path = getUpdateStoreName(storeName)
        params.headers.'Content-Type' = "${ContentType.JSON}"
        restClient.delete(params)
    }

    def create(storeName) {
        assert storeName, "Please provide the name of the Keystore or Truststore name to be created."
        def params = apigee.params
        params.path = apigee.profile.keystoreTruststoreURI
        restClient.post(params) {
            type ContentType.JSON
            json name: storeName
        }
    }

    def getStore(storeName) {
        assert storeName, "Please provide the name of the Keystore or Truststore to be queried."
        def params = apigee.params
        params.path = getUpdateStoreName(storeName)
        params.headers.'Content-Type' = "${ContentType.JSON}"
        restClient.get(params)
    }

    def uploadKeystoreJarFor(storeName, password, File jarFile) {
        assert storeName, "Please provide the name of the Keystore to be uploaded."
        assert jarFile.exists(), "Jar file cannot be found on disk."
        def params = apigee.params
        params.path = "${getUpdateStoreName(storeName)}/keys"
        params.query.alias = storeName
        if(password) params.query.password = password
        restClient.post(params) {
            type ContentType.MULTIPART
            multipart 'file', jarFile.bytes
        }
    }


    def uploadTruststoreCertFor(storeName, File jarFile) {
        assert storeName, "Please provide the name of the Truststore to receive the certificate."
        assert jarFile.exists(), "Jar file cannot be found on disk."
        def params = apigee.params
        params.path = "${getUpdateStoreName(storeName)}/certs"
        params.query.alias = storeName
        if(password) params.query.password = password
        restClient.post(params) {
            type ContentType.MULTIPART
            multipart 'file', jarFile.bytes
        }
    }

}
