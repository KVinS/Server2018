package ru.wcrg.world.creatures.messages;

import ru.wcrg.messaging.Address;
import ru.wcrg.world.creatures.Animal;
import ru.wcrg.world.creatures.IAnimalController;

/**
 * Created by Эдуард on 07.01.2018.
 */
public class MessageOnAnimalDie extends MessageToIAnimalController {
    private Animal animal;

    public MessageOnAnimalDie(Address from, Address to, Animal animal) {
        super(from, to);
        this.animal = animal;
    }

    @Override
    protected void exec(IAnimalController controller) {
        controller.OnAnimalDied(animal);
    }
}
