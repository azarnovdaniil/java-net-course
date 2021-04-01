package chat.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class ChatFrame extends JFrame {
    private StringBuilder stringBuilder;
    private TextArea textArea;
    private ActionListener submitListener;

    public ChatFrame(Consumer<String> transmitter) {
        stringBuilder = new StringBuilder();

        setTitle("Chat Frame v1.0.0");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final int x = 150;
        final int y = 150;
        final int width = 400;
        final int height = 700;
        setBounds(new Rectangle(x, y, width, height));

        setLayout(new BorderLayout());

        textArea = new TextArea();
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.add(textArea, BorderLayout.CENTER);

        add(top, BorderLayout.CENTER);

        TextField textField = new TextField();
        JButton jButton = new JButton("Submit");
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        bottom.add(textField, BorderLayout.CENTER);
        bottom.add(jButton, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        submitListener = new SubmitActionListener(textField, transmitter);
        jButton.addActionListener(submitListener);
        textField.addActionListener(submitListener);

        setVisible(true);
    }

    public void append(String message) {
        stringBuilder.append(textArea.getText())
                .append("\n")
                .append(message);
        textArea.setText(stringBuilder.toString());
        stringBuilder.setLength(0);
    }

}
