package exercise.domain;

import java.io.Serializable;

public class MyMessageEx implements Serializable {

    private static final long serialVersionUID = 5193392663743561680L;

    private final String text;

    public MyMessageEx(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
