package ru.wcrg.world.creatures.messages;

import ru.wcrg.messaging.Address;
import ru.wcrg.world.creatures.npc.AIBalancer;
import ru.wcrg.world.creatures.npc.NPC;

/**
 * Created by Эдуард on 07.01.2018.
 */
public class MessageFindControllerForNPC extends MessageToAIBalancer {
    private NPC npc;

    public MessageFindControllerForNPC(Address from, Address to, NPC npc) {
        super(from, to);
        this.npc = npc;
    }

    @Override
    protected void exec(AIBalancer balancer) {
        balancer.findControllerForNPC(npc);
    }
}