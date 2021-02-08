package ru.gb.putilin.cloudstorage.client;

public interface FileManager {
    void uploadFile(String path);
    void downloadFile(String path);
    void showFiles();
}
