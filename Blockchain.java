import utils.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Blockchain implements Serializable {
    private static final long serialVersionUID = 3L;

    public static final int LOWER_LIMIT_MILLISECONDS = 1000;
    public static final int UPPER_LIMIT_MILLISECONDS = 3000;

    public static int hashComplexity = 3;

    private final ArrayList<Block> blockchain;
    private final String filename;

    Blockchain(String filename) {
        this.filename = filename;
        this.blockchain = (ArrayList<Block>) FileUtils.readFromFileOrCreate(filename);

        initStaticCounters();
        validate();
    }

    private void initStaticCounters() {
        Block lastBlock;
        int blockchainSize = blockchain.size();

        if (blockchainSize > 0) {
            lastBlock = blockchain.get(blockchainSize - 1);

            Block.idCounter = lastBlock.getId() + 1;

            List<Message> lastMessageList = lastBlock.getMessages();
            if (lastMessageList != null) {
                int messageListSize = lastMessageList.size();
                if (messageListSize > 0) {
                    Message lastMessage = lastMessageList.get(messageListSize - 1);
                    Message.idCounter = lastMessage.getId() + 1;
                }
            }
        }
    }

    public void generateBlock(List<String> data) {
        String previousHash = "0";
        int blockchainSize = blockchain.size();

        if (blockchainSize != 0) {
            previousHash = blockchain.get(blockchainSize - 1).getCurrentHash();
        }

        Block block = new Block(previousHash, data);
        blockchain.add(block);

        FileUtils.saveToFile(blockchain, filename);
    }

    private void validate() {
        String prevBlockHash = "0";

        for (Block block : this.blockchain) {
            if (!block.getPreviousHash().equals(prevBlockHash)) {
                throw new RuntimeException("Block with id=" + block.getId() + " has invalid hash");
            }
            if (block.getMessages() != null) {
                boolean isMessagesValid = block.getMessages().stream()
                        .collect(Collectors.partitioningBy(Message::verifySignature))
                        .get(false).size() == 0;

                if (!isMessagesValid) {
                    throw new RuntimeException("Block with id=" + block.getId() + " has messages with invalid signature");
                }
            }

            prevBlockHash = block.getCurrentHash();
        }

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Block block : this.blockchain) {
            builder.append(block.toString());
            builder.append('\n');
        }

        return builder.toString();
    }

}