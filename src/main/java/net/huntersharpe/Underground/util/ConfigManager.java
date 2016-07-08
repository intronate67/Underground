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

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private File configuration;
    private ConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    private CommentedConfigurationNode configurationNode;

    public ConfigManager() {
        String folder = "config" + File.separator + "underground";
        if(!new File(folder).isDirectory()) {
            new File(folder).mkdirs();
        }
        configuration = new File(folder, "config.conf");
        create();
        load();
    }
    public void main(){
        save();
    }

    public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
        return configurationLoader;
    }

    public CommentedConfigurationNode getConfig() {
        return configurationNode;
    }

    private void create(){
        if(!configuration.exists()) {
            try {
                configuration.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void load(){
        configurationLoader = HoconConfigurationLoader.builder().setFile(configuration).build();
        try {
            configurationNode = configurationLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(){
        try {
            configurationLoader.save(configurationNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
