package io.liveness.flash.core.jws;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 *
 * @author trungnl
 * @modifier Sondc
 */



class PublicKeyReaderBio {

	private String filename;

	public PublicKeyReaderBio() {
	}

	public PublicKeyReaderBio(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public PublicKey getPublicKeyFromString(String pubKeyStr) throws IOException, GeneralSecurityException{

		PublicKey pubKey = null;

		try{
			InputStream is = new ByteArrayInputStream(pubKeyStr.getBytes());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder builder = new StringBuilder();
			boolean inKey = false;
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (!inKey) {
					if (line.startsWith("-----BEGIN ")
							&& line.endsWith(" PUBLIC KEY-----")) {
						inKey = true;
					}
					continue;
				} else {
					if (line.startsWith("-----END ")
							&& line.endsWith(" PUBLIC KEY-----")) {
						inKey = false;
						break;
					}
					builder.append(line);
				}
			}

			byte[] encoded = Base64.decode(builder.toString(), Base64.DEFAULT);
			KeySpec keySpec = new X509EncodedKeySpec(encoded);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			pubKey = kf.generatePublic(keySpec);

		}catch(Exception ex){
			ex.printStackTrace();
		}

		return pubKey;
	}

	public PublicKey getPublicKey (){

		PublicKey pubKey = null;
		FileInputStream fis = null;

		try {
			File f = new File(filename);
			fis = new FileInputStream(f);

			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			StringBuilder builder = new StringBuilder();
			boolean inKey = false;
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (!inKey) {
					if (line.startsWith("-----BEGIN ")
							&& line.endsWith(" PUBLIC KEY-----")) {
						inKey = true;
					}
					continue;
				} else {
					if (line.startsWith("-----END ")
							&& line.endsWith(" PUBLIC KEY-----")) {
						inKey = false;
						break;
					}
					builder.append(line);
				}
			}

			byte[] encoded = Base64.encodeToString(builder.toString().getBytes(), Base64.DEFAULT).getBytes();
			KeySpec keySpec = new X509EncodedKeySpec(encoded);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			pubKey = kf.generatePublic(keySpec);

		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception ign) {
				}
			}
		}

		return pubKey;
	}

	public PublicKey getPublicKeyFromCertString(String certString) {

		PublicKey pubkey = null;
		try {
			ByteArrayInputStream arrayin = new ByteArrayInputStream(certString.getBytes());
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			X509Certificate certificate = (X509Certificate) factory.generateCertificate(arrayin);
			pubkey = certificate.getPublicKey();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return pubkey;
	}

	public PublicKey getPublicKeyFromCert() {

		PublicKey pubkey = null;
		FileInputStream fis = null;

		try {
			File f = new File(filename);
			fis = new FileInputStream(f);

			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			X509Certificate certificate = (X509Certificate) factory.generateCertificate(fis);
			pubkey = certificate.getPublicKey();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return pubkey;
	}
}

