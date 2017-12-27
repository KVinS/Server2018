package ru.wcrg.world.creatures.messages;

import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.Message;
import ru.wcrg.world.creatures.Animal;

/**
 * Created by Эдуард on 09.12.2017.
 */
public abstract class MessageToAnimal extends Message {
    public MessageToAnimal(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof Animal) {
            exec((Animal) abonent);
        }
    }

    protected abstract void exec(Animal animal);
}