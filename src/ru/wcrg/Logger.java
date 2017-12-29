package ru.wcrg;

import com.sun.istack.internal.NotNull;

import java.io.PrintStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Эдуард on 09.12.2017.
 */


public class Logger {
    private static class LogMessage {
        private String message;
        private boolean error;

        LogMessage(String message, boolean error) {
            this.message = message;
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public boolean isError() {
            return error;
        }
    }


    private static class LoggerThread implements Runnable {
        private PrintStream log;
        private PrintStream errors;
        private static LinkedBlockingQueue<LogMessage> queue = new LinkedBlockingQueue<>();

        public LoggerThread(PrintStream log, PrintStream errors) {
            this.log = log;
            this.errors = errors;
        }

        public void appendMessage(@NotNull LogMessage logMessage) {
            queue.add(logMessage);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    LogMessage logMessage = queue.take();
                    if (logMessage.isError()) {
                        errors.println(logMessage.getMessage());
                    } else {
                        log.println(logMessage.getMessage());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean debug = true;
    private static boolean showThreadId = false;
    private static LoggerThread loggerThread;

    static {
        loggerThread = new LoggerThread(System.out, System.err);
        Thread logThread = new Thread(loggerThread);
        logThread.setName("LogThread");
        logThread.setDaemon(true);
        logThread.start();
    }

    static void StartTrackThreads() {
        showThreadId = true;
    }

    static void StopTrackThreads() {
        showThreadId = false;
    }

    public static void Log(String message) {
        if (debug) {
            loggerThread.appendMessage(new LogMessage(((showThreadId ? Thread.currentThread().getName() + " " : "") + Thread.currentThread().getId() + " " + message + " (" + System.currentTimeMillis() + ")"), false));
        }
    }

    public static void LogError(String message) {
        loggerThread.appendMessage(new LogMessage(((showThreadId ? Thread.currentThread().getName() + " " : "") + Thread.currentThread().getId() + " " + message + " (" + System.currentTimeMillis() + ")"), true));
    }
}
