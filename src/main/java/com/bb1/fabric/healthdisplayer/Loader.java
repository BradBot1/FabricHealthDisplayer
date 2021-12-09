package com.bb1.fabric.healthdisplayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.bb1.fabric.bfapi.GameObjects;
import com.bb1.fabric.bfapi.permissions.PermissionUtils;
import com.bb1.fabric.bfapi.utils.ExceptionWrapper;
import com.bb1.fabric.bfapi.utils.Field;
import com.bb1.fabric.bfapi.utils.Inputs.Input;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

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
public class Loader implements ModInitializer {

	private static final Config CONFIG = new Config();

	public static final @NotNull Config getConfig() { return CONFIG; }
	
	private static final Map<UUID, Integer> DISPLAY_MAP = new HashMap<UUID, Integer>();
	
	private static final ExceptionWrapper<Input<ServerCommandSource>, ServerPlayerEntity> PLAYER_GETTER = i->i.get().getPlayer();
	
	public static final int getDisplayMode(@NotNull UUID user) {
		return (CONFIG.perPlayerOptions)?DISPLAY_MAP.getOrDefault(user, CONFIG.defaultMode):CONFIG.defaultMode;
	}
	
	public static final void updateDisplayMode(@NotNull UUID user, int mode) {
		DISPLAY_MAP.put(user, mode);
	}

	@Override
	public void onInitialize() {
		CONFIG.load();
		CONFIG.save();
		if (CONFIG.requiresPermission) {
			CONFIG.permission.register();
		}
		if (CONFIG.perPlayerOptions) {
			for (Entry<String, JsonElement> entry : CONFIG.playerPreferences.entrySet()) {
				DISPLAY_MAP.put(UUID.fromString(entry.getKey()), entry.getValue().getAsInt());
			}
		}
		GameObjects.GameEvents.COMMAND_REGISTRATION.addHandler((input)->{
			if (CONFIG.perPlayerOptions) { // If they enable it we need to add a command to allow players to change their display settings
				final CommandDispatcher<ServerCommandSource> dispatcher = input.get();
				for (JsonElement jsonObject : CONFIG.commandNames) {
					if (jsonObject.isJsonPrimitive()&&jsonObject.getAsJsonPrimitive().isString()) {
						dispatcher.register(CommandManager.literal(jsonObject.getAsString())
								.requires((s)->{
									return ((ExceptionWrapper.exectuteWithReturn(Input.of(s), PLAYER_GETTER)!=null))&&(CONFIG.requiresPermission?(PermissionUtils.hasPermission(Field.of(s.getEntity()), CONFIG.permission.node())):true);
								})
								.then(CommandManager.literal("none")
									.executes((s)->{
										ServerPlayerEntity player = ExceptionWrapper.exectuteWithReturn(Input.of(s.getSource()), PLAYER_GETTER);
										updateDisplayMode(player.getUuid(), 0);
										player.sendMessage(CONFIG.updatedText, false);
										return 1;
									})
								)
								.then(CommandManager.literal("hearts")
									.executes((s)->{
										ServerPlayerEntity player = ExceptionWrapper.exectuteWithReturn(Input.of(s.getSource()), PLAYER_GETTER);
										updateDisplayMode(player.getUuid(), 1);
										player.sendMessage(CONFIG.updatedText, false);
										return 1;
									})
								)
								.then(CommandManager.literal("percent")
									.executes((s)->{
										ServerPlayerEntity player = ExceptionWrapper.exectuteWithReturn(Input.of(s.getSource()), PLAYER_GETTER);
										updateDisplayMode(player.getUuid(), 2);
										player.sendMessage(CONFIG.updatedText, false);
										return 1;
									})
								)
								.then(CommandManager.literal("fraction")
									.executes((s)->{
										ServerPlayerEntity player = ExceptionWrapper.exectuteWithReturn(Input.of(s.getSource()), PLAYER_GETTER);
										updateDisplayMode(player.getUuid(), 3);
										player.sendMessage(CONFIG.updatedText, false);
										return 1;
									})
								)
						);
					}
				}
			}
		});
		GameObjects.GameEvents.SERVER_STOP.addHandler((server)->{
			NameDisplayer.DISPLAYS.forEach(e->e.kill());
			CONFIG.load();
			if (CONFIG.perPlayerOptions) {
				final JsonObject jsonObject = new JsonObject();
				for (Entry<UUID, Integer> entry : DISPLAY_MAP.entrySet()) {
					jsonObject.addProperty(entry.getKey().toString(), entry.getValue());
				}
				CONFIG.playerPreferences = jsonObject;
			} else {
				CONFIG.playerPreferences = new JsonObject();
			}
			CONFIG.save();
		});
	}

}
