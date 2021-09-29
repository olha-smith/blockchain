package executor;

public class HashData {

    private final Long magicNumber;
    private final String hash;
    private final String sign;

    public HashData(Long magicNumber, String hash, String sign) {
        this.magicNumber = magicNumber;
        this.hash = hash;
        this.sign = sign;
    }

    public Long getMagicNumber() { return magicNumber; }
    public String getHash() { return hash; }
    public String getSign() { return sign; }
}