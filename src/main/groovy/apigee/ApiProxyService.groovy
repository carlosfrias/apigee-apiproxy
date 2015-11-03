package apigee

import wslite.http.auth.HTTPBasicAuthorization
import wslite.rest.ContentType
import wslite.rest.RESTClient
import wslite.rest.RESTClientException

/**
 * Created by carlosfrias on 10/19/15.
 */
class ApiProxyService {

    Apigee apigee

    def getRestClient() {
        apigee.authorization(new RESTClient(apigee.profile.organizationURL))
    }

    def getAppRestClient() {
        authorization(new RESTClient(apigee.profile.appServiceEndpointURL))
    }
    def getCreateApiProxy() {
        assert apigee.profile?.application, "Please provide the name of the application"
        try {
            def params = [:]
            params.path = apigee.profile.organizationApiURI
            params.headers = [:]
            params.headers.'Content-Type' = "${ContentType.JSON}"
            restClient.post(params) {
                type:
                "${ContentType.JSON}"
                json name: apigee.profile.application
            }
        } catch (any) {
            any
        }
    }

    def getDeleteApiProxy() {
        unDeployAllApiProxyRevisions
        try {
            def params = [:]
            params.path = apigee.profile.apiProxyURI
            params.headers = [:]
            params.headers.'Accept' = '*/*'
            params.headers.'Content-Length' = '0'
            params.headers.'Content-Type' = "${ContentType.JSON}"
            restClient.delete(params)
        } catch (any) {
            any
        }
    }

    def getDeleteApiProxyRevision() {
        try {
            def params = [:]
            params.path = apigee.profile.apiProxyRevisionURI
            params.headers = [:]
            params.headers.'Accept' = '*/*'
            params.headers.'Content-Length' = '0'
            params.headers.'Content-Type' = "${ContentType.URLENC}"
            restClient.delete(params)
        } catch (any) {
            any
        }
    }

    def getDeployApiProxy() {
        try {
            def params = [:]
            params.path = apigee.profile.deployApiProxyURI
            params.query = [:]
            params.query.'override' = true
            params.headers = [:]
            params.headers.'Accept' = "${ContentType.JSON}"
            restClient.post(params)
        } catch (any) {
            any
        }
    }

    def getUnDeployApiProxy() {
        try {
            def params = [:]
            params.path = apigee.profile.deployApiProxyURI
            params.query = [:]
            params.query.'override' = true
            params.headers = [:]
            params.headers.'Accept' = "${ContentType.JSON}"
            restClient.delete(params)
        } catch (any) {
            any
        }
    }

    def getUnDeployAllApiProxyRevisions() {
        try {
            def apiProxyResp = apiProxy
            apiProxyResp.json.revision.each { revision ->
                apigee.profile.revision = revision
                unDeployApiProxy
            }
        } catch (any) {
            any
        }
    }

    def getExportApiProxy() {
        try {
            def params = [:]
            params.path = apigee.profile.apiProxyRevisionURI
            params.query = [:]
            params.query.format = 'bundle'
            params.headers = [:]
            params.headers.'Accept' = '*/*'
            params.headers.'Accept-Encoding' = 'gzip'
            params.headers.'Accept-Language' = 'en-US'
            params.headers.'Content-Type' = "${ContentType.BINARY}"
            def response = restClient.get(params)
            new File(apiProxyZipFilePath) << response.data
            println "Api Proxy Zip File Path: $apiProxyZipFilePath"
            response
        } catch (any) {
            any
        }
    }

    def getExtractExportApiProxy() {
        new AntBuilder().with {
            def dir = 'src/main/apiproxy'
            def dest = "$dir/${apigee.profile.application}-${apigee.profile.projectVersion}-${apigee.profile.revision}"
            mkdir(dir: dir)
            unzip(
                    src: apiProxyZipFilePath,
                    dest: dest
            )
        }
        new File(dest)
    }

    def getApiProxy() {
        try {
            def params = [:]
            params.path = apigee.profile.apiProxyURI
            params.accept = ContentType.JSON
            restClient.get(params)
        } catch (any) {
            any
        }
    }

    def getListApiProxies() {
        try {
            def params = [:]
            params.path = apigee.profile.organizationApiURI
            params.accept = ContentType.JSON
            restClient.get(params)
        } catch (any) {
            any
        }

    }

    def getApiProxyRevision() {
        try {
            def params = [:]
            params.path = apigee.profile.apiProxyRevisionURI
            params.accept = ContentType.JSON
            restClient.get(params)
        } catch (any) {
            any
        }
    }

    def importApiProxy() {
        assert apigee.profile?.application, "Please provide the name of the application"
        def params = [:]
        params.path = apigee.profile.organizationApiURI
        params.query = [:]
        params.query.'action' = 'import'
        params.query.'name' = "${apigee.profile.application}"
        params.query.'validate' = 'false'
        params.headers = [:]
        params.headers.'Accept' = '*/*'
        params.headers.'Accept-Encoding' = 'gzip'
        params.headers.'Accept-Language' = 'en-US'
        try {
            restClient.post(params) {
                type ContentType.MULTIPART
                multipart 'file', new File(apiProxyZipFilePath).bytes
            }
        } catch (any) {
            any
        }
    }

    def getLastRevision() {
        try {
            apigee.profile.revision = apiProxy.json.revision.last()
        } catch (any) {
            any
        }
    }

    def authorization(RESTClient restClient) {
        apigee.profile.with {
            assert username, "Please login"
            assert password, "Please login"
            restClient.authorization = new HTTPBasicAuthorization(
                    username: apigee.profile.username,
                    password: apigee.profile.password
            )
            restClient.httpClient.sslTrustAllCerts = true
        }
        restClient
    }


    String getApiProxyZipFilePath() {
        assert apigee.profile?.buildDirectory, "Please provide the buildDirectory."
        assert apigee.profile?.baseDirectory, "Please provide the baseDirectory."
        apigee.profile.with {
            def dir = "${baseDirectory}${File.separator}${buildDirectory}"
            new AntBuilder().mkdir(dir: dir)
            "${dir}${File.separator}${apiProxyZipFilename}"
        }
    }

    def createApiProxyZip() {
        assert apigee.profile?.apiProxySource, "Please provide the folder for the apiProxySource"
        new AntBuilder().with {
            delete(file: apiProxyZipFilePath)
            zip(destfile: apiProxyZipFilePath) {
                zipfileset(dir: "${apigee.profile.apiProxySource}", prefix: "apiproxy")
            }
        }
        new File(apiProxyZipFilePath)
    }

    def redeployApiProxy() {
        println "Starting redeploy for api proxy ${apigee.profile.application}"
        def responses = [:]
        def resp
        println "Obtaining list of Api Proxies for ${apigee.profile.application}"
        def apiProxyResponse = apiProxy
        responses.apiProxy = apiProxyResponse
        if (!(apiProxyResponse instanceof RESTClientException) && apiProxyResponse.contentType.contains('json')) {
            apiProxyResponse.json.revision.each { revision ->
                println "Removing revision $revision"
                apigee.profile.revision = revision
                println "Undeploying revision $revision"
                resp = unDeployApiProxy
                responses.unDeployApiProxyRevision = resp
                println "Removing revision $revision"
                resp = deleteApiProxyRevision
                responses.deleteApiProxyRevision = resp
            }
            println "Removing Api Proxy: ${apigee.profile.application}"
            resp = deleteApiProxy
            responses.deleteApiProxy = resp
            println "Creating api proxy zip archive for ${apigee.profile.application}"
            createApiProxyZip()
            println "Importing api proxy zip archive for ${apigee.profile.application}"
            resp = importApiProxy()
            responses.importApiProxy = resp
            println "Deploying api proxy ${apigee.profile.application}"
            resp = deployApiProxy
            responses.deployApiProxy = resp
        }
        println "Completed redeploy for api proxy: ${apigee.profile.application}"
    }

    def getApiKeys() {

    }
}
