package chat.helpers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHelper {

    private static final String OUTPUT_DIRECTORY = "upload";

    public static void writeHistory(String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("history.txt", true))) {
            bw.newLine();
            bw.write(message);
            bw.flush();
        } catch (Exception e) {
            throw new RuntimeException("history writing error", e);
        }
    }

    public static String readHistory(int count) {
        StringBuilder result = new StringBuilder();
        File file = new File("history.txt");

        if (file.exists()) {
            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                StringBuilder tmp = new StringBuilder();
                long fileLength = raf.length() - 1;
                raf.seek(fileLength);
                int i = 0;
                long pointer = fileLength;

                do {
                    for (; pointer >= 0; pointer--) {
                        raf.seek(pointer);
                        char c = (char) raf.read();
                        if (c == '\n') {
                            i++;
                            pointer--;
                            break;
                        }
                        tmp.append(c);
                    }
                    tmp.append("\n");
                    result.insert(0, tmp.reverse());
                    tmp.setLength(0);
                } while (i <= count && pointer >= 0);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    public static void saveFile(InputStream from, Path path){
        Path out = Path.of(OUTPUT_DIRECTORY + File.separator + path.getFileName());
        try(OutputStream to = Files.newOutputStream(out)){
            from.transferTo(to);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
