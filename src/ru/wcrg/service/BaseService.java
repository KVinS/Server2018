package ru.wcrg.service;

import ru.wcrg.Logger;
import ru.wcrg.ThreadSettings;
import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.service.messages.MessageReportDuration;
import ru.wcrg.world.creatures.npc.NPC;

import javax.xml.ws.Service;

/**
 * Created by Эдуард on 04.01.2018.
 */
public abstract class BaseService implements Abonent, Runnable {
    protected String name;
    protected final BaseBalancer balancer;
    protected final Address address = new Address();
    protected final MessageSystem messageSystem;

    public BaseService(String name, BaseBalancer balancer, MessageSystem messageSystem){
        this.name = name;
        this.balancer = balancer;
        this.messageSystem = messageSystem;
        balancer.add(this);
        messageSystem.addAbonent(this);
    }

    protected abstract void ServiceWork();

    @Override
    public void run() {
        while (true) {
            long startTime = System.currentTimeMillis();

            messageSystem.execForAbonent(this);

            ServiceWork();

            long currentTime = System.currentTimeMillis();
            long duration = currentTime - startTime;
            long sleepTime = ThreadSettings.SERVICE_SLEEP_TIME - duration;

            Logger.Log(this + " обработана за: " + duration, 30);

            messageSystem.sendMessage(new MessageReportDuration(this.getAddress(), balancer.getAddress(), this, duration));

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

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public void setName(String newName){
        name = newName;
    }

    @Override
    public String toString(){
        return name;
    }
}
