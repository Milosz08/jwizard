/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: IMemberSettingsRepository.java
 * Last modified: 09/04/2023, 23:04
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

package pl.miloszgilga.domain.member_settings;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Repository
public interface IMemberSettingsRepository extends JpaRepository<MemberSettingsEntity, Long> {
    Optional<MemberSettingsEntity> findByMember_DiscordIdAndGuild_DiscordId(String memberDiscordId, String guildDiscordId);
    void deleteByMember_DiscordIdAndGuild_DiscordId(String memberDiscordId, String guildDiscordId);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Query(value = """
        select e.statsPrivate from MemberSettingsEntity e join e.guild g join e.member m
        where m.discordId = :memberDiscordId and g.discordId = :guildDiscordId
    """)
    Boolean isStatsPrivate(
        @Param("memberDiscordId")   String memberDiscordId,
        @Param("guildDiscordId")    String guildDiscordId
    );

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Query(value = """
        select e.statsDisabled from MemberSettingsEntity e join e.guild g join e.member m
        where m.discordId = :memberDiscordId and g.discordId = :guildDiscordId
    """)
    Boolean isStatsDisabled(
        @Param("memberDiscordId")   String memberDiscordId,
        @Param("guildDiscordId")    String guildDiscordId
    );
}