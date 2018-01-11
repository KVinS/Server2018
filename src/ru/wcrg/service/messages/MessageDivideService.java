package ru.wcrg.service.messages;

import ru.wcrg.messaging.Address;
import ru.wcrg.service.BaseService;

/**
 * Created by Эдуард on 12.01.2018.
 */
public class MessageDivideService extends MessageToService {
    private Address helper;

    public MessageDivideService(Address from, Address to, Address helper) {
        super(from, to);
        this.helper = helper;
    }

    @Override
    protected void exec(BaseService service) {
        service.divideLoadTo(helper);
    }

}
