package io.liveness.flash.core.jws;

import io.liveness.flash.core.utils.AppConfigBio;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;

import org.json.JSONObject;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class JwsUtilsBio {

    private static JwsUtilsBio jwsUtils;

    private static final String EID_PUB = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDjzCCAnegAwIBAgIEPhgWFTANBgkqhkiG9w0BAQsFADBbMScwJQYDVQQDDB5SZWdlcnkgU2Vs\n" +
            "Zi1TaWduZWQgQ2VydGlmaWNhdGUxIzAhBgNVBAoMGlJlZ2VyeSwgaHR0cHM6Ly9yZWdlcnkuY29t\n" +
            "MQswCQYDVQQGEwJVQTAgFw0yNDA0MTEwMDAwMDBaGA8yMTI0MDQxMTAzMTMwOVowUTEdMBsGA1UE\n" +
            "AwwUcXVhbmd0cnVuZ3F0cy5jb20udm4xIzAhBgNVBAoMGlJlZ2VyeSwgaHR0cHM6Ly9yZWdlcnku\n" +
            "Y29tMQswCQYDVQQGEwJVQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAIfK7LjzjqCo\n" +
            "VSzXz/ROHc2IyBMc89GwnR0slF1Lenavs+r+lnjFAxkVonBRTjtMj1pWqlACnd3qiIAD/8GbSagG\n" +
            "qsV43BDPbioDibWg/9wln82VLwEQohjLTl7VJtKuRAIUcg2nY4r5LNzpdClJx+k7zrIVDKSO8tRa\n" +
            "onU1dU6KLSmC2ZOzT10zrK4qmjvN/LFp0rlXJtdw++MUOIM9kccyi+3MK7iiraNV7Tlazy9xF0OZ\n" +
            "ytzgSX5R+oHE3aUS0M+W4p/dhihvLKjiejuw46E0dqEKxaqMJHXj2Qei1Ky1RrdRBNB0oQLCoUGx\n" +
            "KRaYw1CbZ7QWAgnrbqTvs1Y8pwUCAwEAAaNjMGEwDwYDVR0TAQH/BAUwAwEB/zAOBgNVHQ8BAf8E\n" +
            "BAMCAYYwHQYDVR0OBBYEFIlsqZHH0jmPvIjlF4YARXnamm7AMB8GA1UdIwQYMBaAFIlsqZHH0jmP\n" +
            "vIjlF4YARXnamm7AMA0GCSqGSIb3DQEBCwUAA4IBAQBfSk1XtHU8ix87g+lVzRQrEf7qsqWiwkN9\n" +
            "TW05qaPDMoMEoe/MW0YJZ+QwgvGMNLkEWjz/v0p1fVFF6kIolbo1o+1P6D4RCWvyB8S5zV9Mv+aR\n" +
            "1uWbAYiAA2uql/NrIJ3V1pJhIgRgDsRNuVP8MhNZc6DgJQLZOMKLwXsNHDtGOHk+ZcPiyWcjb4a3\n" +
            "voZCp4HN8+V2umO+QGuESZhTLihBnXv9HTpKxwWu4tK/4dgngDYM3UmChRjD/H7A3aYV4Xyxkqw2\n" +
            "rnd2LAr/zUEhFkbs21iG3DF0cHGKI15YzIq5pEhb9l4ePcCIgWgnJDNJPA/QhxpRB1XhP4bpK8kP\n" +
            "GJ8f\n" +
            "-----END CERTIFICATE-----";
    private String appLicense = "";

    public static JwsUtilsBio getInstance() {
        if (jwsUtils == null) {
            jwsUtils = new JwsUtilsBio();
        }
        return jwsUtils;
    }

    public void setAppLicense(String license) {
        this.appLicense = String.valueOf(license);
    }

    public String encrypt(JSONObject value) {
        JWSObject jwsObject = null;
        JWSSigner signer = null;
        JWEObject jweObject = null;
        JWEHeader jweHeader = null;
        RSAPrivateKey keySignJWS = null;//sign prikey

        PublicKeyReaderBio pubkeyEidReader_jws = new PublicKeyReaderBio();
        PrivateKeyReaderBio privatekeyEidReader_jwe = new PrivateKeyReaderBio();


        String senderPayload = null;
        Payload senderJwePayload = null;
        String senderJws = null;
        String senderJwe = null;
        RSAEncrypter encrypter = null;

        try {

            senderPayload = value.toString();
            senderJwePayload = new Payload(senderPayload);
//			System.out.print("Payload : " + senderPayload + "\n");
            // pack JWE
            jweHeader = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM);
//			jweHeader.setKeyID("PK.SD.KEK");
            jweObject = new JWEObject(jweHeader, senderJwePayload);
//			System.out.print("JWEDecrypt: Header: " + jweHeader.toJSONObject().toString() + "\n");
            String publicKey = EID_PUB;
            if (AppConfigBio.INSTANCE.getMLivenessRequest() != null) {
                publicKey = AppConfigBio.INSTANCE.getMLivenessRequest().getPublicKey();
                if (publicKey == null || publicKey.isEmpty()) {
                    publicKey = EID_PUB;
                }
            }
            encrypter = new RSAEncrypter((RSAPublicKey) pubkeyEidReader_jws.getPublicKeyFromCertString(publicKey));
            jweObject.encrypt(encrypter);
            senderJwe = jweObject.serialize();

//			System.out.print("JWE : " + senderJwe + "\n");
//			System.out.print("PublicKey : " + EID_PUB + "\n");
//			System.out.print("PrivateKey : " + this.appLicense + "\n");
            // pack JWS
            keySignJWS = (RSAPrivateKey) privatekeyEidReader_jwe.getPrivateKeyFromString(this.appLicense);
            signer = new RSASSASigner(keySignJWS);
            JWSHeader header = new JWSHeader(JWSAlgorithm.PS256);
//			header.set("SK.OCE.AUT");

            Payload pl = new Payload(senderJwe);
            jwsObject = new JWSObject(header, pl);
//			System.out.println("signer"+signer);
            jwsObject.sign(signer);
//			System.out.println("JWS: Header: " + header.toJSONObject().toString());
            senderJws = jwsObject.serialize();
//            System.out.print("JWS : " + senderJws + "\n");
            return senderJws;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
