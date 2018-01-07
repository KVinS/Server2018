package ru.wcrg.messaging;


import ru.wcrg.world.creatures.npc.AIBalancer;
import ru.wcrg.world.creatures.npc.AIService;

import java.util.ArrayList;
import java.util.List;


public final class AddressService {
    private List<Address> gameLogicServicesList = new ArrayList<>();
    private Address aiBalancer;

    public void registerAIBalancer(AIBalancer aiBalancer) {
        this.aiBalancer = aiBalancer.getAddress();
    }

    public synchronized Address getAIBalancerAddress() {
        return aiBalancer;
    }

    /*private AtomicInteger accountServiceCounter = new AtomicInteger();

    public void registerFrontEnd(FrontEnd frontEnd) {
        this.frontEnd = frontEnd.getAddress();
    }

    public void registerGameMechanics(GameMechanics gameMechanics) {
        this.gameMechanics = gameMechanics.getAddress();
    }

    public void registerAccountService(AccountService accountService) {
        accountServiceList.add(accountService.getAddress());
    }

    public Address getFrontEndAddress() {
        return frontEnd;
    }

    public Address getGameMechanicsAddress() {
        return gameMechanics;
    }

    public synchronized Address getAccountServiceAddress() {
        int index = accountServiceCounter.getAndIncrement();
        if (index >= accountServiceList.size()) {
            index = 0;
        }
        return accountServiceList.get(index);
    }*/
}
