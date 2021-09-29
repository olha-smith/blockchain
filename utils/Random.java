package utils;

public class Random {

    static final java.util.Random random = new java.util.Random();

    public static Long getRandomNumber() {
        return random.nextLong();
    }

}
