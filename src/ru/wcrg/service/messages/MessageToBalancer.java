package ru.wcrg.service.messages;

import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.Message;
import ru.wcrg.service.BaseBalancer;
import ru.wcrg.world.creatures.Animal;

/**
 * Created by Эдуард on 05.01.2018.
 */
public abstract class MessageToBalancer extends Message {
    public MessageToBalancer(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof BaseBalancer) {
            exec((BaseBalancer) abonent);
        }
    }

    protected abstract void exec(BaseBalancer balancer);
}