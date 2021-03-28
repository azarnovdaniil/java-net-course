package ru.daniilazarnov;

public class ClientPathHolder implements PathHolder{

    String pathToRepo;
    long fileLenfth;

    ClientPathHolder (String pathToRepo){
        this.pathToRepo=pathToRepo;
    }

    @Override
    public void transComplete() {
        System.out.println("File downloaded successfully!");;
    }

    @Override
    public void setAuthority(String path) {
        this.pathToRepo=path;
    }

    @Override
    public String getAuthority() {
        return this.pathToRepo;
    }

    @Override
    public void setFileLength(long length) {
        this.fileLenfth=length;
    }

    @Override
    public long getFileLength() {
        return this.fileLenfth;
    }
}
