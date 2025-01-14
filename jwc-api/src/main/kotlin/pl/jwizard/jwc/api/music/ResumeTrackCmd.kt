/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwc.api.music

import net.dv8tion.jda.api.entities.MessageEmbed
import pl.jwizard.jwc.api.CommandEnvironmentBean
import pl.jwizard.jwc.api.MusicCommandBase
import pl.jwizard.jwc.audio.manager.GuildMusicManager
import pl.jwizard.jwc.command.async.TUpdatableCommandHook
import pl.jwizard.jwc.command.context.GuildCommandContext
import pl.jwizard.jwc.command.reflect.JdaCommand
import pl.jwizard.jwc.core.i18n.source.I18nResponseSource
import pl.jwizard.jwc.core.jda.color.JdaColor
import pl.jwizard.jwc.core.jda.command.TFutureResponse
import pl.jwizard.jwc.core.util.ext.mdTitleLink
import pl.jwizard.jwc.core.util.ext.name
import pl.jwizard.jwc.core.util.ext.qualifier
import pl.jwizard.jwc.core.util.jdaInfo
import pl.jwizard.jwc.exception.UnexpectedException
import pl.jwizard.jwl.command.Command
import pl.jwizard.jwl.util.logger

/**
 * Command for resuming a paused track.
 *
 * This command resumes a track that was previously paused and provides feedback to the user, including track details
 * and relevant commands for further interactions, such as pausing again.
 *
 * @param commandEnvironment The environment context for executing the command.
 * @author Miłosz Gilga
 */
@JdaCommand(Command.RESUME)
class ResumeTrackCmd(
	commandEnvironment: CommandEnvironmentBean,
) : MusicCommandBase(commandEnvironment), TUpdatableCommandHook<GuildMusicManager> {

	companion object {
		private val log = logger<ResumeTrackCmd>()
	}

	override val shouldPaused = true
	override val shouldOnSameChannelWithBot = true
	override val shouldBeContentSenderOrSuperuser = true

	/**
	 * Executes the resume command.
	 *
	 * This method resumes the paused track using the music manager and initiates an asynchronous update to reflect the
	 * change. The result is sent to the user through the provided response object.
	 *
	 * @param context The context of the command, containing user interaction details.
	 * @param manager The guild music manager responsible for handling the audio queue and playback.
	 * @param response The future response object used to send the result of the command execution.
	 */
	override fun executeMusic(context: GuildCommandContext, manager: GuildMusicManager, response: TFutureResponse) {
		val asyncUpdatableHandler = createAsyncUpdatablePlayerHandler(context, response, this)
		asyncUpdatableHandler.performAsyncUpdate(
			asyncAction = manager.createdOrUpdatedPlayer.setPaused(false),
			payload = manager,
		)
	}

	/**
	 * Called when the async resume operation succeeds.
	 *
	 * This method generates a detailed response to the user, including the track's title, the user who invoked the
	 * command, and an option to pause the track again. The response is embedded as a rich message with visual elements.
	 *
	 * @param context The context of the command, containing user interaction details.
	 * @param payload The guild music manager used for handling audio playback and queue management.
	 * @return A MessageEmbed containing detailed information about the resumed track.
	 * @throws UnexpectedException If the resumed track is not found.
	 */
	override fun onAsyncSuccess(context: GuildCommandContext, payload: GuildMusicManager): MessageEmbed {
		val resumedTrack = payload.cachedPlayer?.track ?: throw UnexpectedException(context, "Resumed track is NULL.")
		log.jdaInfo(context, "Current paused track: %s was resumed.", resumedTrack.qualifier)

		return createEmbedMessage(context)
			.setDescription(
				i18nLocaleSource = I18nResponseSource.RESUME_TRACK,
				args = mapOf(
					"track" to resumedTrack.mdTitleLink,
					"invoker" to context.author.name,
					"pauseCmd" to Command.PAUSE.parseWithPrefix(context),
				),
			)
			.setArtwork(resumedTrack.thumbnailUrl)
			.setColor(JdaColor.PRIMARY)
			.build()
	}
}
