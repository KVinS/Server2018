package ru.wcrg;

import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.world.GameWorld;
import ru.wcrg.world.WorldObject;
import ru.wcrg.world.creatures.npc.AIService;
import ru.wcrg.world.creatures.npc.NPC;
import ru.wcrg.world.creatures.npc.SpawnerNPC;
import ru.wcrg.world.gameLogic.GameLogicService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Server Started!");
        //Просто симуляция с кучей ботов
        //MainTest();

        //Генерация ботов на границе чанков
        MainTest2();

        //Дофига ботов - проверка на быстродействее и стабильность
        //StressTest();
    }

    private static void waitExit(){
        Scanner input = new Scanner(System.in);
        while (!input.nextLine().equals("Exit")) {

        }
    }

    private static void createAIService(MessageSystem messageSystem){
        final Thread aiService = new Thread(new AIService(messageSystem));
        aiService.setName("AIService"+0);
        aiService.setDaemon(true);
        aiService.start();
    }

    private static void MainTest(){
        final MessageSystem messageSystem = new MessageSystem();
        createAIService(messageSystem);

        GameWorld gameWorld = new GameWorld();

        SpawnerNPC spawner1 = new SpawnerNPC(gameWorld, WorldObject.Fraction.Dogs, 10, 0, 50, 1000, 100, messageSystem);
        SpawnerNPC spawner2 = new SpawnerNPC(gameWorld, WorldObject.Fraction.Cats, 290, 0, 50, 1000, 100, messageSystem);

        gameWorld.addWorldObject(spawner1);
        gameWorld.addWorldObject(spawner2);

        for (int x = 0; x < 2; x++) {
            final Thread gameLogic = new Thread(new GameLogicService(x * 100, 0, 300, 300, gameWorld, messageSystem));
            gameLogic.setName("GameLogicService "+x);
            gameLogic.setDaemon(true);
            gameLogic.start();
        }

        waitExit();
    }

    private  static void MainTest2(){
        Logger.StartTrackThreads();
        final MessageSystem messageSystem = new MessageSystem();
        createAIService(messageSystem);

        GameWorld gameWorld = new GameWorld();
        for (int i = 0; i < 1; i++) {
        NPC dog = new NPC("Dog"+i, 99, 0, 0, gameWorld, null, WorldObject.Fraction.Dogs, messageSystem);
        NPC cat = new NPC("Cat"+i, 101, 0, 0, gameWorld, null, WorldObject.Fraction.Cats, messageSystem);
        gameWorld.addAnimal(dog);
        gameWorld.addAnimal(cat);

            for (int x = 0; x < 2; x++) {
                final Thread gameLogic = new Thread(new GameLogicService(x * 100, 0, 100, 100, gameWorld, messageSystem));
                gameLogic.setName("GL" + x);
                gameLogic.setDaemon(true);
                gameLogic.start();
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
        final MessageSystem messageSystem = new MessageSystem();
        createAIService(messageSystem);

        GameWorld gameWorld = new GameWorld();

        SpawnerNPC spawner1 = new SpawnerNPC(gameWorld, WorldObject.Fraction.Dogs, 10, 0, 50, 10, 10000, messageSystem);
        SpawnerNPC spawner2 = new SpawnerNPC(gameWorld, WorldObject.Fraction.Cats, 290, 0, 50, 10, 10000, messageSystem);

        gameWorld.addWorldObject(spawner1);
        gameWorld.addWorldObject(spawner2);

        for (int x = 0; x < 2; x++) {
            final Thread gameLogic = new Thread(new GameLogicService(x * 100, 0, 300, 300, gameWorld, messageSystem));
            gameLogic.setName("GameLogicService "+x);
            gameLogic.setDaemon(true);
            gameLogic.start();
        }

        waitExit();
    }
}
