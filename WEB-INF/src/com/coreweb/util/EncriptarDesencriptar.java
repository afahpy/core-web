package com.coreweb.util;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Encripta y DesEncripta un string
 * 
 * @author daniel
 * 
 */
public class EncriptarDesencriptar {

	private Cipher ecipher;
	private Cipher dcipher;

	 // usa una frase por default
	private String frase = "Las computadoras son inutiles. Solo pueden darte respuestas";

	// 8-byte Salt
	private byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
			(byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

	// Iteration count
	int iterationCount = 19;

	public EncriptarDesencriptar() {
		this.init(frase);
	}

	public EncriptarDesencriptar(String clave) {
		this.init(clave);
	}

	private void init(String passPhrase) {
		try {
			// Create the key
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt,
					iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
					.generateSecret(keySpec);
			ecipher = Cipher.getInstance(key.getAlgorithm());
			dcipher = Cipher.getInstance(key.getAlgorithm());

			// Prepare the parameter to the ciphers
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
					iterationCount);

			// Create the ciphers
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String encriptar(String str) {
		try {

			// Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			String out = new sun.misc.BASE64Encoder().encode(enc);
			// le quito el último caracter, es siempre un =
			out = out.substring(0, out.length() - 1);
			return out;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String desEncriptar(String str) {
		try {
			// le pongo el último caracter, es siempre un =
			str += "=";

			// Decode base64 to get bytes
			byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

			// Decrypt
			byte[] utf8 = dcipher.doFinal(dec);

			// Decode using utf-8
			return new String(utf8, "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {

		EncriptarDesencriptar n = new EncriptarDesencriptar();
		EncriptarDesencriptar n2 = new EncriptarDesencriptar();

		int ii = 10;
		String err = "";
		for (int i = 0; i < ii; i++) {
			String desde = i + "";
			String enc = n.encriptar(desde);
			String desde2 = n2.desEncriptar(enc);
			if (desde.compareTo(desde2) != 0) {
				err += desde + "\n";
			} else {
			}
			System.out.println(i + "   " + enc);
		}
		System.out.println(ii);
		System.out.println("err:" + err);

	}

}
