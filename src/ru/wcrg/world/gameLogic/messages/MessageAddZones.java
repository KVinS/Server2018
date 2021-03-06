package ru.wcrg.world.gameLogic.messages;

import ru.wcrg.messaging.Address;
import ru.wcrg.world.WorldZone;
import ru.wcrg.world.gameLogic.GameLogicService;

import java.util.List;

/**
 * Created by Эдуард on 06.01.2018.
 */
public class MessageAddZones extends MessageToGameLogic{
    private List<WorldZone> newZones;

    public MessageAddZones(Address from, Address to, List<WorldZone> newZones) {
        super(from, to);
        this.newZones = newZones;
    }

    @Override
    protected void exec(GameLogicService gameLogic) {
        gameLogic.addZones(newZones);
    }
}
