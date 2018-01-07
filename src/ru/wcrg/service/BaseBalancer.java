package ru.wcrg.service;

import ru.wcrg.Logger;
import ru.wcrg.ThreadSettings;
import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.MessageSystem;

import java.util.*;

/**
 * Created by Эдуард on 05.01.2018.
 */
public abstract class BaseBalancer implements Abonent, Runnable {
    protected final Address address = new Address();
    protected final MessageSystem messageSystem;
    protected final HashMap<BaseService, Long> servicesDuration;
    protected int sleepTime = ThreadSettings.BALANCER_SLEEP_TIME;

    public BaseBalancer(MessageSystem messageSystem){
        this.messageSystem = messageSystem;
        messageSystem.addAbonent(this);
        servicesDuration = new HashMap<>();
    }

    @Override
    public void run() {
        //Некоторое время ничего не делать, чтобы просто собрать статистику
        try {
            Thread.sleep(ThreadSettings.BALANCER_QUARANTINE_TIME);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        while (true) {
            Queue<BaseService> queueToDivide = new LinkedList<>();
            Deque<BaseService> queueToOptimization = new LinkedList<>();

            messageSystem.execForAbonent(this);

            for(Map.Entry<BaseService, Long> entry : servicesDuration.entrySet()) {
                long average = entry.getValue();
                Logger.Log(entry.getKey() +" average: " +average, 50);
                BaseService service = entry.getKey();
                if (average > ThreadSettings.SERVICE_SLEEP_TIME){
                    //Добавляем сервис в очередь на разделение
                    queueToDivide.add(service);
                } else if (average < ThreadSettings.SERVICE_SLEEP_TIME / 4){
                    //Определяем список слабонагруженных сервисов,
                    queueToOptimization.add(service);
                }
            }

            //TODO: Добавить забор сервисов из queueToOptimization
            queueToDivide.forEach(this::divideLoad);

            if (queueToOptimization.size() > 1){
                optimizeLoad(queueToOptimization);
            }


            try {
                Thread.sleep(sleepTime);
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

    protected abstract void optimizeLoad(Deque<BaseService> baseService);
    protected abstract void divideLoad(BaseService baseService);

    public void add(BaseService baseService){
        servicesDuration.put(baseService, 0L);
    }

    public void remove(BaseService baseService){
        servicesDuration.remove(baseService);
    }

    protected BaseService getRandomService(){
        Random generator = new Random();
        Object[] entries = servicesDuration.keySet().toArray();
        return (BaseService) entries[generator.nextInt(entries.length)];
    }

    protected BaseService getLowestService(){
        return servicesDuration.entrySet().stream().min((e1, e2) -> Long.compare(e1.getValue(), e2.getValue())).get().getKey();
    }
}
