package ru.wcrg.world.creatures;

import ru.wcrg.messaging.Address;

/**
 * Created by Эдуард on 28.12.2017.
 */
public interface IAnimalController {
    void OnAnimalDied(Animal animal);
    Address getAddress();
}
