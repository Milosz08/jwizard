package pl.jwizard.jwc.exception.radio

import pl.jwizard.jwc.core.jda.command.CommandBaseContext
import pl.jwizard.jwc.exception.CommandPipelineException
import pl.jwizard.jwl.command.Command
import pl.jwizard.jwl.i18n.source.I18nExceptionSource

class RadioStationIsNotPlayingException(context: CommandBaseContext) : CommandPipelineException(
	commandBaseContext = context,
	i18nExceptionSource = I18nExceptionSource.RADIO_STATION_IS_NOT_PLAYING,
	args = mapOf("playRadioStationCmd" to Command.RADIO_PLAY.parseWithPrefix(context)),
	logMessage = "Attempt to invoke command, while radio station is not playing.",
)
