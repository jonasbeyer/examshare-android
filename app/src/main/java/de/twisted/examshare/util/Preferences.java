package de.twisted.examshare.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import de.twisted.examshare.ExamShareApplication;

public class Preferences {

    public final static String INFO_URL = "https://examshare.twisted-it.de";
    public final static String DONATION_URL = INFO_URL + "/donate";
    public final static String IMPRINT_URL = INFO_URL + "/impressum";
    public final static String PRIVACY_URL = INFO_URL + "/datenschutzerklaerung";

    public final static int MIN_TAGS = 2;
    public final static int MAX_TAGS = 4;
    public final static int MAX_TASK_IMAGES = 4;
    public final static int MAX_SOLUTION_IMAGES = 6;

    public static boolean isOfflineCache() {
        return getPreferences().getBoolean("save_images", true);
    }

    public static boolean isVibrationEnabled() {
        return getPreferences().getBoolean("vibration", false);
    }

    public static Uri getRingtoneUri() {
        String defaultUri = "content://settings/system/notification_sound";
        String uri = getPreferences().getString("ringtone", defaultUri);
        return uri.isEmpty() ? null : Uri.parse(uri);
    }

    public static SharedPreferences getPreferences() {
        return null;
        // return PreferenceManager.getDefaultSharedPreferences(ExamShareApplication.getInstance());
    }

}
