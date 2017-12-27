package ru.wcrg.world.gameLogic.messages;

import ru.wcrg.messaging.Address;
import ru.wcrg.world.creatures.Animal;
import ru.wcrg.world.gameLogic.GameLogicService;

/**
 * Created by Эдуард on 09.12.2017.
 */
public class MessageAttack extends MessageToGameLogic {
    private int deltaHp;
    private Animal agressor;
    private Animal victim;

    public MessageAttack(Address from, Address to, Animal agressor, Animal victim, int deltaHp) {
        super(from, to);
        this.agressor = agressor;
        this.victim = victim;
        this.deltaHp = deltaHp;
    }

    @Override
    protected void exec(GameLogicService gameLogic) {
        victim.attacked(agressor, deltaHp);
    }
}
