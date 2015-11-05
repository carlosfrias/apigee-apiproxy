/**
 * Created by carlosfrias on 10/19/15.
 */
import apigee.*
import wslite.rest.*
import wslite.http.*
import wslite.http.auth.*

profile = new ApigeeProfile(
        hostURL: 'https://api.enterprise.apigee.com',
        orgName: 'carlosfrias',
        envName: 'test',
        application: 'yahoo-deleteme',
        apiVersion: 'v1',
        projectVersion: '1.0.0-SNAPSHOT',
        username: username,
        password: password,
        buildDirectory: 'build',
        baseDirectory: System.properties.'user.dir',
        apiProxySource: 'src/main/apiproxy',
        revision: '1'
)
apigee = new Apigee(profile: profile)

println profile.application
