package helpers;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import messages.FileMessage;
import messages.FileOperationType;
import messages.AuthMessage;
import ru.daniilazarnov.FileOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ClientFileHelper {

    private final Socket socket;
    private final AuthMessage user;

    public ClientFileHelper(Socket socket, AuthMessage user) {
        this.socket = socket;
        this.user = user;
    }

    public void uploadFile(Path path) {
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream())) {

            FileMessage fm = new FileMessage(FileOperationType.UPLOAD, user, path.getFileName().toString(), "");

            out.writeObject(fm);
            out.flush();

            String answer = (String) in.readObject();

            if (answer.equals("ok")) {
                try (InputStream from = Files.newInputStream(path)) {
                    from.transferTo(out);
                } catch (IOException e) {
                    in.close();
                    out.close();
                    e.printStackTrace();
                }
            } else {
                throw new FileOperationException(answer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void downloadFile(String filename) {
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream())) {

            FileMessage fm = new FileMessage(FileOperationType.DOWNLOAD, user, filename, "");

            out.writeObject(fm);
            out.flush();

            String answer = (String) in.readObject();

            if (answer.equals("ok")) {
                try (OutputStream to = Files.newOutputStream(Path.of(filename))) {
                    in.transferTo(to);
                } catch (IOException e) {
                    in.close();
                    out.close();
                    e.printStackTrace();
                }
            } else {
                throw new FileOperationException(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void renameFile(String oldName, String newName) {
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream())) {

            FileMessage fm = new FileMessage(FileOperationType.RENAME, user, oldName, newName);

            out.writeObject(fm);
            out.flush();

            String answer = (String) in.readObject();

            if (!answer.equals("ok")) {
                throw new FileOperationException(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFile(String fileName) {
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream())) {

            FileMessage fm = new FileMessage(FileOperationType.DELETE, user, fileName, "");

            out.writeObject(fm);
            out.flush();

            String answer = (String) in.readObject();

            if (!answer.equals("ok")) {
                throw new FileOperationException(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Path> listFiles() {
        List<Path> res = new ArrayList<>();
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream())) {

            FileMessage fm = new FileMessage(FileOperationType.LIST, user, "", "");

            out.writeObject(fm);
            out.flush();

            //мдя...
            res = (List<Path>) in.readObject();

            if (res.isEmpty()) {
                throw new FileOperationException(res.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
