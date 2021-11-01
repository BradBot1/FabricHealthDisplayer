# FabricHealthDisplay

## Required Mods

> This mod is dependent on [BFAPI](https://github.com/BradBot1/BradsFabricApi)

## Commands

Commands are configurable, from the config you can change their name, permission value and op level required to run them

## Config

The config is found under `fabrichealthdisplayer.json` in the config directory

|Field|Type|Description|Default|
|-----|----|-----------|-------|
|commandAliases|Array|A list of names that should be used as command names|["fabrichealthdisplayer", "fhd", "healthdisplayer", "hd"]|
|commandRequiresPermission|Boolean|If commands should require a permission or op level|false|
|commandPermission|String|If `commandRequiresPermission` is enabled its the permission that should be checked for|fabrichealthdisplayer.use|
|commandOpLevel|Integer|If `commandRequiresPermission` is enabled its the op level that should be checked for|1|
|commandSuccessText|Text|The text to display a user when they change their command level|[✓] Updated your display settings!|
|allowPlayersToChangePersonalSettings|Boolean|If individual players can modify their display settings|true|
|playerPreferences|JsonObject|If `allowPlayersToChangePersonalSettings` is enabled it is where data is stored on player preferences|N/A|
|displayTime|Integer|The time in ms to display the health for|3000|
|defaultDisplayMode|Integer|The default display mode to show the user|1|

## Display modes

0 -> none (Nothing is diplayes to this user)
1 -> hearts (The amount of health in hearts is shown)
2 -> percent (The health is shown as a percentage of full health)
3 -> fraction (The heal if shown as healh/maxHealth)
## Links

* [GitHub](https://github.com/BradBot1/FabricHealthDisplayer)
* [Modrinth](https://modrinth.com/mod/healthdisplay)
* [CurseForge](https://www.curseforge.com/minecraft/mc-mods/fabrichealthdisplay)