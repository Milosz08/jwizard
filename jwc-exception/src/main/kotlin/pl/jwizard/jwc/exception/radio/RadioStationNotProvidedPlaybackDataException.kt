/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwc.exception.radio

import pl.jwizard.jwc.core.jda.command.CommandBaseContext
import pl.jwizard.jwc.exception.CommandPipelineExceptionHandler
import pl.jwizard.jwl.i18n.source.I18nExceptionSource

/**
 * Exception thrown when a radio station does not provide playback data while attempting to invoke a radio command.
 *
 * @param context The context of the command where the exception occurred.
 * @param radioStation The identifier of the radio station that failed to provide playback data.
 * @author Miłosz Gilga
 */
class RadioStationNotProvidedPlaybackDataException(
	context: CommandBaseContext,
	radioStation: String,
) : CommandPipelineExceptionHandler(
	commandBaseContext = context,
	i18nExceptionSource = I18nExceptionSource.RADIO_STATION_NOT_PROVIDING_PLAYBACK_DATA,
	logMessage = """
		Attempt to invoke radio command, while radio: "$radioStation" not providing any
		information about audio stream playback.
	""",
)
