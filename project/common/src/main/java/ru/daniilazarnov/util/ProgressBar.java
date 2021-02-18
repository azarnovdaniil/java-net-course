package ru.daniilazarnov.util;

import static ru.daniilazarnov.constants.Constants.*;

public class ProgressBar {
    private final int length;
    private String line;
    private String pattern;
    private final boolean isProperlyInitialized;
    private static int done = 0;

    public ProgressBar(int length) {
        super();
        this.length = length;
        prepare();
        isProperlyInitialized = true;
    }

    private void prepare() {
        if (length < 0 && length > HUNDRED) {
            throw new IllegalArgumentException("Length of line is not in range of 0 and 100");
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            s.append("░"); //176
        }
        line = s.toString();
        pattern = String.format("[%%-%ds%%3d%%%%]\r", length);
    }

    void print(int step) {
        System.out.format(pattern, line.substring(0, step * line.length() / HUNDRED), step);
    }

    void nextPercent() throws Throwable {
        if (!isProperlyInitialized) {
            throw new NotProperlyInitialized("There was error in object initialization. Object can't be used");
        }
        if (done <= HUNDRED) {
            print(done);
            done++;
        }
    }

    public static class NotProperlyInitialized extends Throwable {
        public NotProperlyInitialized(String msg) {
            super(msg);
        }
    }

    public static void start(int delay) {
        ProgressBar pb;
        try {
            pb = new ProgressBar(TWENTY);
            for (int i = 0; i < ONE_HUNDRED_AND_FIVE; i++) {
                Thread.sleep(delay);
                pb.nextPercent();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}
