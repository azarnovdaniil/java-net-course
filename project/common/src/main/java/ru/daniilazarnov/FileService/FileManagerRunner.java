package ru.daniilazarnov.FileService;

class FileManagerRunner {
    public static void main(String[] args) {
        FileManager fileManager=new FileManager();
        fileManager.newStorage("client-storage");
    }
}