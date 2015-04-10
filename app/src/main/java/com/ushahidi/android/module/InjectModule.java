/*
 * Copyright (c) 2015 Ushahidi.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program in the file LICENSE-AGPL. If not, see
 * https://www.gnu.org/licenses/agpl-3.0.html
 */

package com.ushahidi.android.module;

import com.ushahidi.android.ui.activity.AboutActivity;
import com.ushahidi.android.ui.activity.MainActivity;
import com.ushahidi.android.ui.activity.SendFeedbackActivity;
import com.ushahidi.android.ui.activity.SettingsActivity;
import com.ushahidi.android.ui.fragment.AboutFragment;
import com.ushahidi.android.ui.fragment.SendFeedbackFragment;
import com.ushahidi.android.ui.fragment.SettingsFragment;

import dagger.Module;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Module(library = true, complete = false,
    injects = {MainActivity.class, SettingsFragment.class, SettingsActivity.class, AboutFragment.class,
        AboutActivity.class, SendFeedbackActivity.class, SendFeedbackFragment.class})
public class InjectModule {

}
