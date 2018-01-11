package ru.wcrg.world.creatures.npc;

import ru.wcrg.Logger;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.utility.Random;
import ru.wcrg.world.GameWorld;
import ru.wcrg.world.creatures.Animal;
import ru.wcrg.world.creatures.messages.*;
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

        MessageFindControllerForNPC messageToBalancer = new MessageFindControllerForNPC(getAddress(), messageSystem.getAddressService().getAIBalancerAddress(), this);
        messageSystem.sendMessage(messageToBalancer);
    }

    public void gameTick(GameLogicService gameLogic) {
        super.gameTick(gameLogic);
    }

    public void AI(){
        Iterator<Animal> animalsIterator = gameWorld.getAnimals(x-10, z-10, x+10, z+10);
        while (animalsIterator.hasNext()) {
            Animal animal = animalsIterator.next();
            if (animal.isLife() && animal.getFraction() != getFraction()) {
                int damage = -getAttack() - Random.Range(1,10);
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
