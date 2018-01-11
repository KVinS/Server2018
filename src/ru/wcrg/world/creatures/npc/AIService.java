package ru.wcrg.world.creatures.npc;

import ru.wcrg.Logger;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.service.BaseBalancer;
import ru.wcrg.service.BaseService;
import ru.wcrg.world.creatures.Animal;
import ru.wcrg.world.creatures.IAnimalController;
import ru.wcrg.world.creatures.messages.MessageAddNPC;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Эдуард on 28.12.2017.
 */
public class AIService extends BaseService implements IAnimalController {
    private List<NPC> npcs;

    public AIService(String name, BaseBalancer balancer, MessageSystem messageSystem) {
        super(name, balancer, messageSystem);
        npcs = new LinkedList<NPC>();
    }

    @Override
    public AIService clone(){
        return new AIService("Clone " + name, balancer, messageSystem);
    }

    @Override
    protected void ServiceWork(){
        //Logger.Log(this + " count - " + npcs.size(), 45);
        npcs.stream().filter(Animal::isLife).forEach(NPC::AI);
    }

    public void registerNPC(NPC npc){
        npcs.add(npc);
        npc.setController(this);
    }

    @Override
    public void OnAnimalDied(Animal animal) {
        npcs.remove(animal);
    }

    public void setNPC(List<NPC> npcs) {
        this.npcs = npcs;
    }

    public void addNPC(List<NPC> npcs) {
        this.npcs.addAll(npcs);
    }

    @Override
    public void stop(Address inheritor){
        super.stop(inheritor);
        if (inheritor != null){
            messageSystem.sendMessage(new MessageAddNPC(address, inheritor, npcs));
        }
    }

    @Override
    public void divideLoadTo(Address helper) {
        LinkedList<NPC> npcsForOldService = new LinkedList<>();
        LinkedList<NPC> npcsForNewService = new LinkedList<>();

        int num = 1;
        for (NPC npc : npcs) {
            if (num % 2 == 0) {
                npcsForOldService.add(npc);
            } else {
                npcsForNewService.add(npc);
            }
            num++;
        }

        setNPC(npcsForOldService);
        messageSystem.sendMessage(new MessageAddNPC(address, helper, npcsForNewService));
    }
}
