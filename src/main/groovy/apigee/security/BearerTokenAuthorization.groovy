package apigee.security

import wslite.http.HTTP
import wslite.http.auth.HTTPAuthorization

/**
 * Created by carlosfrias on 11/23/15.
 */
class BearerTokenAuthorization implements HTTPAuthorization {

    def token
    def authorization

    def setToken(String _token) {
        token = _token
        authorization = null
    }

    def getToken() { return token }

    void authorize(conn) {
        conn.addRequestProperty(HTTP.AUTHORIZATION_HEADER, getAuthorization())
    }

    private String getAuthorization() {
        if (!authorization) {
            authorization = 'Bearer ' + "$token".toString().bytes.encodeBase64()
        }
        return authorization
    }
}
