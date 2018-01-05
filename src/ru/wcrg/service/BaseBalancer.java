package ru.wcrg.service;

import ru.wcrg.Logger;
import ru.wcrg.ThreadSettings;
import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.MessageSystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Эдуард on 05.01.2018.
 */
public class BaseBalancer implements Abonent, Runnable {
    protected final Address address = new Address();
    protected final MessageSystem messageSystem;
    protected final HashMap<BaseService, Long> servicesDuration;

    public BaseBalancer(MessageSystem messageSystem){
        this.messageSystem = messageSystem;
        messageSystem.addAbonent(this);
        servicesDuration = new HashMap<>();
    }

    @Override
    public void run() {
        while (true) {
            messageSystem.execForAbonent(this);

            for(Map.Entry<BaseService, Long> entry : servicesDuration.entrySet()) {
                long average = entry.getValue();
                Logger.Log(entry.getKey() +" average: " +average, 50);
                if (average > ThreadSettings.SERVICE_SLEEP_TIME){
                    BaseService service = entry.getKey();
                    Logger.LogError("Need new service for " + service);
                }
            }

            try {
                Thread.sleep(ThreadSettings.BALANCER_SLEEP_TIME);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public void reportDurationWork(BaseService baseService, long duration){
        long average = servicesDuration.get(baseService);
        long newAverage = (average + duration) / 2;
        servicesDuration.put(baseService, newAverage);
    }

    public void add(BaseService baseService){
        servicesDuration.put(baseService, 0L);
    }

    public void remove(BaseService baseService){
        servicesDuration.remove(baseService);
    }
}
