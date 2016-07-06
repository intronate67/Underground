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

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;

@Plugin(id="underground", name="Underground", version="1.0")
public class Underground {

    public static Underground underground = new Underground();

    public static Underground getUnderground(){
        return underground;
    }

    @Inject
    @DefaultConfig(sharedRoot = true)
    private File configuration = null;

    @Inject
    @DefaultConfig(sharedRoot = true)
    public ConfigurationLoader<CommentedConfigurationNode> configurationLoader = null;

    public CommentedConfigurationNode configurationNode = null;

    @Inject
    Game game;

    @Subscribe
    public void onPreInit(GamePreInitializationEvent e){
        try {
            if(!configuration.exists()){

                configuration.createNewFile();
                configurationNode = configurationLoader.load();
                configurationNode.getNode("rlgl", "arenas").setComment("Do not edit these values, All information stored about arenas is saved below.");
                configurationLoader.save(configurationNode);
            }
            configurationNode = configurationLoader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        game.getCommandManager().register(this, undergroundSpec, "underground");
    }


    CommandSpec addSpec = CommandSpec.builder()
            .arguments(GenericArguments.seq(
                        GenericArguments.string(Text.of("name")),
                        GenericArguments.string(Text.of("type")),
                        GenericArguments.integer(Text.of("regen-time"))),
                    GenericArguments.optional(GenericArguments.string(Text.of("world-name")))
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
                    GenericArguments.flags().flag("rt", "t", "q").buildWith(GenericArguments.seq(
                            GenericArguments.string(Text.of("name")),
                            GenericArguments.string(Text.of("value")),
                            GenericArguments.optional(GenericArguments.string(Text.of("operation")))
                    )),
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

    public CommentedConfigurationNode rootNode(){
        return configurationNode;
    }

    public Game getGame(){
        return game;
    }

}
