package helpers;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import messages.FileMessage;
import messages.FileOperationType;
import ru.daniilazarnov.CredentialsEntry;
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

    private Socket socket;
    private CredentialsEntry user;

    public ClientFileHelper(Socket socket, CredentialsEntry user) {
        this.socket = socket;
        this.user = user;
    }

    public boolean uploadFile(Path path) {
        boolean res = false;
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream())) {

            FileMessage fm = new FileMessage(FileOperationType.UPLOAD, user, path.getFileName().toString(), "");

            out.writeObject(fm);
            out.flush();

            String answer = (String) in.readObject();

            if (answer.equals("ok")) {

                try (InputStream from = Files.newInputStream(path)) {
                    from.transferTo(out);
                    res = true;
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

        return res;
    }

    public boolean downloadFile(String filename) {
        boolean res = false;

        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream())) {

            FileMessage fm = new FileMessage(FileOperationType.DOWNLOAD, user, filename, "");

            out.writeObject(fm);
            out.flush();

            String answer = (String) in.readObject();

            if (answer.equals("ok")) {

                try (OutputStream to = Files.newOutputStream(Path.of(filename))) {
                    //todo check for file existence
                    in.transferTo(to);

                    res = true;
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

        return res;
    }

    public boolean renameFile(String oldName, String newName) {
        boolean res = false;

        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream())) {

            FileMessage fm = new FileMessage(FileOperationType.RENAME, user, oldName, newName);

            out.writeObject(fm);
            out.flush();

            String answer = (String) in.readObject();

            if (answer.equals("ok")) {
                res = true;
            } else {
                throw new FileOperationException(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public boolean removeFile(String fileName) {
        boolean res = false;
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream())) {

            FileMessage fm = new FileMessage(FileOperationType.DELETE, user, fileName, "");

            out.writeObject(fm);
            out.flush();

            String answer = (String) in.readObject();

            if (answer.equals("ok")) {
                res = true;
            } else {
                throw new FileOperationException(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
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
