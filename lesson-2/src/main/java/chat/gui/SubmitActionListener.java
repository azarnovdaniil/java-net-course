package chat.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class SubmitActionListener implements ActionListener {
    private TextField textField;
    private Consumer<String> callback;

    public SubmitActionListener(TextField textField, Consumer<String> callback) {
        this.textField = textField;
        this.callback = callback;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = textField.getText();
        textField.setText("");
        callback.accept(message);
    }
}
