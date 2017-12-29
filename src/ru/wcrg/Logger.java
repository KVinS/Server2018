package ru.wcrg;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Эдуард on 09.12.2017.
 */

class LogMessage {
    private String message;
    private boolean error;
    LogMessage(String message, boolean error){
        this.message=message;
        this.error=error;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return error;
    }
}

public class Logger {

    private static boolean debug = true;
    private static boolean showThreadId = false;
    private static ConcurrentLinkedQueue<LogMessage> queue = new ConcurrentLinkedQueue<>();

    static {
        //Многопоточный логгер о смерти которого никто не узнает... Ведь веселее городить велосипед чем использовать Log4j
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        LogMessage logMessage = queue.poll();
                        while (logMessage != null) {
                            if (logMessage.isError()){
                                System.err.println(logMessage.getMessage());
                            } else {
                                System.out.println(logMessage.getMessage());
                            }
                            logMessage = queue.poll();
                        }
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    static void StartTrackThreads() {
        showThreadId = true;
    }

    static void StopTrackThreads() {
        showThreadId = true;
    }

    public static void Log(String message) {
        if (debug) {
            queue.add(new LogMessage(((showThreadId ? Thread.currentThread().getName() + " " : "") + Thread.currentThread().getId() + " " + message + " (" + System.currentTimeMillis() + ")"), false));
        }
    }

    public static void LogError(String message) {
        queue.add(new LogMessage(((showThreadId ? Thread.currentThread().getName() + " " : "") + Thread.currentThread().getId() + " " + message + " (" + System.currentTimeMillis() + ")"), true));
    }
}
