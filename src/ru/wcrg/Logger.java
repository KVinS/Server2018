package ru.wcrg;

/**
 * Created by Эдуард on 09.12.2017.
 */
public class Logger {
    private static boolean debug = true;
    private static boolean showThreadId = false;

    public static void StartTrackThreads(){
        showThreadId = true;
    }

    public static void StopTrackThreads(){
        showThreadId = true;
    }

    public static void Log(String message){
        if (debug){
            System.out.println((showThreadId ? Thread.currentThread().getName() + " " : "") + Thread.currentThread().getId() + " " + message + " (" +System.currentTimeMillis()+ ")");
        }
    }
    public static void LogError(String message){
        System.err.println((showThreadId ? Thread.currentThread().getName() + " " : "") + message + " (" +System.currentTimeMillis()+ ")");
    }
}
