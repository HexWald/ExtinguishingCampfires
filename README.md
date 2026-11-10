# ExtinguishingCampfires

Rain can put out campfires and soul campfires near players. A roof keeps them dry.

## Install

Use JDK 25 and Maven to build the plugin.

```sh
mvn clean package
```

Put `target/ExtinguishingCampfires-1.1.1.jar` in the server's `plugins` folder and restart. The plugin builds against the Spigot 26.3 API.

## Settings

Edit `plugins/ExtinguishingCampfires/config.yml` and restart the server.

```yaml
chance: 0.15
radius: 10
check-interval-ticks: 100
```

By default, the plugin checks every 100 ticks (about five seconds at 20 TPS). A lit campfire within 10 blocks of a player on each axis gets one 15% chance to go out in the rain.

`chance` accepts values from 0 to 1. Set it to 0 to turn the check off. The radius accepts 0 to 64 blocks, and the interval must be positive.
