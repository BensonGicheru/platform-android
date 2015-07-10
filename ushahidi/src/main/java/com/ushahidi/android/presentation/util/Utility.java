/*
 * Copyright (c) 2015 Ushahidi Inc
 *
 * This program is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Affero General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program in the file LICENSE-AGPL. If not, see
 *  https://www.gnu.org/licenses/agpl-3.0.html
 */

package com.ushahidi.android.presentation.util;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Generic Utilities for the application
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class Utility {

    public static boolean isCollectionEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static int collectionSize(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }

    public static String capitalizeFirstLetter(String text) {
        if (text.length() == 0) {
            return text;
        }

        return text.substring(0, 1).toUpperCase(Locale.getDefault())
                + text.substring(1).toLowerCase(Locale.getDefault());

    }

    public static boolean validateHexColor(String hexColor) {
        if (TextUtils.isEmpty(hexColor)) {
            return false;
        }
        final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
        return Pattern.compile(HEX_PATTERN).matcher(hexColor).matches();
    }

    public static void writeDbToSDCard() {
        File f = new File("/data/data/com.ushahidi.android/databases/ushahidi.db");
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(f);
            fos = new FileOutputStream("/mnt/sdcard/ush_dump.db");
            while (true) {
                int i = fis.read();
                if (i != -1) {
                    fos.write(i);
                } else {
                    break;
                }
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ioe) {
            }
        }
    }

}
