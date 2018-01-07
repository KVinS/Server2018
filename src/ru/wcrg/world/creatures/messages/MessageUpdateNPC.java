package ru.wcrg.world.creatures.messages;

import ru.wcrg.messaging.Address;
import ru.wcrg.world.creatures.npc.AIService;
import ru.wcrg.world.creatures.npc.NPC;

import java.util.List;

/**
 * Created by Эдуард on 07.01.2018.
 */
public class MessageUpdateNPC extends MessageToAIService {
    private List<NPC> npcs;

    public MessageUpdateNPC(Address from, Address to, List<NPC> npcs) {
        super(from, to);
        this.npcs = npcs;
    }

    @Override
    protected void exec(AIService AIService) {
        AIService.setNPC(npcs);
    }
}