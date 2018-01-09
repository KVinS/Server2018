package ru.wcrg.network;

import ru.wcrg.Logger;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.service.BaseBalancer;
import ru.wcrg.service.BaseService;

/**
 * Created by Эдуард on 08.01.2018.
 */
public class FrontendService extends BaseService {
    public FrontendService(String name, BaseBalancer balancer, MessageSystem messageSystem) {
        super(name, balancer, messageSystem);

        int port = 8080;
        FrontendServer frontendServer = new FrontendServer(port);
        try {
            frontendServer.run();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.LogError(e.toString());
        }
    }

    @Override
    protected void ServiceWork() {

    }
}
