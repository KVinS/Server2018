package ru.wcrg.world;

import ru.wcrg.Logger;
import ru.wcrg.world.creatures.Animal;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Эдуард on 09.12.2017.
 */
public class GameWorld {
    public ConcurrentHashMap<Animal,Animal> animals = new ConcurrentHashMap<Animal,Animal>();
    private ConcurrentHashMap<InteractiveWorldObject,InteractiveWorldObject> objects = new ConcurrentHashMap<InteractiveWorldObject,InteractiveWorldObject>();

    public void addWorldObject(InteractiveWorldObject object) {
        objects.put(object, object);
    }

    public void addAnimal(Animal n) {
        animals.put(n,n);
    }

    public void removeAnimal(Animal n) {
        animals.remove(n);
    }

    public Iterator<Animal> getAnimals(int x, int z, int x2, int z2) {
        return animals.values().stream()
                .filter(a -> x <= a.getX() && a.getX() <= x2 &&  z <= a.getZ() && a.getZ() <= z2).iterator();
    }

    public Iterator<InteractiveWorldObject> getObjects(int x, int z, int x2, int z2) {
        return objects.values().stream()
                .filter(a -> x <= a.getX() && a.getX() <= x2 && z <= a.getZ() && a.getZ() <= z2).iterator();
    }

}
