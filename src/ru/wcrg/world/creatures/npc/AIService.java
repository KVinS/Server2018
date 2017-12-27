package ru.wcrg.world.creatures.npc;

import ru.wcrg.Logger;
import ru.wcrg.ThreadSettings;
import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.world.creatures.Animal;
import ru.wcrg.world.creatures.IAnimalController;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Эдуард on 28.12.2017.
 */
public class AIService implements Abonent, Runnable, IAnimalController {
    private final Address address = new Address();
    private final MessageSystem messageSystem;
    private ConcurrentHashMap<NPC,NPC> npcs = new ConcurrentHashMap<NPC,NPC>();

    public AIService(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        messageSystem.addAbonent(this);
        messageSystem.getAddressService().registerAIServices(this);
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public String toString(){
        return  "AIService";
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        while (true) {
            long startTime = System.currentTimeMillis();

            messageSystem.execForAbonent(this);

            npcs.values().stream().filter(npc -> npc.isLife()).forEach(NPC::AI);

            long currentTime = System.currentTimeMillis();
            long sleepTime = ThreadSettings.SERVICE_SLEEP_TIME - (currentTime - startTime);

            Logger.Log(this + " обработана за: " + (currentTime - startTime));

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

    public void registerNPC(NPC npc){
        npcs.put(npc,npc);
        npc.SetController(this);
    }

    @Override
    public void OnAnimalDied(Animal animal) {
        npcs.remove(animal);
    }
}
