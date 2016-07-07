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
package net.huntersharpe.Underground;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.WorldCreationSettings;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.*;

public class WorldController {

    private static List<UGWorld> worlds = new ArrayList<>();

    public static WorldController worldController = new WorldController();

    public static WorldController getWorldController(){
        return worldController;
    }

    private Map<UUID, Date> time = new HashMap<>();

    private CommentedConfigurationNode config = Underground.getUnderground().rootNode().getNode("worlds");

    public void test(CommandSource src){
        src.sendMessage(Text.of("Test"));
    }

    public void createWorld(String name, int regenTime){
        WorldCreationSettings.Builder builder = WorldCreationSettings.builder().name(name);
        WorldCreationSettings settings = builder.enabled(true).keepsSpawnLoaded(true).loadsOnStartup(true).build();
        final Optional<WorldProperties> optionalProperties = Sponge.getGame().getServer().createWorldProperties(settings);
        WorldProperties properties = optionalProperties.get();
        Sponge.getServer().saveWorldProperties(properties);
        Sponge.getServer().loadWorld(name);
        UGWorld ug = new UGWorld(name, UUID.randomUUID(), regenTime, 50, "1d");
        worlds.add(ug);
        config.getNode(name, "type").setValue("original");
        config.getNode(name, "custom-world").setValue(null);
        config.getNode(name, "regen-time").setValue(regenTime);
        config.getNode(name, "tgug").setValue("1d");
    }

    public void createWorld(String name, int regenTime, String customWorld){
        //TODO: Create custom underground worlds.
        WorldCreationSettings.Builder builder = WorldCreationSettings.builder().name(name);
        WorldCreationSettings settings = builder.enabled(true).keepsSpawnLoaded(true).loadsOnStartup(true).build();
        final Optional<WorldProperties> optionalProperties = Sponge.getGame().getServer().createWorldProperties(settings);
        WorldProperties properties = optionalProperties.get();
        Sponge.getServer().saveWorldProperties(properties);
        Sponge.getServer().loadWorld(name);
        UGWorld ug = new UGWorld(name, UUID.randomUUID(), regenTime, 50, "1d");
        worlds.add(ug);
        config.getNode(name, "type").setValue("custom");
        config.getNode(name, "custom-world").setValue(customWorld);
        config.getNode(name, "regen-time").setValue(regenTime);
        config.getNode(name, "tgug").setValue("1d");
    }

    public void removeWorld(String name){
        Sponge.getServer().deleteWorld(Sponge.getServer().getWorldProperties(name).get());
        config.removeChild(name);
    }

    public void transition(String name){

    }

    public void setTime(UUID pUUID){
        //TODO: Below is not best way to compare, might change to in-game world time.
        time.put(pUUID, Calendar.getInstance().getTime());
    }

    public UGWorld getUGWold(UUID id){
        for(UGWorld ug : this.worlds){
            if(ug.getId().equals(ug)){
                return ug;
            }
        }
        return null;
    }

}
