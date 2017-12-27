package ru.wcrg.utility;

/**
 * Created by Эдуард on 27.12.2017.
 */
public class Random {
    private static java.util.Random rand = new java.util.Random();

    public static int Range(int x, int y) {
        return rand.nextInt((y - x) + 1) + x;
    }
}
