/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwc.exception.dj

import pl.jwizard.jwc.core.i18n.source.I18nExceptionSource
import pl.jwizard.jwc.core.jda.command.CommandBaseContext
import pl.jwizard.jwc.exception.CommandPipelineExceptionHandler

/**
 * Exception thrown when a user attempts to invoke a DJ command without the required DJ role.
 *
 * @param commandBaseContext The context of the command that triggered this exception.
 * @param djRoleName The name of the missing DJ role required to execute the command.
 * @author Miłosz Gilga
 */
class UnauthorizedDjException(
	commandBaseContext: CommandBaseContext,
	djRoleName: String,
) : CommandPipelineExceptionHandler(
	commandBaseContext,
	i18nExceptionSource = I18nExceptionSource.UNAUTHORIZED_DJ,
	logMessage = "Attempt to invoke DJ command without DJ role: \"$djRoleName\".",
)