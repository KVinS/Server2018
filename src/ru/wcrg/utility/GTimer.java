package ru.wcrg.utility;

/**
 * Created by Эдуард on 10.12.2017.
 */
public class GTimer {
   public enum Types {
       lastDamage,
   }

    private boolean started = false;
    private long startTime;
    private long finishTime;

    private int duration;

    public GTimer(int duration) {
        this(duration, true);
    }

    public GTimer(int duration, boolean started) {
        this.duration = duration;
        if (started) {
            start();
        }
    }

    public void restart(){
        reset(true);
    }

    public void restart(int newDuration){
        this.duration = newDuration;
        reset(true);
    }

    public void reset(){
        reset(false);
    }

    public void reset(int newDuration){
        this.duration = newDuration;
        reset(false);
    }

    public void reset(boolean start){
        this.startTime = System.currentTimeMillis();
        this.finishTime = startTime + duration;
        started = start;
    }

    public void start(){
        reset(true);
    }

    public void stop(){
        started = false;
    }

    public boolean isStarted(){
        return started;
    }

    public boolean isCompleted(){
        return started && this.finishTime <= System.currentTimeMillis();
    }
}
