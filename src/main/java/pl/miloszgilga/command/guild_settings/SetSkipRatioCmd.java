/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: SetSkipRatioCmd.java
 * Last modified: 16/05/2023, 10:09
 * Project name: jwizard-discord-bot
 *
 * Licensed under the MIT license; you may not use this file except in compliance with the License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * THE ABOVE COPYRIGHT NOTICE AND THIS PERMISSION NOTICE SHALL BE INCLUDED IN ALL COPIES OR
 * SUBSTANTIAL PORTIONS OF THE SOFTWARE.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited
 * to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no event
 * shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an
 * action of contract, tort or otherwise, arising from, out of or in connection with the software or the use
 * or other dealings in the software.
 */

package pl.miloszgilga.command.guild_settings;

import lombok.extern.slf4j.Slf4j;

import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Map;
import java.util.Objects;

import pl.miloszgilga.BotCommand;
import pl.miloszgilga.BotCommandArgument;
import pl.miloszgilga.misc.JDALog;
import pl.miloszgilga.locale.ResLocaleSet;
import pl.miloszgilga.core.configuration.BotProperty;
import pl.miloszgilga.dto.CommandEventWrapper;
import pl.miloszgilga.embed.EmbedMessageBuilder;
import pl.miloszgilga.cacheable.CacheableGuildSettingsDao;
import pl.miloszgilga.command.AbstractGuildSettingsCommand;
import pl.miloszgilga.core.remote.RemotePropertyHandler;
import pl.miloszgilga.core.configuration.BotConfiguration;
import pl.miloszgilga.core.loader.JDAInjectableCommandLazyService;

import pl.miloszgilga.domain.guild_settings.GuildSettingsEntity;
import pl.miloszgilga.domain.guild_settings.IGuildSettingsRepository;

import static pl.miloszgilga.exception.SettingsException.PercentageOutOfBoundsException;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Slf4j
@JDAInjectableCommandLazyService
public class SetSkipRatioCmd extends AbstractGuildSettingsCommand {

    SetSkipRatioCmd(
        BotConfiguration config, EmbedMessageBuilder embedBuilder, RemotePropertyHandler handler,
        IGuildSettingsRepository repository, CacheableGuildSettingsDao cacheableGuildSettingsDao
    ) {
        super(BotCommand.SET_SKIP_RATIO, config, embedBuilder, handler, repository, cacheableGuildSettingsDao);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void doExecuteGuildSettingsCommand(CommandEventWrapper event) {
        final Integer skipRatio = event.getArgumentAndParse(BotCommandArgument.SET_SKIP_RATIO_TAG);
        final int defSkipRatio = config.getProperty(BotProperty.J_VOTING_PERCENTAGE_RATIO, Integer.class);

        GuildSettingsEntity settingsToSave;
        MessageEmbed messageEmbed;

        if (Objects.isNull(skipRatio)) { // reset
            settingsToSave = cacheableGuildSettingsDao.setCacheableProperty(event,
                guildSettings -> guildSettings.setSkipRatio(null));
            messageEmbed = embedBuilder.createMessage(ResLocaleSet.SKIP_RATIO_WAS_RESET_MESS, Map.of(
                "setSkipRatioCmd", BotCommand.SET_SKIP_RATIO.parseWithPrefix(config)
            ), event.getGuild());
            JDALog.info(log, event, "Skip ratio was successfully reset to '%s' (default value)", defSkipRatio);
        } else {
            if (skipRatio < 1 || skipRatio > 100) {
                throw new PercentageOutOfBoundsException(config, event);
            }
            settingsToSave = cacheableGuildSettingsDao.setCacheableProperty(event,
                guildSettings -> guildSettings.setSkipRatio(skipRatio));
            messageEmbed = embedBuilder.createMessage(ResLocaleSet.SKIP_RATIO_WAS_SETTED_MESS, Map.of(
                "skipRatio", skipRatio,
                "setSkipRatioCmd", BotCommand.SET_SKIP_RATIO.parseWithPrefix(config)
            ), event.getGuild());
            JDALog.info(log, event, "Skip ratio was successfully setted to '%s'", skipRatio);
        }
        repository.save(settingsToSave);
        event.sendEmbedMessage(messageEmbed);
    }
}