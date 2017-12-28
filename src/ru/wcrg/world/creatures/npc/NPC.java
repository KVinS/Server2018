package ru.wcrg.world.creatures.npc;

import ru.wcrg.Logger;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.utility.Random;
import ru.wcrg.world.GameWorld;
import ru.wcrg.world.creatures.Animal;
import ru.wcrg.world.creatures.messages.MessageMoveTo;
import ru.wcrg.world.creatures.messages.MessageRegisterNPC;
import ru.wcrg.world.creatures.messages.MessageSetImpuls;
import ru.wcrg.world.creatures.messages.MessageToAIService;
import ru.wcrg.world.gameLogic.GameLogicService;
import ru.wcrg.world.gameLogic.messages.MessageAttack;

import java.util.Iterator;

/**
 * Created by Эдуард on 09.12.2017.
 */
public class NPC extends Animal {
    private SpawnerNPC spawner;

    public NPC(String name, int x, int y, int z, GameWorld gameWorld, SpawnerNPC spawner, Fraction fraction, MessageSystem messageSystem) {
        super(name, x, y, z, fraction, gameWorld, messageSystem);
        this.spawner = spawner;

        MessageToAIService messageToAIService = new MessageRegisterNPC(getAddress(), messageSystem.getAddressService().getAIServiceAddress(), this);
        messageSystem.sendMessage(messageToAIService);
    }

    public void gameTick(GameLogicService gameLogic) {
        super.gameTick(gameLogic);
    }

    public void AI(){
        Iterator<Animal> animalsIterator = gameWorld.getAnimals(x-50, z-50, x+50, z+50);
        while (animalsIterator.hasNext()) {
            Animal animal = animalsIterator.next();
            if (animal.isLife() && animal.getFraction() != getFraction()) {
                int damage = -getAttack() - Random.Range(1,10);
                Logger.Log(this + " attack " + animal + " tp " + damage + " damage.");
                getMessageSystem().sendMessage(new MessageAttack(this.getAddress(), animal.getAddress(), this, animal, damage));
            }
        }

        int x = Random.Range(-2,2);
        int z = Random.Range(-2,2);

        //getMessageSystem().sendMessage(new MessageMoveTo(this.getAddress(), this.getAddress(), x, 0, z));
        getMessageSystem().sendMessage(new MessageSetImpuls(this.getAddress(), this.getAddress(), x, 0, z));
    }

    @Override
    public void dying() {
        super.dying();
        if (spawner != null) {
            spawner.OnNPCDied(this);
        }
    }
}
