package ru.wcrg.world.creatures.messages;

import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.Message;
import ru.wcrg.world.creatures.Animal;
import ru.wcrg.world.creatures.IAnimalController;

/**
 * Created by Эдуард on 07.01.2018.
 */
public abstract class MessageToIAnimalController extends Message {
    public MessageToIAnimalController(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof IAnimalController) {
            exec((IAnimalController) abonent);
        }
    }

    protected abstract void exec(IAnimalController controller);
}