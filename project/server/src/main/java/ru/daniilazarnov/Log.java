package ru.daniilazarnov;

import java.io.IOException;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.logging.FileHandler;

import java.util.logging.Formatter;

import java.util.logging.Level;

import java.util.logging.LogRecord;

public class Log {

    public static final java.util.logging.Logger protocolLogger = java.util.logging.Logger.getLogger(Handler.class.getName());

    public static final java.util.logging.Logger echoLogger = java.util.logging.Logger.getLogger(EchoHandler.class.getName());


    public static void startLog() {
        protocolLogger.setLevel(Level.CONFIG);
        echoLogger.setLevel(Level.CONFIG);
        try {
            java.util.logging.Handler protocolLogHandler = new FileHandler("info_protocol", true);
            java.util.logging.Handler echoLogHandler = new FileHandler("echo_info_protocol", true);
            protocolLogHandler.setLevel(Level.CONFIG);
            echoLogHandler.setLevel(Level.CONFIG);
            protocolLogger.addHandler(protocolLogHandler);
            echoLogger.addHandler(echoLogHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        protocolLogger.getHandlers()[0].setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date date = new Date();
                return dateFormat.format(date) + "\tmsg:" + record.getMessage() + "\tlvl: " + record.getLevel() + "\n";
            }
        });

        echoLogger.getHandlers()[0].setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date date = new Date();
                return dateFormat.format(date) + "\tmsg:" + record.getMessage() + "\tlvl: " + record.getLevel() + "\n";
            }
        });
    }
}
