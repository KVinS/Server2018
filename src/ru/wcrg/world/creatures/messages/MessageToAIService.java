package ru.wcrg.world.creatures.messages;

import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.Message;
import ru.wcrg.world.creatures.npc.AIService;

/**
 * Created by Эдуард on 28.12.2017.
 */
public abstract class MessageToAIService extends Message {
    public MessageToAIService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AIService) {
            exec((AIService) abonent);
        }
    }

    protected abstract void exec(AIService service);
}
