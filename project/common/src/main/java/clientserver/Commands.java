package clientserver;

public enum Commands {
    UPLOAD("upload", (byte) 1),  // [команда][имя файла на клиенте][путь назначения на сервере][размер файла][файл][][][]
    DOWNLOAD("download", (byte) 2),
    LS("ls", (byte) 3), // [команда]  // [ответ][список файлов ???]
    RM("rm", (byte) 4), // [команда][длина имени файла][имя файла] // [ответ]
    MKDIR("mkdir", (byte) 5), // [команда][длина имени][имя директории]
    MV("mv", (byte) 6), // [команда][]
    CD("cd", (byte) 7),
    RENAME("rename", (byte) 8);

    byte signal;

    Commands(String name, byte signal) {
        this.signal = signal;
    }

    public byte getSignal() {
        return signal;
    }

    public static Commands getCommand(byte code) {
        int index = code;
        switch (index) {
            case 1:
                return UPLOAD;
            case 2:
                return DOWNLOAD;
            case 3:
                return LS;
            case 4:
                return RM;
            case 5:
                return MKDIR;
            case 6:
                return MV;
            case 7:
                return CD;
            case 8:
                return RENAME;
            default:
                return null;
        }
    }
}
