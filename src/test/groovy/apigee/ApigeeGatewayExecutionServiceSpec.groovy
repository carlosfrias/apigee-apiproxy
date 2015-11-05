package apigee

import spock.lang.Specification
import spock.lang.Stepwise

/**
 * Created by carlosfrias on 10/19/15.
 */
@Stepwise
class ApigeeGatewayExecutionServiceSpec extends Specification {

    Apigee apigee

    def setup() {
        def profile = new ApigeeProfile(
                hostURL: 'https://api.enterprise.apigee.com',
                orgName: 'carlosfrias',
                envName: 'test',
                application: 'yahoo-deleteme',
                apiVersion: 'v1',
                projectVersion: '1.0.0-SNAPSHOT',
                username: System.properties.username,
                password: System.properties.password,
                buildDirectory: 'build',
                baseDirectory: System.properties.'user.dir',
                revision: 1,
                apiProxySource: 'src/main/apiproxy',
//                clientId: System.properties.clientid,
//                clientSecret: System.properties.clientsecret
        )
        apigee = new Apigee(profile: profile)
    }

    def "require login credentials"() {
        given:
        def profile = apigee.profile
        def apiProxy = apigee.apiProxy
        profile.with {
            username = null
            password = null
        }

        when:
        apiProxy.lastRevision

        then:
        thrown(AssertionError)

    }

    def "validate api proxy url"() {
        def profile = apigee.profile


        given:
        profile

        when:
        def serviceURL = profile.apiProxyURL
        def serviceURI = profile.apiProxyURI

        then:
        serviceURL
        serviceURL == "https://api.enterprise.apigee.com/v1/organizations/carlosfrias/apis/yahoo-deleteme"
        serviceURI == serviceURL - profile.organizationURL
    }

    def "validate apiproxy zip filename"() {
        def profile = apigee.profile

        given:
        profile

        when:
        def name = profile.apiProxyZipFilename

        then:
        name
        name == "yahoo-deleteme-1.0.0-SNAPSHOT-1.zip"
    }

    def "create an apiproxy zip file"() {
        def profile = apigee.profile
        def apiProxy = apigee.apiProxy

        given:
        profile.revision = '1'
        apiProxy

        when:
        def file = apiProxy.createApiProxyZip()

        then:
        file
        file.exists()
        file.name.endsWith('.zip')
    }


//    def "create an empty apiproxy on edge"() {
//        given:
//        profile.revision = '1'
//        apiProxy
//
//        when:
//        def resp = apiProxy.createApiProxy
//
//        then:
//        resp
//    }


    def "Import an apiproxy zip file"() {
        def apiProxy = apigee.apiProxy

        given:
        apiProxy

        when:
        def result = apiProxy.importApiProxy()
        println "result: ${result.dump()}"
        println "result.request: ${result.request.dump()}"
        println "result.response: ${result.response.dump()}"

        then:
        result
        result.statusCode == 201
        result.statusMessage == 'Created'
    }

    def "list apiproxies in the organization"() {
        def apiProxy = apigee.apiProxy

        given:
        apiProxy

        when:
        def result = apiProxy.listApiProxies

        then:
        result
        result.json.size() > 0
        result.json.find { proxyName-> proxyName == profile.application}
    }

    def "retrieve apiproxies on the server"() {
        def apiProxy = apigee.apiProxy

        given:
        apiProxy

        when:
        def result = apiProxy.apiProxy

        then:
        result
        result.statusCode == 200
        result.json.revision.size() > 0
    }


    def "Get the latest revision on the server"() {
        def apiProxy = apigee.apiProxy

        given:
        apiProxy

        when:
        def result = apiProxy.lastRevision
        def totalRevisions = apiProxy.apiProxy.json.revision.size()

        then:
        result
        result == "$totalRevisions"

    }

    def "Deploy the last api proxy revision"() {
        def apiProxy = apigee.apiProxy

        given:
        apiProxy

        when:
        def result = apiProxy.deployApiProxy
        println "Deploy Api Proxy Response: \n ${result?.json.toString(2)}"

        then:
        result
        result.json

    }


    def "Undeploy the last api proxy revision"() {
        def apiProxy = apigee.apiProxy

        given:
        apiProxy

        when:
        def result = apiProxy.unDeployApiProxy
        println "Undeploy Api Proxy Response: \n ${result?.json.toString(2)}"

        then:
        result
        result.json

    }

    def "Delete api proxy revision"() {
        def apiProxy = apigee.apiProxy

        given:
        apiProxy

        when:
        def lastRevision = apiProxy.lastRevision
        def result = apiProxy.deleteApiProxyRevision

        then:
        result.json.revision == lastRevision

    }

    def "Remove api proxy from server"() {
        def apiProxy = apigee.apiProxy

        given:
        apiProxy

        when:
        def result = apiProxy.deleteApiProxy

        then:
        result.statusCode == 200
    }
}
