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

    public static String Encrypt(final String plain , String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] keyData = Base64.encode(key.getBytes() , Base64.DEFAULT);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData , "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE , secretKeySpec);
        byte[] encrypted = cipher.doFinal(plain.getBytes());
        return new String(Base64.encode(encrypted , Base64.DEFAULT)).replaceAll("[!@#$%^&*()_+=<>:;'/.,]" , "").toUpperCase();
    }

    public static String Decrypt(final String plain) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        byte[] keyData = Base64.encode("Ishwar50150!".getBytes() , Base64.DEFAULT);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData , "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE , secretKeySpec);
        byte[] decrypted = cipher.doFinal(Base64.decode(plain.getBytes() , Base64.DEFAULT));
        return new String(decrypted);
    }
}
