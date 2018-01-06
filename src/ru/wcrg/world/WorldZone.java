package ru.wcrg.world;

/**
 * Created by Эдуард on 06.01.2018.
 */
public class WorldZone{
    private int x, z, sizeX, sizeZ;

    public WorldZone(int x, int z, int sizeX, int sizeZ){
        this.x = x;
        this.z = z;
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
    }

    public WorldZone[] divide(){
        return new WorldZone[]{new WorldZone(x, z, sizeX/2, sizeZ), new WorldZone(x + sizeX/2, z, sizeX/2, sizeZ)};
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeZ() {
        return sizeZ;
    }
}