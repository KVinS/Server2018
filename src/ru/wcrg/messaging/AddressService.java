package ru.wcrg.messaging;


import ru.wcrg.world.creatures.npc.AIService;

import java.util.ArrayList;
import java.util.List;


public final class AddressService {
    private List<Address> gameLogicServicesList = new ArrayList<>();
    private List<Address> AIServicesList = new ArrayList<>();

    public void registerAIServices(AIService aiService) {
        AIServicesList.add(aiService.getAddress());
    }

    public synchronized Address getAIServiceAddress() {
        return AIServicesList.get(0);
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
