package ru.daniilazarnov;


enum CommandList {
    help(1, false, ": Prints all the commands available. 'help currentCommand' will print you this command description"),
    login(2, false, ": Print 'login yourLogin yourPassword' to login storage server and use it's service"),
    register(3, false, ": Print 'login yourLogin yourPassword' to create a new account. After that, you will be instantly logined"),
    getFileList(4, false, ": Use 'getFileList' to check your server storage content"),
    upload(5, false, ": Use 'upload C:\\filePath||file.txt' to upload your file. Use '\\' instead of path if the file is in your repo"),
    download(6, false, ": Use 'download fileName.txt' to download a file from your storage to your repo folder"),
    delete(7, false, ": Use 'delete fileName.txt' to delete a file from your storage"),
    setRepository(8, false, ": Use 'setRepository C:\\folderPath' to change your repository folder"),
    exit(9, false, ": Shutting the application down"),
    connect(10, false, ": Your app will try to connect the server."),
    setHost(11, false, ": Use 'setHost newHost newPort' to change the settings"),
    serverMessage(12, true, ": System command"),
    fileUploadInfo(13, true, ": System command"),
    rename(14, false, ": Use this command to change a file name in your sorage 'rename oldName.txt newName.txt'");

    private final int num;
    private final boolean systemOnly;
    private final String description;

    CommandList(int num, boolean systemOnly, String description) {
        this.num = num;
        this.systemOnly = systemOnly;
        this.description = description;
    }

    public int getNum() {
        return num;
    }

    public boolean isSystemOnly() {
        return systemOnly;
    }

    public String getDescription() {
        return description;
    }

}