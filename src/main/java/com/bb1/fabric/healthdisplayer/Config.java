package com.bb1.fabric.healthdisplayer;

import com.bb1.fabric.bfapi.Constants;
import com.bb1.fabric.bfapi.config.ConfigName;
import com.bb1.fabric.bfapi.config.ConfigSub;
import com.bb1.fabric.bfapi.permissions.Permission;
import com.bb1.fabric.bfapi.permissions.PermissionLevel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

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
public class Config extends com.bb1.fabric.bfapi.config.Config {

	public Config() { super(new Identifier(Constants.ID, "fabrichealthdisplayer")); }
	/**
	 * 0 - off
	 * 1 - hearts
	 * 2 - percent
	 * 3 - fraction
	 */
	@ConfigName(name = "defaultDisplayMode")
	public int defaultMode = 1;
	
	@ConfigName(name = "allowPlayersToChangePersonalSettings")
	public boolean perPlayerOptions = true; // If players can change their personal display settings
	
	public long displayTime = 3000;
	
	// command stuff
	
	@ConfigName(name = "commandAliases")
	@ConfigSub(subOf = "command")
	public JsonArray commandNames = JsonParser.parseString("[\"fabrichealthdisplayer\", \"fhd\", \"healthdisplayer\", \"hd\"]").getAsJsonArray();
	
	@ConfigName(name = "commandRequiresPermission")
	@ConfigSub(subOf = "command.permission")
	public boolean requiresPermission = false;
	
	@ConfigName(name = "commandPermission")
	@ConfigSub(subOf = "command.permission")
	public Permission permission = new Permission("fabrichealthdisplayer.use", PermissionLevel.OP_1);
	
	@ConfigName(name = "commandSuccessText")
	@ConfigSub(subOf = "command")
	public Text updatedText = new LiteralText("[✓]").formatted(Formatting.GREEN).append(new LiteralText(" Updated your display settings!").formatted(Formatting.WHITE));
	
	public JsonObject playerPreferences = new JsonObject();

}
