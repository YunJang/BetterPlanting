# BetterPlanting - 1.8.x

Current Version: 1.1.1

Written for our personal Minecraft server that is running Spigot 1.8.x. Spigot 1.8.1 API was used but it should work as long as Minecraft remains on version 1.8.

Features:
- Planting radius is based on the quality of the Hoe tool in your inventory.
	- Iron defaults to 3x3.
	- Gold defaults to 6x6.
	- Diamond defaults to 9x9.
- Plants in the tool's radius with the following crops:
	- Wheat
	- Carrot
	- Potato
	- Nether Wart

To-Do:
- Reduce durability of the hoes based on number of seeds planted.

Changelog:
- Version 1.1.1
    - Fixed the issue where it would fail to deduct the correct amount of seeds.
- Version 1.1.0
	- Updated with hoe tool check to determine planting radius.
		- If no hoe tool or Wooden Hoe is being used, normal planting will be done.
- Version 1.0.0
	- Initial Release
	- Planting with any of the listed seeds will plant in a 3x3 radius.
