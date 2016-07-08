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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UGWorld {

    private String name;
    private int regenTime;
    private int maxSize;
    private String tgug;
    private  List<UUID> players = new ArrayList<>();

    public UGWorld(String name, int regenTime, int maxSize, String tgug){
        this.name = name;
        this.regenTime = regenTime;
        this.maxSize = maxSize;
        this.tgug = tgug;
    }

    public String getName(){
        return this.name;
    }

    public int getRegenTime(){
        return this.regenTime;
    }

    public int getMaxSize(){
        return this.maxSize;
    }

    public String getTgug(){
        return this.tgug;
    }

    public List<UUID> getPlayers(){
        return this.players;
    }

    public void setRegenTime(int regenTime){
        this.regenTime = regenTime;
    }

    public void setMaxSize(int maxSize){
        this.maxSize = maxSize;
    }

    public void setTgug(String tgug){
        this.tgug = tgug;
    }
}
