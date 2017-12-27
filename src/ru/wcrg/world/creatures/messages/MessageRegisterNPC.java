package ru.wcrg.world.creatures.messages;

import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.Message;
import ru.wcrg.world.creatures.Animal;
import ru.wcrg.world.creatures.npc.AIService;
import ru.wcrg.world.creatures.npc.NPC;

/**
 * Created by Эдуард on 28.12.2017.
 */
public class MessageRegisterNPC extends MessageToAIService {
    private NPC newNPC;

    public MessageRegisterNPC(Address from, Address to, NPC npc) {
        super(from, to);
        newNPC = npc;
    }

    @Override
    protected void exec(AIService service) {
        service.registerNPC(newNPC);
    }
}