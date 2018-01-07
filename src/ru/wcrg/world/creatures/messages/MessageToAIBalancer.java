package ru.wcrg.world.creatures.messages;

import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.Message;
import ru.wcrg.world.creatures.npc.AIBalancer;
import ru.wcrg.world.creatures.npc.AIService;

/**
 * Created by Эдуард on 07.01.2018.
 */
public abstract class MessageToAIBalancer extends Message {
    public MessageToAIBalancer(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AIBalancer) {
            exec((AIBalancer) abonent);
        }
    }

    protected abstract void exec(AIBalancer service);
}
