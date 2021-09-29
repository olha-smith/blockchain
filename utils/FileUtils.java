package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {

    public static Object createNewFile(File file) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Error while creating new file");
        }
        return new ArrayList<>();
    }

    public static Object readFromFile(File file) {
        try {
            if (file.length() == 0) {
                return new ArrayList<>();
            } else {
                return SerializationUtils.deserialize(file.getAbsolutePath());
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Something goes wrong while deserializing");
        }
    }

    public static void saveToFile(Object object, String filename) {
        try {
            SerializationUtils.serialize(object, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readFromFileOrCreate(String filename) {
        File file = new File(filename);

        if (!file.exists()) {
            return FileUtils.createNewFile(file);
        } else if (!file.isFile()) {
            throw new RuntimeException(filename + " is not a file");
        } else {
            return FileUtils.readFromFile(file);
        }
    }

    public static void writeToFile(File file, byte[] data) throws IOException {
        file.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.flush();
        fos.close();
    }

}
