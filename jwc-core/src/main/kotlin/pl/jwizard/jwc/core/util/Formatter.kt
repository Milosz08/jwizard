/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwc.core.util

import net.dv8tion.jda.api.entities.Guild

/**
 * Formats the provided [Guild] object into a string representation.
 * Formatted [String] that includes the guild's name and ID.
 * Example format: `"GuildName <@GuildID>"`.
 *
 * @author Miłosz Gilga
 */
val Guild.qualifier get() = "\"$name <@${id}>\""
