/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: CommandCategory.java
 * Last modified: 16/03/2023, 21:01
 * Project name: jwizard-discord-bot
 *
 * Licensed under the MIT license; you may not use this file except in compliance with the License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * THE ABOVE COPYRIGHT NOTICE AND THIS PERMISSION NOTICE SHALL BE INCLUDED IN ALL
 * COPIES OR SUBSTANTIAL PORTIONS OF THE SOFTWARE.
 */

package pl.miloszgilga.misc;

import lombok.RequiredArgsConstructor;

import pl.miloszgilga.core.LocaleSet;
import pl.miloszgilga.core.configuration.BotConfiguration;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@RequiredArgsConstructor
public enum CommandCategory {
    MUSIC               (LocaleSet.COMMAND_CATEGORY_MUSIC),
    DJ                  (LocaleSet.COMMAND_CATEGORY_DJ_ROLE),
    STATISTICS          (LocaleSet.COMMAND_CATEGORY_STATISTICS),
    OWNER_MANAGER       (LocaleSet.COMMAND_CATEGORY_OWNER_AND_MANAGER),
    OTHERS              (LocaleSet.COMMAND_CATEGORY_OTHERS);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final LocaleSet localeSet;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getHolder(BotConfiguration config) {
        return config.getLocaleText(localeSet);
    }
}