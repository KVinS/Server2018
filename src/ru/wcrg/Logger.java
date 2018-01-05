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
        private int logLevel;

        LogMessage(String message, int logLevel) {
            this.message = message;
            this.logLevel = logLevel;
        }

        public String getMessage() {
            return message;
        }

        public boolean isError() {
            return logLevel == ERROR_LEVEL;
        }
    }

    private static int ERROR_LEVEL = 1000;

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
    private static int logLevel = 0;
    private static boolean showThreadId = false;
    private static LoggerThread loggerThread;

    static {
        loggerThread = new LoggerThread(System.out, System.err);
        Thread logThread = new Thread(loggerThread);
        logThread.setName("LogThread");
        logThread.setDaemon(true);
        logThread.start();
    }

    public static void StartTrackThreads() {
        showThreadId = true;
    }

    public static void StopTrackThreads() {
        showThreadId = false;
    }

    public static void SetLogLevel(int newLogLevel) {
        logLevel = newLogLevel;
    }

    public static void Log(String message) {
        Log(message, 0);
    }

    public static void Log(String message, int logLevel) {
        if (debug && logLevel >= Logger.logLevel) {
            loggerThread.appendMessage(new LogMessage(((showThreadId ? Thread.currentThread().getName() + " " : "") + " " + message + " (" + System.currentTimeMillis() + ")"), logLevel));
        }
    }

    public static void LogError(String message) {
        loggerThread.appendMessage(new LogMessage(((showThreadId ? Thread.currentThread().getName() + " " : "") + " " + message + " (" + System.currentTimeMillis() + ")"), ERROR_LEVEL));
    }
}
