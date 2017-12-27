package ru.wcrg.world;

import ru.wcrg.Logger;
import ru.wcrg.world.gameLogic.GameLogicService;

/**
 * Created by Эдуард on 09.12.2017.
 */
public class WorldObject {
    public enum Fraction {
        Dogs, Cats
    }

    private String title;
    protected GameWorld gameWorld;
    private Fraction fraction;

    protected int x, y, z;

    public WorldObject(String title, GameWorld gameWorld, Fraction fraction, int x, int y, int z) {
        this.title = title;
        this.gameWorld = gameWorld;
        this.fraction = fraction;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getZ(){
        return z;
    }

    public void moveTo(int x, int y, int z) {
        Logger.Log(this + " moved to " + (x +" : " + y + " : " +z));
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void gameTick(GameLogicService gameLogic) {

    }

    public String getTitle(){
        return title;
    }

    public Fraction getFraction(){
        return fraction;
    }

    @Override
    public String toString(){
        return title + " {"+x+":"+y+":"+z+"}";
    }
}
