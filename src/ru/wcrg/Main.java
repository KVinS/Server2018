package ru.wcrg;

import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.service.BaseBalancer;
import ru.wcrg.world.GameWorld;
import ru.wcrg.world.WorldObject;
import ru.wcrg.world.WorldZone;
import ru.wcrg.world.creatures.npc.AIBalancer;
import ru.wcrg.world.creatures.npc.AIService;
import ru.wcrg.world.creatures.npc.NPC;
import ru.wcrg.world.creatures.npc.SpawnerNPC;
import ru.wcrg.world.gameLogic.GameLogicBalancer;
import ru.wcrg.world.gameLogic.GameLogicService;

import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Server Started!");
        //Просто симуляция с кучей ботов
        //MainTest();

        //Генерация ботов на границе чанков
        //MainTest2();

        //Дофига ботов - проверка на быстродействее и стабильность
        StressTest();

        //Просто вывод цифр в консоль
        //LogTest();
    }

    private static void waitExit(){
        Scanner input = new Scanner(System.in);
        while (!input.nextLine().equals("Exit")) {

        }
    }

    private static void createGameLogicService(String name, BaseBalancer gameLogicBalancer, MessageSystem messageSystem, int x, int z, int sizeX, int sizeZ, GameWorld gameWorld){
        LinkedList<WorldZone> worldZones = new LinkedList<>();
        worldZones.add(new WorldZone(x, z, sizeX, sizeZ));

        GameLogicService gameLogic = new GameLogicService(name, gameLogicBalancer, messageSystem, worldZones, gameWorld);
        final Thread gameLogicThread = new Thread(gameLogic);
        gameLogicThread.setName(name);
        gameLogicThread.setDaemon(true);
        gameLogicThread.start();
    }

    private static void createAIService(MessageSystem messageSystem){
        final Thread aiService = new Thread(new AIService("AI0", createAIBalancer(messageSystem), messageSystem));
        aiService.setName("AI0");
        aiService.setDaemon(true);
        aiService.start();
    }

    private static BaseBalancer createAIBalancer(MessageSystem messageSystem){
        BaseBalancer aiBalancer = new AIBalancer(messageSystem);
        final Thread aiBalancerThread = new Thread(aiBalancer);
        aiBalancerThread.setName("AIB");
        aiBalancerThread.setDaemon(true);
        aiBalancerThread.start();
        return aiBalancer;
    }

    private static BaseBalancer createGameLogicBalancer(MessageSystem messageSystem){
        BaseBalancer gameLogicBalancer = new GameLogicBalancer(messageSystem);
        final Thread gameLogicBalancerThread = new Thread(gameLogicBalancer);
        gameLogicBalancerThread.setName("GLB");
        gameLogicBalancerThread.setDaemon(true);
        gameLogicBalancerThread.start();
        return gameLogicBalancer;
    }

    private static void MainTest(){
        Logger.SetLogLevel(10);

        final MessageSystem messageSystem = new MessageSystem();
        final BaseBalancer gameLogicBalancer = createGameLogicBalancer(messageSystem);
        createAIService(messageSystem);

        GameWorld gameWorld = new GameWorld();

        SpawnerNPC spawner1 = new SpawnerNPC(gameWorld, WorldObject.Fraction.Dogs, 10, 0, 50, 1000, 100, messageSystem);
        SpawnerNPC spawner2 = new SpawnerNPC(gameWorld, WorldObject.Fraction.Cats, 290, 0, 50, 1000, 100, messageSystem);

        gameWorld.addWorldObject(spawner1);
        gameWorld.addWorldObject(spawner2);

        int sizeX = 300;
        int sizeZ = 100;
        for (int x = 0; x < 2; x++) {
            createGameLogicService("GL"+x, gameLogicBalancer, messageSystem, x * sizeX, 0, sizeX, sizeZ, gameWorld);
        }

        waitExit();
    }

    private  static void MainTest2(){
        Logger.SetLogLevel(10);
        Logger.StartTrackThreads();
        final MessageSystem messageSystem = new MessageSystem();
        final BaseBalancer gameLogicBalancer = createGameLogicBalancer(messageSystem);
        createAIService(messageSystem);

        GameWorld gameWorld = new GameWorld();
        for (int i = 0; i < 1; i++) {
        NPC dog = new NPC("Dog"+i, 99, 0, 0, gameWorld, null, WorldObject.Fraction.Dogs, messageSystem);
        NPC cat = new NPC("Cat"+i, 101, 0, 0, gameWorld, null, WorldObject.Fraction.Cats, messageSystem);
        gameWorld.addAnimal(dog);
        gameWorld.addAnimal(cat);

        int sizeX = 100;
        int sizeZ = 100;
        for (int x = 0; x < 2; x++) {
            createGameLogicService("GL"+x, gameLogicBalancer, messageSystem, x * sizeX, 0, sizeX, sizeZ, gameWorld);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Конец теста №" + i);
        }
    }


    private static void StressTest(){
        Logger.SetLogLevel(40);
        final MessageSystem messageSystem = new MessageSystem();
        final BaseBalancer gameLogicBalancer = createGameLogicBalancer(messageSystem);
        createAIService(messageSystem);

        GameWorld gameWorld = new GameWorld();

        SpawnerNPC spawner1 = new SpawnerNPC(gameWorld, WorldObject.Fraction.Dogs, 10, 0, 50, 10, 10000, messageSystem);
        SpawnerNPC spawner2 = new SpawnerNPC(gameWorld, WorldObject.Fraction.Cats, 290, 0, 50, 10, 10000, messageSystem);

        gameWorld.addWorldObject(spawner1);
        gameWorld.addWorldObject(spawner2);

        int sizeX = 300;
        int sizeZ = 100;
        for (int x = 0; x < 2; x++) {
            createGameLogicService("GL"+x, gameLogicBalancer, messageSystem, x * sizeX, 0, sizeX, sizeZ, gameWorld);
        }

        waitExit();
    }

    private static void LogTest(){
        Logger.Log("1");
        Logger.LogError("2");
        Logger.Log("3");
        Logger.LogError("4");
        Logger.Log("5");
        Logger.LogError("6");
    }
}
