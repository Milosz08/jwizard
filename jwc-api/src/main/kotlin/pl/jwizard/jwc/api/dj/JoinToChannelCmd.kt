/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwc.api.dj

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import net.dv8tion.jda.api.exceptions.PermissionException
import pl.jwizard.jwc.api.CommandEnvironmentBean
import pl.jwizard.jwc.api.DjCommandBase
import pl.jwizard.jwc.audio.manager.GuildMusicManager
import pl.jwizard.jwc.command.context.GuildCommandContext
import pl.jwizard.jwc.command.reflect.JdaCommand
import pl.jwizard.jwc.core.i18n.source.I18nResponseSource
import pl.jwizard.jwc.core.jda.color.JdaColor
import pl.jwizard.jwc.core.jda.command.CommandResponse
import pl.jwizard.jwc.core.jda.command.TFutureResponse
import pl.jwizard.jwc.core.util.ext.qualifier
import pl.jwizard.jwc.core.util.jdaInfo
import pl.jwizard.jwc.core.util.mdCode
import pl.jwizard.jwc.exception.command.InsufficientPermissionsException
import pl.jwizard.jwc.exception.user.UserIsAlreadyWithBotException
import pl.jwizard.jwc.exception.user.UserOnVoiceChannelNotFoundException
import pl.jwizard.jwl.command.Command
import pl.jwizard.jwl.util.logger

/**
 * Command that allows the bot to join or move to the same voice channel as the user.
 *
 * This command ensures that the user is in a voice channel, and if the bot is not already in the same channel, it will
 * be moved to join the user. If the bot is already in the channel, an exception is thrown. A message confirming the
 * move is sent to the user.
 *
 * @param commandEnvironment The environment context for the command execution.
 * @author Miłosz Gilga
 */
@JdaCommand(Command.JOIN)
class JoinToChannelCmd(commandEnvironment: CommandEnvironmentBean) : DjCommandBase(commandEnvironment) {

	companion object {
		private val log = logger<JoinToChannelCmd>()
	}

	override val shouldPlayingMode = true
	override val shouldAllowAlsoForAllContentSender = true

	/**
	 * Executes the command to move the bot to the user's voice channel.
	 *
	 * @param context The context of the command, including user interaction details.
	 * @param manager The guild music manager responsible for handling the audio queue.
	 * @param response The future response object used to send the result of the command execution.
	 */
	override fun executeDj(context: GuildCommandContext, manager: GuildMusicManager, response: TFutureResponse) {
		val voiceChannelWithMember = context.guild.voiceChannels
			.find { mapMembersToIds(it).contains(context.author.idLong) }
			?: throw UserOnVoiceChannelNotFoundException(context)

		if (mapMembersToIds(voiceChannelWithMember).contains(context.selfMember.idLong)) {
			throw UserIsAlreadyWithBotException(context, voiceChannelWithMember)
		}
		context.selfMember.let {
			try {
				context.guild.moveVoiceMember(it, voiceChannelWithMember).complete()
				log.jdaInfo(context, "Bot was successfully moved to channel: %s", voiceChannelWithMember.qualifier)
			} catch (ex: PermissionException) {
				throw InsufficientPermissionsException(context, mdCode(ex.permission.name), ex.permission)
			}
		}
		val message = createEmbedMessage(context)
			.setDescription(
				i18nLocaleSource = I18nResponseSource.MOVE_BOT_TO_SELECTED_CHANNEL,
				args = mapOf("movedChannel" to voiceChannelWithMember.name),
			)
			.setColor(JdaColor.PRIMARY)
			.build()

		val commandResponse = CommandResponse.Builder()
			.addEmbedMessages(message)
			.build()

		response.complete(commandResponse)
	}

	/**
	 * Maps the members of a given voice channel to their corresponding user IDs.
	 *
	 * @param channel The voice channel whose members' IDs are being retrieved.
	 * @return A list of member IDs in the voice channel.
	 */
	private fun mapMembersToIds(channel: VoiceChannel) = channel.members.map(Member::getIdLong)
}
