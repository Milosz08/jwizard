/*
 * Copyright (c) 2022 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 * File name: AudioSkippedCommandExecutor.java
 * Last modified: 15/07/2022, 02:30
 * Project name: franek-bot
 *
 * Licensed under the MIT license; you may not use this file except in compliance with the License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * THE ABOVE COPYRIGHT NOTICE AND THIS PERMISSION NOTICE SHALL BE INCLUDED IN ALL
 * COPIES OR SUBSTANTIAL PORTIONS OF THE SOFTWARE.
 */

package pl.miloszgilga.executors.audioplayer;

import net.dv8tion.jda.api.entities.Member;
import com.jagrosh.jdautilities.command.Command;
import net.dv8tion.jda.api.entities.VoiceChannel;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.*;
import jdk.jfr.Description;

import pl.miloszgilga.audioplayer.MusicManager;
import pl.miloszgilga.audioplayer.PlayerManager;
import pl.miloszgilga.exceptions.EmptyAudioQueueException;
import pl.miloszgilga.exceptions.UserOnVoiceChannelNotFoundException;
import pl.miloszgilga.exceptions.AttemptToRevoteSkippingSongException;
import pl.miloszgilga.executors.executorhandlers.VoteCommandExecutingHandler;

import static pl.miloszgilga.Command.MUSIC_VOTE_SKIP;


public class VoteSkipTrackCommandExecutor extends Command {

    private final PlayerManager playerManager = PlayerManager.getSingletonInstance();

    public VoteSkipTrackCommandExecutor() {
        name = MUSIC_VOTE_SKIP.getCommandName();
        help = MUSIC_VOTE_SKIP.getCommandDescription();
    }

    @Override
    @Description("command: <[prefix]voteskip>")
    protected void execute(CommandEvent event) {
        try {
            final MusicManager musicManager = playerManager.getMusicManager(event);
            final VoiceChannel voiceChannelWithBot = findVoiceChannelWithBotAndUser(event);
            if (musicManager.getAudioPlayer().getPlayingTrack() == null) {
                throw new EmptyAudioQueueException(event);
            }

            final var voteHandler = new VoteCommandExecutingHandler(event, voiceChannelWithBot,
                    "piosenka pominięta", "pominięcie piosenki", "piosenka niepominięta");
            if (voteHandler.voteCommandExecutor()) {
                if (musicManager.getScheduler().getQueue().isEmpty()) {
                    musicManager.getAudioPlayer().stopTrack();
                } else {
                    musicManager.getScheduler().nextTrack();
                }
            }
        } catch (EmptyAudioQueueException | UserOnVoiceChannelNotFoundException |
                 AttemptToRevoteSkippingSongException ex) {
            System.out.println(ex.getMessage());
        }
    }

    static VoiceChannel findVoiceChannelWithBotAndUser(CommandEvent event) {
        final String guildId = event.getGuild().getId();
        return Objects.requireNonNull(event.getJDA().getGuildById(guildId))
                .getVoiceChannels().stream()
                .filter(channel -> {
                    final Member senderUserMember = event.getGuild().getMember(event.getAuthor());
                    final Member botMember = event.getGuild().getMember(event.getJDA().getSelfUser());
                    return channel.getMembers().contains(senderUserMember) && channel.getMembers().contains(botMember);
                })
                .findFirst().orElseThrow(() -> {
                    throw new UserOnVoiceChannelNotFoundException(event, "Aby mieć możliwość użycia komendy, " +
                            "musisz przebywać na kanale głosowym wraz z botem.");
                });
    }
}