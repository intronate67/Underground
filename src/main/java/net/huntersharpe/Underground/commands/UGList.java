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
package net.huntersharpe.Underground.commands;

import net.huntersharpe.Underground.util.Config;
import net.huntersharpe.Underground.util.WorldManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UGList implements CommandExecutor {

    private WorldManager worldManager = new WorldManager();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        //View list of all UGWorlds
        List<String> worlds = new ArrayList<>();
        Map<Object, ? extends CommentedConfigurationNode> map = Config.getConfig().get().getNode("worlds").getChildrenMap();
        if(!args.hasAny("name")){
            if(map.keySet().isEmpty()){
                src.sendMessage(Text.of(
                        TextColors.GRAY,
                        "--------[",
                        TextColors.BLUE,
                        "Underground",
                        TextColors.GRAY,
                        "]--------\n",
                        TextColors.BLUE,
                        "Empty"
                ));
                return CommandResult.success();
            }
            for(Object name : map.keySet().toArray()){
                worlds.add(name.toString());
            }
            src.sendMessage(Text.of(map.keySet().toString()));
            src.sendMessage(Text.of(
                    TextColors.GRAY,
                    "--------[",
                    TextColors.BLUE,
                    "Underground",
                    TextColors.GRAY,
                    "]--------\n",
                    TextColors.BLUE,
                    Text.of(map.keySet().toString())
            ));
            return CommandResult.success();
        }
        String name = args.<String>getOne("name").get();
        if(!map.keySet().contains(name)){
            src.sendMessage(Text.of(TextColors.RED, "Underground world: ", name, " does not exist in the config!"));
            return CommandResult.success();
        }else{
            String cw = Config.getConfig().get().getNode("worlds", name, "custom-world").getString();
            String maxSize = String.valueOf(Config.getConfig().get().getNode("worlds", name, "max-size").getValue());
            String regenTime = String.valueOf(Config.getConfig().get().getNode("worlds", name, "regen-time").getValue());
            String tgug = Config.getConfig().get().getNode("worlds", name, "tgug").getString();
            String type = Config.getConfig().get().getNode("worlds", name, "type").getString();
            src.sendMessage(Text.of(
                    TextColors.GRAY,
                    "--------[",
                    TextColors.BLUE,
                    "Underground",
                    TextColors.GRAY,
                    "]--------\n",
                    TextColors.BLUE,
                    "Name: ", name, "\n",
                    "Custom World: ", cw, "\n",
                    "Maximum Size: ", maxSize, "\n",
                    "Regen Time: ", regenTime, "\n",
                    "T.G.U.G: ", tgug, "\n",
                    "Type: ", type
            ));
        }
        return CommandResult.success();
    }
}