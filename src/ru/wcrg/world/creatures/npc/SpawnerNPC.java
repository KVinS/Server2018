package ru.wcrg.world.creatures.npc;

import com.sun.istack.internal.NotNull;
import ru.wcrg.messaging.MessageSystem;
import ru.wcrg.utility.GTimer;
import ru.wcrg.world.GameWorld;
import ru.wcrg.world.InteractiveWorldObject;
import ru.wcrg.world.gameLogic.GameLogicService;

/**
 * Created by Эдуард on 10.12.2017.
 */
public class SpawnerNPC extends InteractiveWorldObject {
    GTimer respawnTimer;
    private int cooldown = 0;
    private int quantityNPC = 0;
    private int maxNPC = 0;


    public SpawnerNPC(@NotNull GameWorld gameWorld, Fraction fraction, int x, int y, int z, int cooldown, int maxNPC, MessageSystem messageSystem) {
        super("Bot Spawner " + x +" : "+y+" : "+z, gameWorld, fraction, x, y, z, messageSystem);

        this.cooldown = cooldown;
        this.maxNPC = maxNPC;

        respawnTimer = new GTimer(cooldown);
        messageSystem.addAbonent(this);
    }

    public void gameTick(GameLogicService gameLogic) {
        if (respawnTimer.isCompleted() && quantityNPC < maxNPC) {
            spawnNPC();
            respawnTimer.restart();
        }
        super.gameTick(gameLogic);
    }

    public void spawnNPC(){
        NPC npc = new NPC("Bot"+quantityNPC+ "("+getFraction()+")",x, y, z, gameWorld, this, getFraction(), getMessageSystem());
        quantityNPC++;
        gameWorld.addAnimal(npc);
    }

    public void OnNPCDied(NPC npc) {
        quantityNPC--;
    }
}
