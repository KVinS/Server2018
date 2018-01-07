package ru.wcrg.service.messages;

import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.Message;
import ru.wcrg.service.BaseService;

/**
 * Created by Эдуард on 08.01.2018.
 */
public abstract class MessageToService extends Message {
    public MessageToService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof BaseService) {
            exec((BaseService) abonent);
        }
    }

    protected abstract void exec(BaseService service);
}
