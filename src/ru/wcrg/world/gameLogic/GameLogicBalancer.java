package ru.wcrg.world.gameLogic;

import ru.wcrg.Logger;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.service.BaseBalancer;
import ru.wcrg.service.BaseService;
import ru.wcrg.world.WorldZone;
import ru.wcrg.world.gameLogic.messages.MessageUpdateZone;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Эдуард on 07.01.2018.
 */
public class GameLogicBalancer extends BaseBalancer {
    private int divideCounter = 0;

    public GameLogicBalancer(MessageSystem messageSystem) {
        super(messageSystem);
    }

    @Override
    protected void divideLoad(BaseService baseService) {
        GameLogicService oldLogic = (GameLogicService) baseService;
        List<WorldZone> zones = oldLogic.getZones();

        LinkedList<WorldZone> zonesForOldService = new LinkedList<>();
        LinkedList<WorldZone> zonesForNewService = new LinkedList<>();

        if (zones.size() > 1){
            int num = 1;
            for (WorldZone zone : zones) {
                if (num % 2 == 0) {
                    zonesForOldService.add(zone);
                } else {
                    zonesForNewService.add(zone);
                }
                num++;
            }
        } else if (zones.size() == 1){
            WorldZone[] newZones = zones.get(0).divide();
            zonesForOldService.add(newZones[0]);
            zonesForNewService.add(newZones[1]);
        } else {
            Logger.LogError("Error divide load " + baseService);
        }

        servicesDuration.put(baseService, 0L);
        messageSystem.sendMessage(new MessageUpdateZone(getAddress(), oldLogic.getAddress(), zonesForOldService));

        GameLogicService newLogic = oldLogic.clone();
        newLogic.setZones(zonesForNewService);
        newLogic.setName("GLC"+divideCounter);

        final Thread gameLogicThread = new Thread(newLogic);
        gameLogicThread.setName("GLC"+divideCounter++);
        gameLogicThread.setDaemon(true);
        gameLogicThread.start();
        Logger.Log("Создан новый сервис LG", 45);
    }
}
