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

import com.flowpowered.math.vector.Vector3i;
import net.huntersharpe.Underground.UGWorld;
import net.huntersharpe.Underground.Underground;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.WorldCreationSettings;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.*;

public class WorldManager {

    //TODO: Create worlds asynchronously

    private static WorldManager worldManager = new WorldManager();

    public WorldManager(){
        worldManager = this;
    }

    public static WorldManager getWorldManager(){
        return worldManager;
    }

    public static List<UGWorld> worlds = new ArrayList<>();

    public void createWorld(String name, int regenTime){
        WorldCreationSettings.Builder builder = WorldCreationSettings.builder().name(name);
        WorldCreationSettings settings = builder.generateSpawnOnLoad(false).enabled(true).keepsSpawnLoaded(true).loadsOnStartup(true).build();
        final Optional<WorldProperties> optionalProperties = Sponge.getGame().getServer().createWorldProperties(settings);
        WorldProperties properties = optionalProperties.get();
        Sponge.getServer().saveWorldProperties(properties);
        Sponge.getServer().loadWorld(name);
        Sponge.getServer().getWorld(name).get().getProperties().setSpawnPosition(new Vector3i(0, 0, 0));
        Location<Extent> loc = new Location<>(Sponge.getServer().getWorld(name).get().getRelativeExtentView(), 0, 0, 0);
        UGWorld ug = new UGWorld(name, regenTime, 50, "1d", loc);
        worlds.add(ug);
        Config.getConfig().get().getNode("worlds", name, "type").setValue("original");
        Config.getConfig().get().getNode("worlds", name, "custom-world").setValue(false);
        Config.getConfig().get().getNode("worlds", name, "regen-time").setValue(regenTime);
        Config.getConfig().get().getNode("worlds", name, "tgug").setValue("1d");
        Config.getConfig().get().getNode("worlds", name, "max-size").setValue(100);
        Config.getConfig().get().getNode("worlds", name, "spawn-x").setValue(0);
        Config.getConfig().get().getNode("worlds", name, "spawn-y").setValue(0);
        Config.getConfig().get().getNode("worlds", name, "spawn-z").setValue(0);
        Config.getConfig().save();
    }

    public void createWorld(String name, int regenTime, String customWorld){
        //TODO: Create custom underground worlds.
        WorldCreationSettings.Builder builder = WorldCreationSettings.builder().name(name);
        WorldCreationSettings settings = builder.generateSpawnOnLoad(false).enabled(true).keepsSpawnLoaded(true).loadsOnStartup(true).build();
        final Optional<WorldProperties> optionalProperties = Sponge.getGame().getServer().createWorldProperties(settings);
        WorldProperties properties = optionalProperties.get();
        Sponge.getServer().saveWorldProperties(properties);
        Sponge.getServer().loadWorld(name);
        Sponge.getServer().getWorld(name).get().getProperties().setSpawnPosition(new Vector3i(0, 0, 0));
        Location<Extent> loc = new Location<>(Sponge.getServer().getWorld(name).get().getRelativeExtentView(), 0, 0, 0);
        UGWorld ug = new UGWorld(name, regenTime, 50, "1d", loc);
        worlds.add(ug);
        Config.getConfig().get().getNode("worlds", name, "type").setValue("custom");
        Config.getConfig().get().getNode("worlds", name, "custom-world").setValue(customWorld);
        Config.getConfig().get().getNode("worlds", name, "regen-time").setValue(regenTime);
        Config.getConfig().get().getNode("worlds", name, "tgug").setValue("1d");
        Config.getConfig().get().getNode("worlds", name, "max-size").setValue(100);
        Config.getConfig().get().getNode("worlds", name, "spawn-x").setValue(0);
        Config.getConfig().get().getNode("worlds", name, "spawn-y").setValue(0);
        Config.getConfig().get().getNode("worlds", name, "spawn-z").setValue(0);
        Config.getConfig().save();
    }

    public void removeWorld(String name){
        Sponge.getServer().deleteWorld(Sponge.getServer().getWorldProperties(name).get());
        Config.getConfig().get().getNode("worlds").removeChild(name);
        Config.getConfig().save();
    }

    public void transition(String name){

    }

    public Optional<UGWorld> getUGWold(String name){
        for(UGWorld ug : worlds) {
            if (ug.getName().equals(name)) {
                return Optional.of(ug);
            }
        }
        return null;
    }
    public void serializeConfig(){
        //Loading worlds
        List<String> names = new ArrayList<>();
        Map<Object, ? extends CommentedConfigurationNode> map = Config.getConfig().get().getNode("worlds").getChildrenMap();
        if(map.keySet().isEmpty()){
            Underground.getUnderground().getLogger().info("No underground worlds were found inside the config!");
            return;
        }else{
            for(String name : map.keySet().toArray(new String[map.keySet().size()])){
                int regenTime = Config.getConfig().get().getNode("worlds", name, "regen-time").getInt();
                int maxSize = Config.getConfig().get().getNode("worlds", name, "max-size").getInt();
                String tgug = Config.getConfig().get().getNode("worlds", name, "tgug").getString();
                Location<org.spongepowered.api.world.extent.Extent> spawn = new Location<>(
                        Underground.getUnderground().getGame().getServer().getWorld(name).get().getRelativeExtentView(),
                        Config.getConfig().get().getNode("worlds", name, "spawn-x").getDouble(),
                        Config.getConfig().get().getNode("worlds", name, "spawn-y").getDouble(),
                        Config.getConfig().get().getNode("worlds", name, "spawn-z").getDouble()
                );
                UGWorld ug = new UGWorld(name, regenTime, maxSize, tgug, spawn);
                worlds.add(ug);
                names.add(name);
            }
            Underground.getUnderground().getLogger().info("Underground worlds: " + names.toString() + " we found in the config!");
            //Loading Players
            Map<Object, ? extends CommentedConfigurationNode> pMap = Config.getConfig().get().getNode("players").getChildrenMap();
            if(pMap.keySet().isEmpty()){
                Underground.getUnderground().getLogger().info("No player data found in config!");
                return;
            }else{
                for(String name : pMap.keySet().toArray(new String[pMap.keySet().size()])){
                    getUGWold(Config.getConfig().get().getNode("players", name, "ugworld").toString()).get().getPlayers().add(Sponge.getServer().getPlayer(name).get().getUniqueId());
                    PlayerManager.getPlayerManager().players.put(Sponge.getServer().getPlayer(name).get().getUniqueId(), Config.getConfig().get().getNode("players", name, "ugworld").toString());
                }
                Underground.getUnderground().getLogger().info("Loaded players into maps.");
            }
        }
    }
}
