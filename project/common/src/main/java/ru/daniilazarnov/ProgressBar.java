package ru.daniilazarnov;

public class ProgressBar implements Constants {
    private int length;
    private String line;
    private String pattern;
    private boolean isProperlyInitialized;
    private static int done = 0;


    public ProgressBar(int length) throws Throwable {
        super();
        this.length = length;
        prepare();
        isProperlyInitialized = true;
    }

    public int getLength() {
        return length;
    }

    private void prepare() throws Throwable {
        if (length < 0 && length > HUNDRED) {
            throw new IllegalArgumentException("Length of line is not in range of 0 and 100");
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            s.append("*");
        }
        line = s.toString();
        pattern = String.format("[%%-%ds%%3d%%%%]\r", length);
    }

    void print(int step) throws Throwable {
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

    @Override
    public void nothing() {

    }

    public static class NotProperlyInitialized extends Throwable {
        public NotProperlyInitialized(String msg) {
            super(msg);
        }
    }

    public static void start() {
        ProgressBar pb = null;
        try {
            pb = new ProgressBar(TWENTY);
            for (int i = 0; i < ONE_HUNDRED_AND_FIVE; i++) {
                Thread.sleep(TEN);
                pb.nextPercent();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}
