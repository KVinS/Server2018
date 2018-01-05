package ru.wcrg.service.messages;

import ru.wcrg.messaging.Address;
import ru.wcrg.service.BaseBalancer;
import ru.wcrg.service.BaseService;

/**
 * Created by Эдуард on 05.01.2018.
 */
public class MessageReportDuration extends MessageToBalancer {
    private BaseService service;
    private long duration;

    public MessageReportDuration(Address from, Address to, BaseService service, long duration) {
        super(from, to);
        this.service = service;
        this.duration = duration;
    }

    @Override
    protected void exec(BaseBalancer balancer) {
        balancer.reportDurationWork(service, duration);
    }
}
