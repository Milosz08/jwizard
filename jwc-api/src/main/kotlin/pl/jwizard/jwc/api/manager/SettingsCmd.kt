/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwc.api.manager

import net.dv8tion.jda.api.entities.MessageEmbed
import pl.jwizard.jwc.api.ManagerCommandBase
import pl.jwizard.jwc.command.CommandEnvironmentBean
import pl.jwizard.jwc.command.context.CommandContext
import pl.jwizard.jwc.command.refer.Command
import pl.jwizard.jwc.command.refer.CommandArgument
import pl.jwizard.jwc.command.reflect.JdaCommand
import pl.jwizard.jwc.core.i18n.I18nLocaleSource
import pl.jwizard.jwc.core.i18n.source.I18nSystemSource
import pl.jwizard.jwc.core.jda.color.JdaColor
import pl.jwizard.jwc.core.jda.command.CommandResponse
import pl.jwizard.jwc.core.jda.command.TFutureResponse
import pl.jwizard.jwc.core.property.BotProperty

/**
 * Command that displays the current guild settings in a paginated view.
 *
 * @param commandEnvironment The environment context for command execution.
 * @author Miłosz Gilga
 */
@JdaCommand(id = Command.SETTINGS)
class SettingsCmd(commandEnvironment: CommandEnvironmentBean) : ManagerCommandBase(commandEnvironment) {

	/**
	 * Executes the command to display guild settings to the user. The settings are fetched from the guild and displayed
	 * in a paginated format.
	 *
	 * @param context The context of the command, which contains details of the user interaction.
	 * @param response The future response object used to send the result of the command execution.
	 */
	override fun executeManager(context: CommandContext, response: TFutureResponse) {
		val paginatorChunkSize = environmentBean.getProperty<Int>(BotProperty.JDA_PAGINATION_CHUNK_SIZE)
		val settings = guildSettingsEventAction.getGuildSettings(context.guild.idLong)

		val embedMessages = mutableListOf<MessageEmbed>()
		for (chunk in settings.toList().chunked(paginatorChunkSize)) {
			val messageBuilder = createEmbedMessage(context)
				.setTitle(I18nSystemSource.GUILD_SETTINGS_HEADER)

			for ((column, value) in chunk) {
				val key = i18nBean.t(column.i18nSourceKey, context.guildLanguage)
				val mappedValue = if (value == null) {
					null.toString()
				} else {
					val convertedValue = column.converter.mapper(value)
					if (column.converter.isI18nContent) {
						i18nBean.t(convertedValue as I18nLocaleSource, context.guildLanguage)
					} else {
						convertedValue.toString()
					}
				}
				messageBuilder.setKeyValueField(key, mappedValue, inline = false)
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
	override fun isPrivate(context: CommandContext): Long? {
		val isPrivate = context.getNullableArg<Boolean>(CommandArgument.PRIVATE)
		return if (isPrivate == true) context.author.idLong else null
	}
}