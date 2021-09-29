import executor.Executor;
import executor.HashData;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

class Block implements Serializable {
    private static final long serialVersionUID = 3L;

    public static Long idCounter = 0L;

    private final Long id;
    private final Long timestamp;
    private final String sign;
    private final Long generationTime;

    private final List<Message> messages;
    private final String previousHash;
    private final String currentHash;
    private final Long magicNumber;

    Block(String previousHash, List<String> data) {
        this.timestamp = System.currentTimeMillis();
        this.previousHash = previousHash;
        this.id = idCounter++;
        this.messages = data.stream().map(Message::new).collect(Collectors.toList());

        String magicString = id + timestamp + previousHash;
        HashData hashData = Executor.runCalculateHashTasks(magicString, Blockchain.hashComplexity);

        this.magicNumber = hashData.getMagicNumber();
        this.currentHash = hashData.getHash();
        this.sign = hashData.getSign();

        this.generationTime = System.currentTimeMillis() - timestamp;
        recalculateHashComplexity();
    }

    private void recalculateHashComplexity() {
        if (generationTime < Blockchain.LOWER_LIMIT_MILLISECONDS) {
            Blockchain.hashComplexity++;
        } else if (generationTime > Blockchain.UPPER_LIMIT_MILLISECONDS && Blockchain.hashComplexity > 0) {
                Blockchain.hashComplexity--;
        }
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Long getId() {
        return id;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getCurrentHash() {
        return currentHash;
    }

    @Override
    public String toString() {
        float timeInSeconds = generationTime / (float) 1000;

        return "Block:" + "\nCreated by thread #" + sign.charAt(sign.length() - 1) +
                "\nId: " + id +
                "\nTimestamp: " + timestamp +
                "\nMagic number: " + magicNumber +
                "\nHash of the previous block:\n" + previousHash +
                "\nHash of the block:\n" + currentHash +
                "\nBlock data:\n" + messages +
                "\nBlock was generating for " + timeInSeconds + " seconds\n";
    }
}
