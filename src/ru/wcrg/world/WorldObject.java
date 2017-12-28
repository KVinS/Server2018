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
    protected int impX, impY, impZ;

    public WorldObject(String title, GameWorld gameWorld, Fraction fraction, int x, int y, int z) {
        this.title = title;
        this.gameWorld = gameWorld;
        this.fraction = fraction;
        this.x = x;
        this.y = y;
        this.z = z;
        impX = 0;
        impY = 0;
        impZ = 0;
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
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setImpuls(int x, int y, int z) {
        this.impX = x;
        this.impY = y;
        this.impZ = z;
    }

    public void gameTick(GameLogicService gameLogic) {
        Logger.Log(this.toString());

        this.x += impX;
        this.y += impY;
        this.z += impZ;

        //Хардкод на размер карты
        if (x < 0) {
            x = 0;
        }

        if (x > 199) {
            x = 199;
        }

        if (z < 0) {
            z = 0;
        }

        if (z > 199) {
            z = 199;
        }

        Logger.Log("moved to " + (x + " : " + y + " : " + z));

        if (this.impX != 0 || this.impY != 0 || this.impZ != 0) {
            this.impX = 0;
            this.impY = 0;
            this.impZ = 0;
            //TODO: Рассылка сообщений об изменении координат
        }
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
