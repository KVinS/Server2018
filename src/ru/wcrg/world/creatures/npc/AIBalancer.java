package ru.wcrg.world.creatures.npc;

import ru.wcrg.Logger;
import ru.wcrg.ThreadSettings;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.service.BaseBalancer;
import ru.wcrg.service.BaseService;
import ru.wcrg.service.messages.MessageStopService;
import ru.wcrg.world.creatures.messages.MessageAddNPC;
import ru.wcrg.world.creatures.messages.MessageRegisterNPC;
import ru.wcrg.world.creatures.messages.MessageUpdateNPC;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Эдуард on 07.01.2018.
 */
public class AIBalancer extends BaseBalancer {
    private int divideCounter = 0;

    public AIBalancer(MessageSystem messageSystem) {
        super(messageSystem);
        messageSystem.getAddressService().registerAIBalancer(this);

        sleepTime = ThreadSettings.AI_BALANCER_SLEEP_TIME;
    }

    @Override
    protected void optimizeLoad(Deque<BaseService> services) {
        while (services.size() >= 2) {
            AIService service1 = (AIService) services.removeFirst();
            AIService service2 = (AIService) services.removeLast();

            messageSystem.sendMessage(new MessageUpdateNPC(getAddress(), service1.getAddress(), service2.getNPC()));
            messageSystem.sendMessage(new MessageStopService(getAddress(), service2.getAddress()));
            remove(service2);
        }
    }


    @Override
    protected void divideLoad(BaseService baseService, Queue<BaseService> lowestServices) {
        AIService oldLogic = (AIService) baseService;
        List<NPC> npcs = oldLogic.getNPC();

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

        servicesDuration.put(baseService, 0L);
        messageSystem.sendMessage(new MessageUpdateNPC(getAddress(), oldLogic.getAddress(), npcsForOldService));

        AIService newLogic;
        if (lowestServices != null && lowestServices.size() > 0) {
            newLogic = (AIService) lowestServices.remove();
            Logger.Log("Часть нагрузки перенесена с " + oldLogic + " на " + newLogic, 45);
        } else {
            newLogic = oldLogic.clone();
            newLogic.setName("AIC"+divideCounter);
            final Thread aiThread = new Thread(newLogic);
            aiThread.setName("AIC"+divideCounter++);
            aiThread.setDaemon(true);
            aiThread.start();
            Logger.Log("Создан новый сервис AI для переноса нагрузки с " + oldLogic, 45);
        }

        messageSystem.sendMessage(new MessageAddNPC(getAddress(), oldLogic.getAddress(), npcsForNewService));
    }

    public void findControllerForNPC(NPC npc) {
        AIService service = (AIService) getLowestService();
        MessageRegisterNPC messageToBalancer = new MessageRegisterNPC(getAddress(), service.getAddress(), npc);
        messageSystem.sendMessage(messageToBalancer);
    }
}
