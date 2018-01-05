package ru.wcrg.service;

import ru.wcrg.ThreadSettings;
import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.MessageSystem;

/**
 * Created by Эдуард on 05.01.2018.
 */
public class BaseBalancer implements Abonent, Runnable {
    protected final Address address = new Address();
    protected final MessageSystem messageSystem;

    public BaseBalancer(MessageSystem messageSystem){
        this.messageSystem = messageSystem;
        messageSystem.addAbonent(this);
    }

    @Override
    public void run() {
        while (true) {
            messageSystem.execForAbonent(this);

            try {
                Thread.sleep(ThreadSettings.BALANCER_SLEEP_TIME);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
