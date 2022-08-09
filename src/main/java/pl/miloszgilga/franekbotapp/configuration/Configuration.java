/*
 * Copyright (c) 2022 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 * File name: Configuration.java
 * Last modified: 22/07/2022, 22:09
 * Project name: franek-bot
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

package pl.miloszgilga.franekbotapp.configuration;

import lombok.Data;


@Data
public class Configuration {
    private boolean showFancyTitle;
    private String botVersion;
    private boolean developmentMode;
    private AuthorizationConfiguration authorization;
    private CommandExecutorsConfiguration executors;
    private SequencersConfiguration sequencers;
    private MiscellaneousConfiguration miscellaneous;

    public String getPrefix() {
        return executors.getDefPrefix();
    }

    public byte getMaxVotingElapseTimeMinutes() {
        return sequencers.getMaxVotingElapseTimeMinutes();
    }

    public byte getQueuePaginationMaxElmsOnPage() {
        return executors.getQueuePaginationMaxElmsOnPage();
    }

    public byte getMaxInactivityTimeMinutes() {
        return sequencers.getMaxInactivityTimeMinutes();
    }

    public byte getMaxInactivityTimeAfterPauseTrackMinutes() {
        return sequencers.getMaxInactivityTimeAfterPauseTrackMinutes();
    }

    public DatabaseConfiguration getDbConfig() {
        return miscellaneous.getDatabase();
    }

    public ActivityStatusSequencerConfiguration getActivitySequencerConfiguration() {
        return sequencers.getActivityStatusSequencer();
    }
}