package ru.wcrg.world.gameLogic;

import ru.wcrg.Logger;
import ru.wcrg.ThreadSettings;
import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.world.GameWorld;
import ru.wcrg.world.InteractiveWorldObject;
import ru.wcrg.world.creatures.Animal;

import java.util.Iterator;

/**
 * Created by Эдуард on 09.12.2017.
 */
public class GameLogicService implements Abonent, Runnable {
    private final Address address = new Address();
    private final MessageSystem messageSystem;

    private int x, z, width, height;
    private GameWorld gameWorld;

    public GameLogicService(int x, int z, int width, int height, GameWorld gameWorld, MessageSystem messageSystem) {
        this.x = x;
        this.z = z;
        this.width = width;
        this.height = height;

        this.gameWorld = gameWorld;

        this.messageSystem = messageSystem;
        messageSystem.addAbonent(this);
        //main.ru.wcrg.messaging.getAddressService().registerGameLogic(this);
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public String toString(){
        return  "GameLogicService " + x + ":" + z;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        while (true) {
            long startTime = System.currentTimeMillis();

            messageSystem.execForAbonent(this);

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


            long currentTime = System.currentTimeMillis();
            long sleepTime = ThreadSettings.SERVICE_SLEEP_TIME - (currentTime - startTime);

            Logger.Log(this + " обработана за: " + (currentTime - startTime));

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } else {
                Logger.LogError(this + " long work: " + sleepTime);
            }
        }
    }
}
