package ru.wcrg.world.gameLogic;

import ru.wcrg.Logger;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.service.BaseBalancer;
import ru.wcrg.service.BaseService;
import ru.wcrg.service.messages.MessageDivideService;
import ru.wcrg.service.messages.MessageStopService;

import java.util.Deque;
import java.util.Queue;

/**
 * Created by Эдуард on 07.01.2018.
 */
public class GameLogicBalancer extends BaseBalancer {
    private int divideCounter = 0;

    public GameLogicBalancer(MessageSystem messageSystem) {
        super(messageSystem);
    }

    @Override
    protected void optimizeLoad(Deque<BaseService> services) {
        while (services.size() >= 2) {
            GameLogicService service1 = (GameLogicService) services.removeFirst();
            GameLogicService service2 = (GameLogicService) services.removeLast();

            messageSystem.sendMessage(new MessageStopService(getAddress(), service2.getAddress(), service1.getAddress()));
            remove(service2);
        }
    }

    @Override
    protected void divideLoad(BaseService baseService, Queue<BaseService> lowestServices) {
        GameLogicService oldLogic = (GameLogicService) baseService;

        servicesDuration.put(baseService, 0L);

        GameLogicService newLogic;
        if (lowestServices != null && lowestServices.size() > 0) {
            newLogic = (GameLogicService) lowestServices.remove();
            Logger.Log("Часть нагрузки будет перенесена с " + oldLogic + " на " + newLogic, 45);
        } else {
            newLogic = oldLogic.clone();
            newLogic.setName("GLC"+divideCounter);
            final Thread gameLogicThread = new Thread(newLogic);
            gameLogicThread.setName("GLC"+divideCounter++);
            gameLogicThread.setDaemon(true);
            gameLogicThread.start();
            Logger.Log("Создан новый сервис GL для переноса нагрузки с " + oldLogic, 45);
        }

        messageSystem.sendMessage(new MessageDivideService(getAddress(), oldLogic.getAddress(), newLogic.getAddress()));
    }
}
