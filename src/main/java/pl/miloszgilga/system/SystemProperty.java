/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: SystemProperty.java
 * Last modified: 23/03/2023, 02:47
 * Project name: jwizard-discord-bot
 *
 * Licensed under the MIT license; you may not use this file except in compliance with the License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * THE ABOVE COPYRIGHT NOTICE AND THIS PERMISSION NOTICE SHALL BE INCLUDED IN ALL COPIES OR
 * SUBSTANTIAL PORTIONS OF THE SOFTWARE.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited
 * to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no event
 * shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an
 * action of contract, tort or otherwise, arising from, out of or in connection with the software or the use
 * or other dealings in the software.
 */

package pl.miloszgilga.system;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import pl.miloszgilga.core.LocaleSet;
import pl.miloszgilga.core.configuration.BotConfiguration;

import java.util.List;
import java.util.Arrays;
import java.util.function.BiFunction;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Getter
@RequiredArgsConstructor
public enum SystemProperty {
    JVM_NAME                        ("java.vm.name",                    LocaleSet.JVM_NAME_JAVA_DEBUG),
    JVM_VERSION                     ("java.version",                    LocaleSet.JVM_VERSION_JAVA_DEBUG),
    JVM_SPEC_VERSION                ("java.vm.specification.version",   LocaleSet.JVM_SPEC_VERSION_JAVA_DEBUG),

    JRE_NAME                        ("java.runtime.name",               LocaleSet.JRE_NAME_JAVA_DEBUG),
    JRE_VERSION                     ("java.runtime.version",            LocaleSet.JRE_VERSION_JAVA_DEBUG),
    JRE_SPEC_VERSION                ("java.specification.version",      LocaleSet.JRE_SPEC_VERSION_JAVA_DEBUG),

    OS_NAME                         ("os.name",                         LocaleSet.OS_NAME_JAVA_DEBUG),
    OS_ARCHITECTURE                 ("os.arch",                         LocaleSet.OS_ARCHITECTURE_JAVA_DEBUG);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final String property;
    private final LocaleSet localeSet;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static List<String> getAllFormatted(BotConfiguration config, BiFunction<String, String, String> formatter) {
        return Arrays.stream(values())
            .map(v -> formatter.apply(config.getLocaleText(v.localeSet), System.getProperty(v.property)))
            .toList();
    }
}