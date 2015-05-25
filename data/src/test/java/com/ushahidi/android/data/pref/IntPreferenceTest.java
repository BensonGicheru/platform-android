/*
 * Copyright (c) 2014 Ushahidi.
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

package com.ushahidi.android.data.pref;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(emulateSdk = 21, reportSdk = 21, constants = BuildConfig.class)
public class IntPreferenceTest extends BaseTestCase {

    private SharedPreferences mSharedPreferences;

    private IntPreference mIntPreference;

    @Before
    public void setup() {
        mSharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(RuntimeEnvironment.application);
    }

    @Test
    public void shouldSaveInt() {
        int expected = 1;
        String key = "testKey";
        mIntPreference = new IntPreference(mSharedPreferences, key);
        mIntPreference.set(expected);
        assertThat(mSharedPreferences.getInt(key, 0), is(equalTo(expected)));
    }

    @Test
    public void shouldGetValue() {
        int expected = 2;
        String key = "testKey2";
        mSharedPreferences.edit().putInt(key, expected).commit();
        mIntPreference = new IntPreference(mSharedPreferences, key, 0);
        int result = mIntPreference.get();
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void shouldIsSetTrue() {
        String key = "testKey3";
        mSharedPreferences.edit().putInt(key, 1).commit();
        mIntPreference = new IntPreference(mSharedPreferences, key);
        boolean result = mIntPreference.isSet();
        assertThat(result, is(true));
    }

    @Test
    public void shouldIsSetFalse() {
        String key = "testKey4";
        mIntPreference = new IntPreference(mSharedPreferences, key);
        boolean result = mIntPreference.isSet();
        assertThat(result, is(false));
    }

    @Test
    public void shouldDeletePreference() {
        String key = "testKey5";
        mSharedPreferences.edit().putInt(key, 3).commit();
        mIntPreference = new IntPreference(mSharedPreferences, key);
        mIntPreference.delete();
        assertThat(mSharedPreferences.contains(key), is(false));
    }
}
