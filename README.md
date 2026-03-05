# ExtinguishingCampfires

<img width="1536" height="1024" alt="изображение" src="https://github.com/user-attachments/assets/426e0c38-0b70-4a2f-a9af-d9acde19c6fd" />

A small Minecraft (Spigot/Paper) plugin that makes campfires sometimes
go out when it rains.

## Features

-   Campfires can be extinguished by rain
-   Configurable chance
-   Works with rain and thunderstorms
-   Only campfires exposed to the sky are affected
-   Lightweight and simple

## How it works

When it is raining, the plugin checks campfires near players. If the
campfire is not covered by blocks and a random chance succeeds, the fire
will go out.

## Config example

chance: 0.15 
radius: 10 
check-interval-ticks: 100

## Installation

1.  Download the plugin jar
2.  Put it into your server plugins folder
3.  Restart the server

## Requirements

-   Spigot / Paper
-   Java 17+
