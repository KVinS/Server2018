package ru.wcrg.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;


public final class MessageSystem {
    private final Map<Address, ConcurrentLinkedQueue<Message>> messages = new HashMap<>();
    private final AddressService addressService = new AddressService();

    public MessageSystem() {
    }

    public AddressService getAddressService() {
        return addressService;
    }

    public void addAbonent(Abonent abonent) {
        messages.put(abonent.getAddress(), new ConcurrentLinkedQueue<>());
    }

    public void removeAbonent(Abonent abonent) {
        messages.remove(abonent.getAddress());
    }
    public void sendMessage(Message message) {
        if (messages.get(message.getTo()) != null) {
            messages.get(message.getTo()).add(message);
        }
    }

    public void execForAbonent(Abonent abonent) {
        ConcurrentLinkedQueue<Message> queue = messages.get(abonent.getAddress());
        //messages.get(abonent.getAddress()); ConcurrentLinkedQueue
        try {
            while (!queue.isEmpty()) {
                Message message = queue.poll();
                message.exec(abonent);
            }
        } catch (Exception e) {

        }
    }
}
