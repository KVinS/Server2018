package ru.wcrg.world.gameLogic.messages;

import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.Message;
import ru.wcrg.world.gameLogic.GameLogicService;

/**
 * Created by Эдуард on 09.12.2017.
 */
public abstract class MessageToGameLogic extends Message {
    public MessageToGameLogic(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GameLogicService) {
            exec((GameLogicService) abonent);
        }
    }

    protected abstract void exec(GameLogicService gameLogic);
}