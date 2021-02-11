package com.geekbrains.dbox.client;

import java.io.*;

public class Message {
    public byte com = 0;
    public Object obj = null;
    public boolean publiic = false;


    public byte[] HandlerOutToByte(byte comand, Object obj) {
        return addComandToArr(comand, convertObjToByte(obj));
    }
    public byte[] HandlerOutToByte() {
        return addComandToArr(com, convertObjToByte(obj));
    }
    public void HandlerInToMess(byte[] arr){
        Object[] oAr = exemptionComand(arr);
        com = (byte) oAr[0];
        obj = convertByteToObj((byte[])oAr[1]);
    }

    private byte[] convertObjToByte (Object obj){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] yourBytes = null;

        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            yourBytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
            }

            return  yourBytes;
        }
    }
    private byte[] addComandToArr (byte com, byte[] arr){
        byte[] nArr = new byte[arr.length + 1];
        nArr[0] = com;
        for (int i = 1; i < nArr.length; i++) {
            nArr[i] = arr[i-1];
        }
        return nArr;
    }
    private Object convertByteToObj (byte[] arr){
        Object o = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return o;
    }
    private Object[] exemptionComand (byte[] arr){
        byte res = arr[0];
        byte[] nArr = new byte[arr.length - 1];
        for (int i = 0; i < nArr.length; i++) {
            nArr[i] = arr[i + 1];
        }
        Object[] r = new Object[2];
        r[0] = res;
        r[1] = nArr;

        return r;
    }
}
