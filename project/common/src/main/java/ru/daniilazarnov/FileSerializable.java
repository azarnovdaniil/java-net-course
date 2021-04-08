package ru.daniilazarnov;


import java.io.Serializable;
import java.util.Arrays;

public class FileSerializable implements Serializable {
        private String path;
        private long lenght;
        private int part;
        private int partCount;
        private byte[] arr;

        public FileSerializable(String path, long lenght, int part, int partCount, byte[] arr) {
            this.path = path;
            this.lenght = lenght;
            this.part = part;
            this.partCount = partCount;
            this.arr = arr;
        }

        @Override
        public String toString() {
            return "FileSerializable{" +
                    "path='" + path + '\'' +
                    ", lenght=" + lenght +
                    ", part=" + part +
                    ", partCount=" + partCount +
                    ", arr=" + Arrays.toString(arr) +
                    '}';
        }

        public int getPartCount() {
            return partCount;
        }

        public String getPath() {
            return path;
        }

        public long getLenght() {
            return lenght;
        }

        public int getPart() {
            return part;
        }

        public byte[] getArr() {
            return arr;
        }
    }
