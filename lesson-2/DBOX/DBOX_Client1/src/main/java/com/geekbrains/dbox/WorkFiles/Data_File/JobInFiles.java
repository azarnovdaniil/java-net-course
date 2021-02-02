package com.geekbrains.dbox.WorkFiles.Data_File;

import java.io.IOException;

public interface JobInFiles {
    void writerInFile(Object wStr) throws IOException;
    Object readInFile() throws IOException, ClassNotFoundException;

}
