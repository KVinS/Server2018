package ru.wcrg.world.creatures.npc;

import ru.wcrg.Logger;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.service.BaseBalancer;
import ru.wcrg.service.BaseService;
import ru.wcrg.world.WorldZone;
import ru.wcrg.world.creatures.messages.MessageFindControllerForNPC;
import ru.wcrg.world.creatures.messages.MessageRegisterNPC;
import ru.wcrg.world.creatures.messages.MessageUpdateNPC;
import ru.wcrg.world.gameLogic.GameLogicService;
import ru.wcrg.world.gameLogic.messages.MessageUpdateZone;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Эдуард on 07.01.2018.
 */
public class AIBalancer extends BaseBalancer {
    private int divideCounter = 0;

    public AIBalancer(MessageSystem messageSystem) {
        super(messageSystem);
        messageSystem.getAddressService().registerAIBalancer(this);
    }

    @Override
    protected void divideLoad(BaseService baseService) {
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

        AIService newLogic = oldLogic.clone();
        newLogic.setNPC(npcsForNewService);
        newLogic.setName("AIC"+divideCounter);

        final Thread aiThread = new Thread(newLogic);
        aiThread.setName("AIC"+divideCounter++);
        aiThread.setDaemon(true);
        aiThread.start();
        Logger.Log("Создан новый сервис AI", 45);
    }

    public void findControllerForNPC(NPC npc) {
        AIService service = (AIService) getLowestService();
        MessageRegisterNPC messageToBalancer = new MessageRegisterNPC(getAddress(), service.getAddress(), npc);
        messageSystem.sendMessage(messageToBalancer);
    }
}
