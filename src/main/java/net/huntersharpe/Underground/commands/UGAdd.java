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

import net.huntersharpe.Underground.util.WorldManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.extent.Extent;

public class UGAdd implements CommandExecutor {

    //TODO: Figure out player friendly way to write regen-time.

    private WorldManager worldManager = new WorldManager();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        //TODO: Override sponge missed argument handling.
        if(!args.hasAny("name") || !args.hasAny("type")){
            src.sendMessage(Text.of(TextColors.RED, "Usage: /ug add <name> <type> <regen-time> [world-name]"));
            return CommandResult.success();
        }
        if(!args.<String>getOne(Text.of("type")).get().equals("original") && !args.<String>getOne(Text.of("type")).get().
                equals("custom")){
            src.sendMessage(Text.of(TextColors.RED, args.<String>getOne(Text.of("type")).get(), " is not a valid world type!"));
            return CommandResult.success();
        }
        //Custom world command
        String name = args.<String>getOne("name").get();
        int regenTime = args.<Integer>getOne("regen-time").get();
        if(Sponge.getServer().getWorlds().contains(name)){
            src.sendMessage(Text.of(TextColors.RED, name, " is an already existing world!"));
            return CommandResult.success();
        }
        if(args.hasAny(Text.of("world-name"))){
            if(!args.<String>getOne(Text.of("type")).get().equals("custom")){
                src.sendMessage(Text.of(TextColors.RED, "Original Worlds cannot be created from custom worlds, please " +
                        "specify a custom type."));
                return CommandResult.success();
            }
            //Type custom
            String worldName = args.<String>getOne("world-name").get();
            try{
                worldManager.createWorld(name, regenTime, worldName);
            }catch(Exception e){
                e.printStackTrace();
                src.sendMessage(Text.of(TextColors.RED, "Could not create underground world successfully."));
            }
            src.sendMessage(Text.of(TextColors.GREEN, "Underground world", name, " has been created!"));
            return CommandResult.success();
        }
        src.sendMessage(Text.of(TextColors.GRAY, "Creating world ", name, "..."));
        try {
            worldManager.createWorld(name, regenTime);
            src.sendMessage(Text.of(TextColors.GREEN, "Underground world ", name, " has been created!"));
        }catch (Exception e){
            e.printStackTrace();
            src.sendMessage(Text.of(TextColors.RED, "Could not create underground world successfully."));
        }
        return CommandResult.success();
    }
}
