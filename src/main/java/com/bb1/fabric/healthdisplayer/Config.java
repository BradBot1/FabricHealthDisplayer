package com.bb1.fabric.healthdisplayer;

import com.bb1.api.config.Storable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Config extends com.bb1.api.config.Config {

	public Config() { super("fabrichealthdisplayer"); }
	/**
	 * 0 - off
	 * 1 - hearts
	 * 2 - percent
	 * 3 - fraction
	 */
	@Storable(key = "defaultDisplayMode") public int defaultMode = 1;
	
	@Storable(key = "allowPlayersToChangePersonalSettings") public boolean perPlayerOptions = true; // If players can change their personal display settings
	
	@Storable public long displayTime = 3000;
	
	// command stuff
	
	@Storable(key = "commandAliases") public JsonArray commandNames = new JsonParser().parse("[\"fabrichealthdisplayer\", \"fhd\", \"healthdisplayer\", \"hd\"]").getAsJsonArray();
	
	@Storable(key = "commandRequiresPermission") public boolean requiresPermission = false;
	
	@Storable(key = "commandPermission") public String permission = "fabrichealthdisplayer.use";
	
	@Storable(key = "commandOpLevel") public int opLevel = 1;
	
	@Storable(key = "commandSuccessText") public Text updatedText = new LiteralText("[✓]").formatted(Formatting.GREEN).append(new LiteralText(" Updated your display settings!").formatted(Formatting.WHITE));
	
	@Storable public JsonObject playerPreferences = new JsonObject();

}
