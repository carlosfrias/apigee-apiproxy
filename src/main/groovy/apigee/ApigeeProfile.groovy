package apigee

/**
 * Created by carlosfrias on 10/19/15.
 */
class ApigeeProfile {

    def application
    def projectVersion
    def artifactId
    def profileId
    def baseDirectory
    def buildDirectory
    def buildOption
    def hostURL
    def envName
    def apiVersion
    def revision
    def orgName
    def username
    def password
    def options
    def delay
    def overridedelay
    def apiProxySource
    def apiProxyEndpointProtocol = 'https'
    def clientId
    def clientSecret

    def getOrganizationURL() {
        assert hostURL, "Please provide the hostURL"
        assert apiVersion, "Please provide the apiVersion"
        assert orgName, "Please provide the orgName"
        "${hostURL}/${apiVersion}/organizations/${orgName}"
    }

    def getOrganizationApiURI() {
        "/apis"
    }

    def getOrganizationApiURL() {
        "${organizationURL}${organizationApiURI}"
    }

    def getApiProxyURI() {
        assert application, "Please provide the name of the application"
        "${organizationApiURI}/${application}"
    }

    def getApiProxyURL() {
        assert application, "Please provide the name of the application"
        "${organizationURL}${apiProxyURI}"
    }

    def getApiProxyRevisionURI() {
        assert revision, "Please provide the revision"
        "${apiProxyURI}/revisions/${revision}"
    }

    def getApiProxyRevisionURL() {
        assert revision, "Please provide the revision"
        "${apiProxyURL}${apiProxyRevisionURI}"
    }

    def getDeployApiProxyURI() {
        assert envName, "Please provide the envName"
        assert application, "Please provide the application"
        assert revision, "Please provide the revision"
        "/environments/${envName}/apis/${application}/revisions/${revision}/deployments"
    }

    def getDeployApiProxyURL() {
        assert envName, "Please provide the envName"
        assert application, "Please provide the application"
        assert revision, "Please provide the revision"
        "${organizationURL}/${deployApiProxyURI}"
    }

    def getApiProxyZipFilename() {
        assert application, "Please provide the application name"
        assert projectVersion, "Please provide projectVersion"
        assert revision, "Please provide revision"
        def selectedRevision = revision ? "-${revision}" : ''
        "${application}-${projectVersion}${selectedRevision}.zip"
    }

    def getApiProxyEndpointURI() {
        "/${application}"
    }
    def getAppServiceEndpointURL() {
        "${apiProxyEndpointURL}${apiProxyEndpointURI}"
    }

    def getApiProxyEndpointURL() {
        "${apiProxyEndpointProtocol}://${orgName}-${envName}.apigee.net"
    }

    def getRetrieveApiKeyEndpointURL() {
        "${hostURL}/v1/o/${orgName}/developers/${username}/apps/${application}"
    }

}
