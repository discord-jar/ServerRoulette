package com.seailz.serverroulette;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.DiscordJarBuilder;
import com.seailz.serverroulette.commands.CommandNewServer;
import com.seailz.serverroulette.commands.CommandRoulette;

public class ServerRoulette {
    public static void main(String[] args) {
        new ServerRoulette();
    }

    public ServerRoulette() {
        DiscordJar jar = new DiscordJarBuilder(System.getenv("TOKEN"))
                .defaultIntents().defaultCacheTypes().setDebug(true).build();

        jar.registerCommands(new CommandRoulette(), new CommandNewServer());
    }
}