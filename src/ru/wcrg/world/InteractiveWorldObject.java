package ru.wcrg.world;

import ru.wcrg.messaging.Abonent;
import ru.wcrg.messaging.Address;
import ru.wcrg.messaging.MessageSystem;

/**
 * Created by Эдуард on 11.12.2017.
 */
public class InteractiveWorldObject extends WorldObject implements Abonent{

    private final Address address = new Address();

    public InteractiveWorldObject(String title, GameWorld gameWorld, Fraction fraction, int x, int y, int z, MessageSystem messageSystem) {
        super(title, gameWorld, fraction, x, y, z);
        this.messageSystem = messageSystem;
    }

    @Override
    public Address getAddress() {
        return address;
    }
    private MessageSystem messageSystem;
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }


}
