package ru.wcrg.world.creatures.npc;

import ru.wcrg.Logger;
import ru.wcrg.ThreadSettings;
import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.service.BaseBalancer;
import ru.wcrg.service.BaseService;
import ru.wcrg.world.creatures.Animal;
import ru.wcrg.world.creatures.IAnimalController;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Эдуард on 28.12.2017.
 */
public class AIService extends BaseService implements IAnimalController {
    private ConcurrentHashMap<NPC,NPC> npcs = new ConcurrentHashMap<NPC,NPC>();

    public AIService(BaseBalancer balancer, MessageSystem messageSystem) {
        super(balancer, messageSystem);
        messageSystem.getAddressService().registerAIServices(this);
    }


    @Override
    public String toString(){
        return  "AIService";
    }

    @Override
    protected void ServiceWork(){
        npcs.values().stream().filter(npc -> npc.isLife()).forEach(NPC::AI);
    }

    public void registerNPC(NPC npc){
        npcs.put(npc,npc);
        npc.SetController(this);
    }

    @Override
    public void OnAnimalDied(Animal animal) {
        npcs.remove(animal);
    }
}
