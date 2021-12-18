package com.hackathon.teachtube.Utils;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AuthEncrypter {

    public static String Encrypt(final String plain) {
        try {
            byte[] keyData = Base64.encode(("Ishwar50150!".getBytes()), Base64.DEFAULT);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
            Cipher cipher = null;

            cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(plain.getBytes());
            return new String(Base64.encode(encrypted, Base64.DEFAULT));
        } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String Decrypt(final String plain) {
        try {
            byte[] keyData = Base64.encode(("Ishwar50150!".getBytes()), Base64.DEFAULT);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decrypted = cipher.doFinal(Base64.decode(plain.getBytes(), Base64.DEFAULT));
            return new String(decrypted);
        } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
