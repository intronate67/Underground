/*
 * This file is part of EssentialCmds, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015 - 2015 HassanS6000
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.huntersharpe.Underground.util;

import net.huntersharpe.Underground.Underground;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config implements Configurable{

    private static Config config = new Config();

    private Config()
    {
        ;
    }

    public static Config getConfig()
    {
        return config;
    }

    private Path configFile = Paths.get(Underground.getUnderground().getConfigDir() + "/config.conf");
    private ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(configFile).build();
    private CommentedConfigurationNode configNode;

    @Override
    public void setup()
    {
        if (!Files.exists(configFile))
        {
            try
            {
                Files.createFile(configFile);
                load();
                save();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            load();
        }
    }

    @Override
    public void load()
    {
        try
        {
            configNode = configLoader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void save()
    {
        try
        {
            configLoader.save(configNode);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public CommentedConfigurationNode get()
    {
        return configNode;
    }
}
