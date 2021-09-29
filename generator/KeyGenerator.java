package generator;

import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyGenerator {
    private static final int keyLength = 1024;

    public static String privateKeyPath = "keys/privateKey";
    public static String publicKeyPath = "keys/publicKey";

    private static KeyGenerator keyGenerator;

    private KeyPairGenerator keyGen;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private File privateKeyFile;
    private File publicKeyFile;

    public KeyGenerator() {
        try {
            this.keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(keyLength);
            createKeys();

            this.privateKeyFile = new File(privateKeyPath);
            this.publicKeyFile = new File(publicKeyPath);

            if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
                FileUtils.writeToFile(publicKeyFile, publicKey.getEncoded());
                FileUtils.writeToFile(privateKeyFile, privateKey.getEncoded());
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
    }

    public static KeyGenerator getInstance() {
        if (keyGenerator == null) {
            keyGenerator = new KeyGenerator();
        }
        return keyGenerator;
    }

    public void createKeys() {
        KeyPair pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivate() throws Exception {
        byte[] keyBytes = Files.readAllBytes(privateKeyFile.toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public PublicKey getPublic() throws Exception {
        byte[] keyBytes = Files.readAllBytes(publicKeyFile.toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

}
