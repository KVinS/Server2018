package ru.wcrg.world.creatures.messages;
import ru.wcrg.messaging.Address;
import ru.wcrg.world.creatures.Animal;

/**
 * Created by Эдуард on 09.12.2017.
 */
public class MessageGiveAward extends MessageToAnimal {
    private Animal winner;
    private Animal loser;

    public MessageGiveAward(Address from, Address to, Animal loser) {
        super(from, to);
        this.loser = loser;
    }

    @Override
    protected void exec(Animal animal) {
        animal.addExp(loser.getPrice());
    }
}
