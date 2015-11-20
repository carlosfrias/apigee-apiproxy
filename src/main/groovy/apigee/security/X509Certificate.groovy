package apigee.security

import sun.security.provider.X509Factory
import sun.security.tools.keytool.CertAndKeyGen
import sun.security.x509.X500Name

/**
 * Created by carlosfrias on 11/6/15.
 */
class X509Certificate {

/**
 * Create a self-signed X.509 Certificate
 * @param dn the X.509 Distinguished Name, eg "CN=Test, L=London, C=GB"
 * @param days how many days from now the Certificate is valid for, defaults to 365
 * @param algorithm either RSA or DSA, defaults to RSA
 * @param algorithmId the signing algorithm, defaults to "SHA1withRSA"
 */
    def generateCertificate(String dn, int days = 365, String algorithm = "RSA", String signingAlgorithm = "SHA1WithRSA") {
        CertAndKeyGen keyGen = new CertAndKeyGen(algorithm, signingAlgorithm, null)
        keyGen.generate(1024)
        today = new Date()
        nextYear = today + days
        X509Certificate cert = keyGen.getSelfCertificate(new X500Name("CN=ROOT"), nextYear.time)
        samplePem = new File('sample.pem')

        samplePem << X509Factory.BEGIN_CERT << '\n' << chain[0].encoded.encodeBase64() << '\n' << X509Factory.END_CERT
    }

}
