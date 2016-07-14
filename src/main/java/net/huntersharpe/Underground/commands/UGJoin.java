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
import net.huntersharpe.Underground.util.PlayerManager;
import net.huntersharpe.Underground.util.WorldManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class UGJoin implements CommandExecutor {

    //TODO: Scheduler

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(!(src instanceof Player)){
            src.sendMessage(Text.of(TextColors.RED, "Only players can join underground worlds!"));
            return CommandResult.success();
        }
        Player player = (Player)src;
        String name = args.<String>getOne("name").get();
        if(!WorldManager.worlds.contains(name) || WorldManager.worlds.isEmpty()){
            src.sendMessage(Text.of(TextColors.RED, "No underground world by that name exists!"));
            return CommandResult.success();
        }
        if(WorldManager.getWorldManager().getUGWold(name).get().getPlayers().contains(player.getUniqueId())
                || Config.getConfig().get().getNode("players", player.getName(), "ugworld").getValue().equals(name)){
            player.sendMessage(Text.of(TextColors.RED, "You already are in that world!"));
            return CommandResult.success();
        }
        try{
            PlayerManager.getPlayerManager().players.put(player.getUniqueId(), name);
            PlayerManager.getPlayerManager().joinWorld(player, name);
        }catch(Exception e){
            player.sendMessage(Text.of(TextColors.RED, "Error while joining world, contact an administrator."));
        }
        return CommandResult.success();
    }
}
