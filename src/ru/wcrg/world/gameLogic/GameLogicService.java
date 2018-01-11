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
import ru.wcrg.world.creatures.messages.MessageAddNPC;
import ru.wcrg.world.gameLogic.messages.MessageAddZones;

import java.util.Iterator;
import java.util.LinkedList;
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
        return new GameLogicService("Clone " + name, balancer, messageSystem, new LinkedList<>(), gameWorld);
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

    public void addZones(List<WorldZone> newZones) {
        this.worldZones.addAll(newZones);
    }

    @Override
    public void stop(Address inheritor){
        super.stop(inheritor);
        if (inheritor != null){
            messageSystem.sendMessage(new MessageAddZones(address, inheritor, worldZones));
        }
    }

    @Override
    public void divideLoadTo(Address helper) {
        LinkedList<WorldZone> zonesForOldService = new LinkedList<>();
        LinkedList<WorldZone> zonesForNewService = new LinkedList<>();

        if (worldZones.size() > 1){
            int num = 1;
            for (WorldZone zone : worldZones) {
                if (num % 2 == 0) {
                    zonesForOldService.add(zone);
                } else {
                    zonesForNewService.add(zone);
                }
                num++;
            }
        } else if (worldZones.size() == 1){
            WorldZone[] newZones = worldZones.get(0).divide();
            zonesForOldService.add(newZones[0]);
            zonesForNewService.add(newZones[1]);
        } else {
            Logger.LogError("Error divide load " + this);
        }

        setZones(zonesForOldService);
        messageSystem.sendMessage(new MessageAddZones(address, helper, zonesForNewService));
    }
}
