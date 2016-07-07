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

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class UGAdd implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(!args.<String>getOne(Text.of("type")).get().equals("original") && !args.<String>getOne(Text.of("type")).get().
                equals("custom")){
            src.sendMessage(Text.of(TextColors.RED, args.<String>getOne(Text.of("type")).get(), " is not a valid world type!"));
            return CommandResult.success();
        }
        //Custom world command
        String name = args.<String>getOne("name").get();
        Integer regenTime = args.<Integer>getOne("regen-time").get();
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
            WorldController.getWorldController().createWorld(name, regenTime, worldName);
            src.sendMessage(Text.of(TextColors.GREEN, "Underground world", name, " has been created!"));
            return CommandResult.success();
        }
        WorldController.getWorldController().createWorld(name, regenTime);
        src.sendMessage(Text.of(TextColors.GREEN, "Underground world", name, " has been created!"));
        return CommandResult.success();
    }
}
