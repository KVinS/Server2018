package ru.wcrg.service.messages;

import ru.wcrg.messaging.Address;
import ru.wcrg.service.BaseService;

/**
 * Created by Эдуард on 08.01.2018.
 */
public class MessageStopService extends MessageToService {
    public MessageStopService(Address from, Address to) {
        super(from, to);
    }

    @Override
    protected void exec(BaseService service) {
        service.stop();
    }
}
