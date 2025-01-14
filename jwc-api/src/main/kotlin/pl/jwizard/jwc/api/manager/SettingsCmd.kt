/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwc.api.manager

import net.dv8tion.jda.api.entities.MessageEmbed
import pl.jwizard.jwc.api.CommandEnvironmentBean
import pl.jwizard.jwc.api.ManagerCommandBase
import pl.jwizard.jwc.command.context.GuildCommandContext
import pl.jwizard.jwc.command.reflect.JdaCommand
import pl.jwizard.jwc.core.i18n.source.I18nSystemSource
import pl.jwizard.jwc.core.jda.color.JdaColor
import pl.jwizard.jwc.core.jda.command.CommandResponse
import pl.jwizard.jwc.core.jda.command.TFutureResponse
import pl.jwizard.jwc.core.property.BotProperty
import pl.jwizard.jwc.core.property.guild.GuildProperty
import pl.jwizard.jwl.command.Command
import pl.jwizard.jwl.command.arg.Argument
import pl.jwizard.jwl.i18n.I18nLocaleSource

/**
 * Command that displays the current guild settings in a paginated view.
 *
 * @param commandEnvironment The environment context for command execution.
 * @author Miłosz Gilga
 */
@JdaCommand(Command.SETTINGS)
class SettingsCmd(commandEnvironment: CommandEnvironmentBean) : ManagerCommandBase(commandEnvironment) {

	/**
	 * Executes the command to display guild settings to the user. The settings are fetched from the guild and displayed
	 * in a paginated format.
	 *
	 * @param context The context of the command, which contains details of the user interaction.
	 * @param response The future response object used to send the result of the command execution.
	 */
	override fun executeManager(context: GuildCommandContext, response: TFutureResponse) {
		val paginatorChunkSize = environment.getProperty<Int>(BotProperty.JDA_PAGINATION_CHUNK_SIZE)
		val properties = environment.getGuildMultipleProperties(
			guildProperties = GuildProperty.entries.filter { it.placeholder.isNotEmpty() && it.converter != null },
			guildId = context.guild.idLong,
		)
		val embedMessages = mutableListOf<MessageEmbed>()
		val sortedProps = properties.toList().sortedBy { it.first.ordinal }

		for (chunk in sortedProps.chunked(paginatorChunkSize)) {
			val messageBuilder = createEmbedMessage(context)
				.setTitle(I18nSystemSource.GUILD_SETTINGS_HEADER)

			for ((index, pair) in chunk.withIndex()) {
				val (column, value) = pair
				val converter = column.converter
				if (column.placeholder.isEmpty() || converter == null) {
					continue
				}
				val key = i18n.t(column, context.language)
				val convertedValue = converter.mapper(value)
				val parsedValue = if (converter.isI18nContent) {
					i18n.t(convertedValue as I18nLocaleSource, context.language)
				} else {
					convertedValue.toString()
				}
				messageBuilder.setKeyValueField(key, parsedValue)
				if (index % 2 == 0) {
					messageBuilder.setSpace()
				}
			}
			val message = messageBuilder
				.setColor(JdaColor.PRIMARY)
				.build()
			embedMessages.add(message)
		}
		val paginator = createPaginator(context, embedMessages)
		val row = paginator.createPaginatorButtonsRow()
		val initMessage = paginator.initPaginator()

		val commandResponse = CommandResponse.Builder()
			.addEmbedMessages(initMessage)
			.addActionRows(row)
			.build()

		response.complete(commandResponse)
	}

	/**
	 * Checks whether the command should be executed in private mode. If the command is executed with a "private"
	 * argument set to true, it returns the author's ID to restrict visibility.
	 *
	 * @param context The context of the command execution.
	 * @return The author's ID if the command is private, or null if it is not private.
	 */
	override fun isPrivate(context: GuildCommandContext): Long? {
		val isPrivate = context.getNullableArg<Boolean>(Argument.PRIVATE)
		return if (isPrivate == true) context.author.idLong else null
	}
}
