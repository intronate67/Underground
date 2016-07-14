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

import com.google.inject.Inject;
import net.huntersharpe.Underground.commands.*;
import net.huntersharpe.Underground.util.Config;
import net.huntersharpe.Underground.util.WorldManager;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Plugin(id="underground", name="Underground", version="1.0")
public class Underground {

    private WorldManager worldManager = new WorldManager();

    protected Underground(){

    }
    private static Underground underground;
    public List<String> ugWorlds = new ArrayList<>();

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    @Inject
    private Logger logger;

    @Inject
    public Game game;

    public static Underground getUnderground(){
        return underground;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent e){
        underground = this;
        if(!Files.exists(configDir)){
            if (Files.exists(configDir.resolveSibling("underground"))){
                try{
                    Files.move(configDir.resolveSibling("underground"), configDir);
                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }else{
                try{
                    Files.createDirectories(configDir);
                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        }
        Config.getConfig().setup();
        worldManager.serializeConfig();
    }

    @Listener
    public void onInit(GameInitializationEvent e){
        game.getCommandManager().register(this, undergroundSpec, "underground");
    }

    @Listener
    public void onStop(GameStoppingEvent e){
        Config.getConfig().save();
    }

    CommandSpec addSpec = CommandSpec.builder()
            .arguments(GenericArguments.seq(
                        GenericArguments.string(Text.of("name")),
                        GenericArguments.string(Text.of("type")),
                        GenericArguments.integer(Text.of("regen-time")),
                        GenericArguments.optional(GenericArguments.string(Text.of("world-name")))
                )
            )
            .executor(new UGAdd())
            .build();
    CommandSpec removeSpec = CommandSpec.builder()
            .arguments(GenericArguments.string(Text.of("name")))
            .executor(new UGRemove())
            .build();
    CommandSpec editSpec = CommandSpec.builder()
            .arguments(GenericArguments.seq(
                    GenericArguments.string(Text.of("name")),
                    GenericArguments.string(Text.of("value")),
                    GenericArguments.string(Text.of("type")),
                    GenericArguments.optional(GenericArguments.string(Text.of("operation")))
            ))
            .executor(new UGEdit())
            .build();
    CommandSpec joinSpec = CommandSpec.builder()
            .arguments(GenericArguments.string(Text.of("name")))
            .executor(new UGJoin())
            .build();
    CommandSpec listSpec = CommandSpec.builder()
            .arguments(GenericArguments.optional(GenericArguments.integer(Text.of("page"))))
            .executor(new UGList())
            .build();
    CommandSpec undergroundSpec = CommandSpec.builder()
            .child(addSpec, "add")
            .child(removeSpec, "remove")
            .child(editSpec, "edit")
            .child(joinSpec, "join")
            .child(listSpec, "list")
            .executor(new UndergroundCommand())
            .build();

    public Path getConfigDir(){
        return configDir;
    }

    public Game getGame(){
        return game;
    }

    public Logger getLogger(){
        return logger;
    }

}
