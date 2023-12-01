package com.seailz.serverroulette.commands;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.command.annotation.SlashCommandInfo;
import com.seailz.discordjar.command.listeners.slash.SlashCommandListener;
import com.seailz.discordjar.events.model.interaction.command.SlashCommandInteractionEvent;
import com.seailz.discordjar.model.channel.GuildChannel;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.invite.Invite;
import com.seailz.discordjar.model.invite.internal.InviteImpl;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

@SlashCommandInfo(
        name = "newserver",
        description = "Creates a new playground"
)
public class CommandNewServer extends SlashCommandListener {
    @Override
    protected void onCommand(SlashCommandInteractionEvent command) {
        System.out.println("new server command");
        command.defer(false);
        Guild guild;
        try {
            guild = createGuild("Server Roulette", command.getBot());
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            e.printStackTrace();
            command.getHandler().followup("this is even more awkward").run();
            return;
        }

        System.out.println("created guild " + guild.id());

        GuildChannel ch = guild.createChannel("roulette", ChannelType.GUILD_TEXT).run().awaitCompleted();
        System.out.println("created channel " + ch.id());

        Invite inv;
        try {
            inv = createInvite(ch, command.getBot());
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            command.getHandler().followup("a stupid level of awkward").run();
            return;
        }

        System.out.println("created invite " + inv.code());

        command.getHandler().followup("bye bye time discord.gg/" + inv.code()).run();
    }

    private Guild createGuild(String name, DiscordJar djar) throws DiscordRequest.UnhandledDiscordAPIErrorException {
        DiscordRequest request = new DiscordRequest(
                new JSONObject().put("name", name),
                new HashMap<>(),
                "/guilds",
                djar,
                "/guilds",
                RequestMethod.POST
        );

        System.out.println("creating guild");
        JSONObject obj = request.invoke().body();
        System.out.println(obj);

        return Guild.decompile(obj, djar);
    }

    private Invite createInvite(GuildChannel gc, DiscordJar jar) throws DiscordRequest.UnhandledDiscordAPIErrorException {
        DiscordRequest request = new DiscordRequest(
                new JSONObject().put("max_age", 604800).put("max_uses", 0).put("temporary", false).put("unique", true),
                new HashMap<>(),
                "/channels/" + gc.id() + "/invites",
                jar,
                "/channels/" + "{channel.id}" +  "/invites",
                RequestMethod.POST
        );

        return InviteImpl.decompile(request.invoke().body(), jar);
    }
}
