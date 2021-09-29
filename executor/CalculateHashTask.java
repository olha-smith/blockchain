package executor;

import utils.Random;
import utils.StringUtils;

import java.util.concurrent.Callable;

class CalculateHashTask implements Callable<HashData> {

    private final String magicString;
    private final int hashComplexity;

    CalculateHashTask(String magicString, int hashComplexity) {
        this.magicString = magicString;
        this.hashComplexity = hashComplexity;
    }

    @Override
    public HashData call() {
        return calculateHash(magicString, hashComplexity);
    }

    private HashData calculateHash(String magicString, int hashComplexity) {
        String guessHash;
        long guessMagicNumber;
        String nullsPrefix = StringUtils.generateZeroString(hashComplexity);
        String threadName = Thread.currentThread().getName();

        do {
            guessMagicNumber = Random.getRandomNumber();
            guessHash = StringUtils.applySha256(magicString + guessMagicNumber);
        } while (!guessHash.startsWith(nullsPrefix));

        return new HashData(guessMagicNumber, guessHash, threadName);
    }
}
