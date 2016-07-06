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

import java.util.*;

public class WorldController {

    public static WorldController worldController = new WorldController();

    public static WorldController getWorldController(){
        return worldController;
    }

    public Map<UUID, Date> time = new HashMap<>();

    public void createWorld(String name, int regenTime){

    }

    public void createWorld(String name, int regenTime, String customWorld){

    }

    public void removeWorld(String name){

    }

    public void transition(String name){

    }

    public void setTime(UUID pUUID){
        //TODO: Below is not best way to compare, might change to in-game world time.
        time.put(pUUID, Calendar.getInstance().getTime());
    }

}
