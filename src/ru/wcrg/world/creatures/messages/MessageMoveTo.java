package ru.wcrg.world.creatures.messages;

import ru.wcrg.messaging.Address;
import ru.wcrg.world.creatures.Animal;

/**
 * Created by Эдуард on 28.12.2017.
 */
public class MessageMoveTo extends MessageToAnimal {
    private int x, y, z;

    public MessageMoveTo(Address from, Address to, int x, int y, int z) {
        super(from, to);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    protected void exec(Animal animal) {
        animal.moveTo(x, y, z);
    }
}
