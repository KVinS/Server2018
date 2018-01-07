package ru.wcrg.world.gameLogic;

import ru.wcrg.Logger;
import ru.wcrg.ThreadSettings;
import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.service.BaseBalancer;
import ru.wcrg.service.BaseService;
import ru.wcrg.world.GameWorld;
import ru.wcrg.world.InteractiveWorldObject;
import ru.wcrg.world.WorldZone;
import ru.wcrg.world.creatures.Animal;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Эдуард on 09.12.2017.
 */
public class GameLogicService extends BaseService {
    private List<WorldZone> worldZones;
    private GameWorld gameWorld;

    public GameLogicService(String name, BaseBalancer balancer, MessageSystem messageSystem, List<WorldZone> worldZones, GameWorld gameWorld) {
        super(name, balancer, messageSystem);
        this.worldZones = worldZones;
        this.gameWorld = gameWorld;

        //main.ru.wcrg.messaging.getAddressService().registerGameLogic(this);
    }

    @Override
    public GameLogicService clone(){
        return new GameLogicService("Clone " + name, balancer, messageSystem, null, gameWorld);
    }

    @Override
    protected void ServiceWork() {

        for (WorldZone worldZone : worldZones) {
            int x = worldZone.getX();
            int z = worldZone.getZ();
            int sizeX = worldZone.getSizeX();
            int sizeZ = worldZone.getSizeZ();

            //Получаю список зверей, за которые отвечает этот обработчик
        Iterator<Animal> animalsIterator = gameWorld.getAnimals(x, z, x+sizeX, z+sizeZ);
        while (animalsIterator.hasNext()) {
            //получаю животное на обработку
            Animal animal = animalsIterator.next();
            //провожу итерацию игровой логики в животном
            animal.gameTick(this);
            //применяю сообщения к животному
            messageSystem.execForAbonent(animal);
        }

        Iterator<InteractiveWorldObject> objectsIterator = gameWorld.getObjects(x, z, x+sizeX, z+sizeZ);
        while (objectsIterator.hasNext()) {
            //получаю объект на обработку
            InteractiveWorldObject object = objectsIterator.next();
            //провожу итерацию игровой логики в объекте
            object.gameTick(this);
            //применяю сообщения к объекту
            messageSystem.execForAbonent(object);
        }
    }
    }

    public void setZones(List<WorldZone> newZones) {
        this.worldZones = newZones;
    }

    public List<WorldZone> getZones() {
        return this.worldZones;
    }
}
