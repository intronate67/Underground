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

import net.huntersharpe.Underground.UGWorld;
import net.huntersharpe.Underground.util.ConfigManager;
import net.huntersharpe.Underground.util.WorldManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class UGEdit implements CommandExecutor {

    //TODO: Fix configuration overriding.

    private ConfigManager config = new ConfigManager();

    private WorldManager worldManager = new WorldManager();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne("name").get();
        if(!config.getConfig().getNode(name).isVirtual()){
            src.sendMessage(Text.of(TextColors.RED, name, " is not a valid UGWorld and/or does not exist in the config."));
            return CommandResult.success();
        }
        String value = args.<String>getOne("value").get();
        //Change regen-time or max size
        UGWorld world = worldManager.getUGWold(name);
        String type = args.<String>getOne("type").get();
        //TODO: Update to switch and case.
        if(type.equalsIgnoreCase("regen") || type.equalsIgnoreCase("size")){
            if(!isInteger(value)){
                src.sendMessage(Text.of(TextColors.RED, "If editing regen time or max size value must be an integer!"));
                return CommandResult.success();
            }
            int n = Integer.parseInt(value);
            if(args.hasAny("operation")){
                src.sendMessage(Text.of(TextColors.RED, "Add/Remove operations can only be done with quests!"));
                return CommandResult.success();
            }
            switch(type){
                case "regen" :
                    src.sendMessage(Text.of(TextColors.GRAY, "Updated regen time successfully!"));
                    world.setRegenTime(n);
                    config.getConfig().getNode(name + "regen-time").setValue(n);
                    config.save();
                    break;
                case "size":
                    src.sendMessage(Text.of(TextColors.GRAY, "Updated maximum size of world time successfully!"));
                    world.setMaxSize(n);
                    config.getConfig().getNode(name + "max-size").setValue(n);
                    config.save();
                    break;
            }
            return CommandResult.success();
        }else if(type.equalsIgnoreCase("tgug") || type.equalsIgnoreCase("quest")){
            switch(type){
                case "tgug":
                    if(!value.contains("d")){
                        src.sendMessage(Text.of(TextColors.RED, "Value must contain the letter 'd'"));
                        break;
                    }
                    world.setTgug(value);
                    src.sendMessage(Text.of(TextColors.GRAY, "Updated the time to get underground successfully!"));
                    config.getConfig().getNode(name, "tgug").setValue(value);
                    config.save();
                    break;
                case "quest":
                    //TODO: Edit config and quest book.
                    break;
            }
        }
        return CommandResult.success();
}

    private boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}