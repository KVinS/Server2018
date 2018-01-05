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
import ru.wcrg.world.creatures.Animal;

import java.util.Iterator;

/**
 * Created by Эдуард on 09.12.2017.
 */
public class GameLogicService extends BaseService {
    private int x, z, width, height;
    private GameWorld gameWorld;

    public GameLogicService(BaseBalancer balancer, MessageSystem messageSystem, int x, int z, int width, int height, GameWorld gameWorld) {
        super(balancer, messageSystem);
        this.x = x;
        this.z = z;
        this.width = width;
        this.height = height;

        this.gameWorld = gameWorld;

        //main.ru.wcrg.messaging.getAddressService().registerGameLogic(this);
    }

    @Override
    public String toString(){
        return  "GameLogicService " + x + ":" + z;
    }

    @Override
    protected void ServiceWork() {
        //Получаю список зверей, за которые отвечает этот обработчик
        Iterator<Animal> animalsIterator = gameWorld.getAnimals(x, z, x+width, z+height);
        while (animalsIterator.hasNext()) {
            //получаю животное на обработку
            Animal animal = animalsIterator.next();
            //провожу итерацию игровой логики в животном
            animal.gameTick(this);
            //применяю сообщения к животному
            messageSystem.execForAbonent(animal);
        }

        Iterator<InteractiveWorldObject> objectsIterator = gameWorld.getObjects(x, z, x+width, z+height);
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
