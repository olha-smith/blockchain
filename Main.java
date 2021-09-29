import executor.Executor;
import generator.KeyGenerator;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String blockchainPath = "data/blockchain.bcb";
        KeyGenerator.privateKeyPath = "data/keys/privateKey";
        KeyGenerator.publicKeyPath = "data/keys/publicKey";
        Executor.numberOfThreads = 10;

        Blockchain blockchain = new Blockchain(blockchainPath);

        for (int i = 0; i < 10; i++) {
            blockchain.generateBlock(List.of("Hi", "there", "!"));
        }

        System.out.println(blockchain);
        Executor.getInstance().threadPool.shutdown();
    }
}
