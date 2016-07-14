/*This file is part of Underground, licensed under the MIT License (MIT).
*
* Copyright (c) 2016 Hunter Sharpe
* Copyright (c) contributors

* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
package net.huntersharpe.Underground.util;

import net.huntersharpe.Underground.UGWorld;
import net.huntersharpe.Underground.Underground;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.property.block.SkyLuminanceProperty;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlayerManager {

    private static PlayerManager playerManager;

    public PlayerManager(){
        playerManager = this;
    }

    public static PlayerManager getPlayerManager(){
        return playerManager;
    }

    public Map<UUID, String> players = new HashMap<>();

    public Map<UUID, Long> times = new HashMap<>();

    public List<UUID> underground = new ArrayList<>();

    private final long[] timer = {0};

    public void joinWorld(Player player, String name){
        //TODO: Give quest book.
        UGWorld ugWorld = WorldManager.getWorldManager().getUGWold(name).get();
        long time = Sponge.getServer().getWorld(name).get().getProperties().getTotalTime();
        player.setLocation(ugWorld.getSpawn());
        player.sendMessage(Text.of(TextColors.GREEN, "You have successfully joined underground world: ", name));
        Config.getConfig().get().getNode("players", player.getName(), "ugworld").setValue(name);
        Config.getConfig().get().getNode("players", player.getName(), "time-joined").setValue(time);
        Config.getConfig().get().getNode("players", player.getName(), "scheduler-time").setValue(0);
        String tgug = Config.getConfig().get().getNode("worlds", name, "tgug").getString().replace("d", "");
        //Add player to array list to handle timing.
        times.put(player.getUniqueId(), time);
    }

    @Listener
    public void onTimeChange(DisplaceEntityEvent e){
        if(!(e.getTargetEntity() instanceof Player)) return;
        Player player = (Player)e.getTargetEntity();
        if(!players.containsKey(player.getUniqueId())
                || !times.containsKey(player.getUniqueId())
                || !WorldManager.getWorldManager().getUGWold(e.getFromTransform().getExtent().getName()).isPresent()) return;
        long timeJoined =   Config.getConfig().get().getNode("players", player.getName(), "time-joined").getLong();
        String regenString = Config.getConfig().get().getNode("worlds", Config.getConfig().get().getNode("players", player.getName(), "ugworld"), "tgug").getString();
        long tgugTime = 24000 * Long.parseLong(regenString.replace("d", ""));
        World world = e.getToTransform().getExtent();
        if(world.getProperties().getTotalTime() >= (timeJoined + tgugTime)){
            //Make sure they're underground
            if(player.getLocation().getBlock().getProperty(SkyLuminanceProperty.class).isPresent()){
                //Possibly way around this.
                startScheduler(player);
            }else{
                underground.add(player.getUniqueId());
            }
        }else{
            return;
        }
    }
    volatile boolean shutdown = false;
    public void startScheduler(Player player){
        Scheduler scheduler = Sponge.getScheduler();
        Task.Builder taskBuilder = scheduler.createTaskBuilder().async();
        taskBuilder.execute(() -> {
            if(player.get(Keys.HEALTH).get().equals(0)){
                while(!shutdown){
                    player.sendMessage(Text.of());
                }
            }
            player.sendMessage(Text.of(TextColors.RED, "You have ", player.get(Keys.HEALTH).get() / 2, " minutes to get underground!"));
            player.offer(Keys.HEALTH, player.get(Keys.HEALTH).get() - 1);
        }).interval(1, TimeUnit.MINUTES).submit(Underground.getUnderground());
    }

}
