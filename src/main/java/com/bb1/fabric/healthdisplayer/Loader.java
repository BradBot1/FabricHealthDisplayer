package com.bb1.fabric.healthdisplayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.events.Events;
import com.bb1.api.permissions.PermissionManager;
import com.google.gson.JsonElement;
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
		Events.GameEvents.COMMAND_REGISTRATION_EVENT.register((input)->{
			if (CONFIG.perPlayerOptions) { // If they enable it we need to add a command to allow players to change their display settings
				final CommandDispatcher<ServerCommandSource> dispatcher = input.get();
				for (JsonElement jsonObject : CONFIG.commandNames) {
					if (jsonObject.isJsonPrimitive()&&jsonObject.getAsJsonPrimitive().isString()) {
						dispatcher.register(CommandManager.literal(jsonObject.getAsString())
								.requires((s)->{
									return (com.bb1.api.Loader.getServerPlayerEntity(s)!=null)&&(CONFIG.requiresPermission?(s.hasPermissionLevel(CONFIG.opLevel)||(PermissionManager.getInstance().isUsable()&&PermissionManager.getInstance().hasPermission(s.getEntity().getUuid(), CONFIG.permission))):true);
								})
								.then(CommandManager.literal("none")
									.executes((s)->{
										ServerPlayerEntity player = com.bb1.api.Loader.getServerPlayerEntity(s.getSource());
										updateDisplayMode(player.getUuid(), 0);
										player.sendMessage(CONFIG.updatedText, false);
										return 1;
									})
								)
								.then(CommandManager.literal("hearts")
									.executes((s)->{
										ServerPlayerEntity player = com.bb1.api.Loader.getServerPlayerEntity(s.getSource());
										updateDisplayMode(player.getUuid(), 1);
										player.sendMessage(CONFIG.updatedText, false);
										return 1;
									})
								)
								.then(CommandManager.literal("percent")
									.executes((s)->{
										ServerPlayerEntity player = com.bb1.api.Loader.getServerPlayerEntity(s.getSource());
										updateDisplayMode(player.getUuid(), 2);
										player.sendMessage(CONFIG.updatedText, false);
										return 1;
									})
								)
								.then(CommandManager.literal("fraction")
									.executes((s)->{
										ServerPlayerEntity player = com.bb1.api.Loader.getServerPlayerEntity(s.getSource());
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
		Events.GameEvents.STOP_EVENT.register((server)->{
			NameDisplayer.DISPLAYS.forEach(e->e.kill());
		});
	}

}
