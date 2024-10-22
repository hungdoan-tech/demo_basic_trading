package com.hungdoan.aquariux;

import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;

public class RSAKeyPairGenerator {

    public static void main(String[] args) {
        try {
            generateKeyPair(2048);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateKeyPair(int keySize) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize);
        KeyPair keyPair = keyGen.generateKeyPair();

        saveKeyToFile("/run/secrets/public.pem", keyPair.getPublic());
        saveKeyToFile("/run/secrets/private.pem", keyPair.getPrivate());
    }

    private static void saveKeyToFile(String fileName, java.security.Key key) throws IOException {
        String keyString = Base64.getEncoder().encodeToString(key.getEncoded());
        String formattedKey = "-----BEGIN " + (key instanceof PublicKey ? "PUBLIC" : "PRIVATE") + " KEY-----\n" +
                keyString.replaceAll("(.{64})", "$1\n") + // Format the key for PEM
                "-----END " + (key instanceof PublicKey ? "PUBLIC" : "PRIVATE") + " KEY-----\n";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(formattedKey);
        }
    }
}
