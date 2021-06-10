package de.twisted.imagepicker.utility;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import de.twisted.imagepicker.interfaces.WorkFinish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 11/14/16.
 */

public abstract class PermUtil {

    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 9921;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean addPermission(List<String> permissionsList, String permission, Activity ac) {
        if (ac.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            return ac.shouldShowRequestPermissionRationale(permission);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void checkForPermissions(Activity activity, boolean camera, WorkFinish workFinish) {
        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<String>();
        if (camera && !addPermission(permissionsList, Manifest.permission.CAMERA, activity))
            permissionsNeeded.add("CAMERA");
        if (!addPermission(permissionsList, permission.READ_EXTERNAL_STORAGE, activity))
            permissionsNeeded.add("READ_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE, activity))
            permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
        if (permissionsList.size() > 0) {
            activity.requestPermissions(permissionsList.toArray(new String[0]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            workFinish.onWorkFinish(true);
        }
    }

}
