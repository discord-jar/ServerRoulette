package com.seailz.serverroulette.commands;

import com.seailz.discordjar.command.annotation.SlashCommandInfo;
import com.seailz.discordjar.command.listeners.slash.SlashCommandListener;
import com.seailz.discordjar.events.model.interaction.command.SlashCommandInteractionEvent;

import java.util.Random;

@SlashCommandInfo(
        name = "roulette",
        description = "It's server death time. (maybe)"
)
public class CommandRoulette extends SlashCommandListener {
    @Override
    protected void onCommand(SlashCommandInteractionEvent command) {
        Random random = new Random();
        int num = random.nextInt(6);
        if (num == 0) {
            command.reply("say bye bye to your server").run();
        } else {
            command.reply("You have been spared.").run();
            return;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            command.getGuild().delete();
        } catch (IllegalAccessException e) {
            command.getHandler().followup("this is awkward (set me to owner >:3)").run();
        }
    }
}
