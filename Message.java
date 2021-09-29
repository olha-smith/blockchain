import generator.KeyGenerator;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public static Long idCounter = 0L;

    private final Long id;
    private final String data;
    private final byte[] signature;

    Message(String data) {
        this.id = idCounter++;
        this.data = data;
        this.signature = sign(data + id);
    }

    public byte[] sign(String data) {
        try {
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(KeyGenerator.getInstance().getPrivate());
            rsa.update(data.getBytes());
            return rsa.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean verifySignature() {
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(KeyGenerator.getInstance().getPublic());
            sig.update((this.data + this.id).getBytes(StandardCharsets.UTF_8));

            return sig.verify(this.signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "id=" + id + " data: " + data;
    }
}
