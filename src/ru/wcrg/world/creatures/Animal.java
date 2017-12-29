package ru.wcrg.world.creatures;

import ru.wcrg.Logger;
import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.utility.GTimer;
import ru.wcrg.world.GameWorld;
import ru.wcrg.world.WorldObject;
import ru.wcrg.world.creatures.messages.MessageGiveAward;
import ru.wcrg.world.creatures.npc.AIService;
import ru.wcrg.world.gameLogic.GameLogicService;

import java.util.HashMap;

/**
 * Created by Эдуард on 09.12.2017.
 */
public class Animal extends WorldObject implements Abonent {
    private IAnimalController controller;
    private boolean life = true;
    private final Address address = new Address();
    private MessageSystem messageSystem;
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }
    private HashMap<GTimer.Types, Long> time = new HashMap<>();
    private int attack = 1;

    private int hp;
    private int maxHp;

    public Animal(String name, int x, int y, int z, Fraction fraction, GameWorld gameWorld, MessageSystem messageSystem) {
        super(name, gameWorld, fraction, x, y, z);
        maxHp = 100;
        hp = maxHp;

        this.controller = controller;
        this.messageSystem = messageSystem;
        messageSystem.addAbonent(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public void dying(){
        life = false;
        Logger.Log(this + " умер");
        messageSystem.removeAbonent(this);
        gameWorld.removeAnimal(this);
        if (controller != null){
            controller.OnAnimalDied(this);
        }
    }

    public void changeHP(int deltaHp) {
        if (isLife()) {
            if (deltaHp < 0) {
                updateTime(GTimer.Types.lastDamage);
            }

            Logger.Log(this + " " + deltaHp + "hp");
            hp += deltaHp;
            if (hp <= 0) dying();
            if (hp > maxHp) hp = maxHp;
        }
    }

    @Override
    public void gameTick(GameLogicService gameLogic) {
        if (isLife()) {
            if (getHp() < getMaxHp() && getTime(GTimer.Types.lastDamage) + 10 < System.currentTimeMillis()) {
                changeHP(getMaxHp() / 10);
            }
        }
        super.gameTick(gameLogic);
    }

    public int getAttack()
    {
        return attack;
    }

    public int getHp(){
        return hp;
    }

    public int getMaxHp(){
        return maxHp;
    }

    public void attacked(Animal agressor, int deltaHp) {
        if (isLife()) {
            Logger.Log(agressor + " attack " + this + " to " + deltaHp + " damage.");
            changeHP(deltaHp);
            if(!isLife()) {
                getMessageSystem().sendMessage(new MessageGiveAward(this.getAddress(), agressor.getAddress(), this));
            }
        }
    }

    public boolean isLife() {
        return life;
    }

    public long getTime(GTimer.Types timer) {
        if (time.containsKey(timer)) {
            return time.get(timer);
        } else {
            return 0;
        }
    }

    public void updateTime(GTimer.Types timer) {
        time.put(timer, System.currentTimeMillis());
    }

    public int getPrice() {
        return attack;
    }

    @Override
    public String toString(){
        return getTitle() + " (" +getAttack()+ ") " + "{"+x+":"+y+":"+z+"}";
    }

    public void addExp(int price) {
        Logger.Log(this + " апнулся");
        attack += (price + 2);
        maxHp += (price + 2);
        hp = maxHp;
    }

    public void SetController(IAnimalController aiService) {
        controller = aiService;
    }
}
