/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwc.api.radio

import pl.jwizard.jwc.api.CommandEnvironmentBean
import pl.jwizard.jwc.api.RadioCommandBase
import pl.jwizard.jwc.audio.manager.GuildMusicManager
import pl.jwizard.jwc.command.context.GuildCommandContext
import pl.jwizard.jwc.command.reflect.JdaCommand
import pl.jwizard.jwc.core.jda.command.TFutureResponse
import pl.jwizard.jwl.command.Command

/**
 * Command to stop playing the current radio station.
 *
 * This command allows users to stop the radio stream that is currently playing. It ensures that the user is in the
 * same channel as the bot and that the radio is currently playing.
 *
 * @param commandEnvironment The environment context for executing the command.
 * @author Miłosz Gilga
 */
@JdaCommand(Command.RADIO_STOP)
class StopPlayingRadioStationCmd(commandEnvironment: CommandEnvironmentBean) : RadioCommandBase(commandEnvironment) {

	override val shouldOnSameChannelWithBot = true
	override val shouldRadioPlaying = true

	/**
	 * Executes the command to stop the currently playing radio stream. This method stops the radio stream and destroys
	 * any related resources, ensuring a clean shutdown.
	 *
	 * @param context The context of the command, containing user interaction details and guild information.
	 * @param manager The guild music manager responsible for handling the audio playback and stream management.
	 * @param response The future response object used to send the result of the command execution.
	 */
	override fun executeRadio(context: GuildCommandContext, manager: GuildMusicManager, response: TFutureResponse) {
		val radioStreamScheduler = manager.state.radioStreamScheduler
		radioStreamScheduler.stopAndDestroy().subscribe()
	}
}
