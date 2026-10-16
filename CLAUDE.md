# CLAUDE.md

## Project
Minecraft paper plugin written in spigot-api.

## Architecture
- Language : Java 8 (openjdk-8-jdk)

## Structure
- / command - Custom command for spigot-api
- / network - External API callers
- / listener - Event Listeners from paper system
- / utils - Code utils
- / world - World Manager
- / translation - Message translator
- / game - Game logics

## Builds
- Build : `./gradlew build`
- lint : `./gradlew spotlessCheck`
- lint-auto-fix : `gradlew spotlessApply`

## Coding conventions
- No wildcard imports
- All codes should be under spigot-api
- No NMS class use (net.minecraft.server)
- No CraftBukkit class use
- Do not add comments logic is non-obvious
- Use PacketEvents for packet modification
- Use PacketEvents from (kr.teamcocoa.core.bukkit.packetevents)

## Important rules

[//]: # (IMPORTANT : )