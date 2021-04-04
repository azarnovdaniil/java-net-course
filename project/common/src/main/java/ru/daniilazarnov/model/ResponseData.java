package ru.daniilazarnov.model;

public class ResponseData {

    private Integer length;
    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "ResponseData{"
                + "length=" + length
                + ", responseMessage='" + responseMessage
                + '\'' + '}';
    }
}
