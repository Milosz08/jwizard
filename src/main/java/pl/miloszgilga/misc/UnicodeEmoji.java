/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: UnicodeEmoji.java
 * Last modified: 04/04/2023, 04:07
 * Project name: jwizard-discord-bot
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     <http://www.apache.org/license/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the license.
 */

package pl.miloszgilga.misc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import net.dv8tion.jda.api.entities.MessageReaction;

import java.util.List;
import java.util.Arrays;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Getter
@RequiredArgsConstructor
public enum UnicodeEmoji {
    THUMBS_UP               ("\uD83D\uDC4D", 0),
    THUMBS_DOWN             ("\uD83D\uDC4E", 0),

    NUMBER_ZERO             ("\u0030\u20E3", 0),
    NUMBER_ONE              ("\u0031\u20E3", 1),
    NUMBER_TWO              ("\u0032\u20E3", 2),
    NUMBER_THREE            ("\u0033\u20E3", 3),
    NUMBER_FOUR             ("\u0034\u20E3", 4),
    NUMBER_FIVE             ("\u0035\u20E3", 5),
    NUMBER_SIX              ("\u0036\u20E3", 6),
    NUMBER_SEVEN            ("\u0037\u20E3", 7),
    NUMBER_EIGHT            ("\u0038\u20E3", 8),
    NUMBER_NINE             ("\u0039\u20E3", 9);

    // https://unicode.org/emoji/charts/full-emoji-list.html#1f44d

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final String code;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean checkEquals(MessageReaction.ReactionEmote emote) {
        return code.equals(emote.getEmoji());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static List<UnicodeEmoji> getNumbers(int maxNumbers) {
        return Arrays.stream(values())
            .filter(e -> e.code.contains("\u20E3"))
            .limit(maxNumbers)
            .toList();
    }
}
