package com.qhy.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;


import com.qhy.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限相关工具类
 */
public class PermissionUtils {
    /**
     * 检查App是否拥有指定权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean isHasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检测指定的所有的权限是否都已授权
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean isHasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (!isHasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限真正需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    public static List<String> getNeedRequestPermissions(Context context, String... permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (!isHasPermission(context, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 校验请求权限返回结果，看是否都已授权
     *
     * @param grantResults
     * @return
     */
    public static boolean verifyPermissionsResult(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public static void onRequestPermissionsFail(Activity activity, String[] permissions) {
        //如果用户在设备中禁用了权限，并且没有弹窗提示，跳转该应用设置页，让用户自己设置权限
        for (String permission : permissions) {
            //shouldShowRequestPermissionRationale()方法：
            //   如果没有请求过此权限，返回true
            //   如果请求过此权限但用户拒绝了请求，此方法将返回 true。
            //   如果用户拒绝过此权限，并在权限请求系统对话框中选择了“不再提醒”选项，此方法将返回 false。
            //   如果设备规范禁止应用具有该权限，此方法也会返回 false。
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                ToastUtils.showShort(R.string.request_permission_remind);

                PermissionUtils.jumpApplicationSettings(activity);
                break;
            }
        }
    }

    public static void requestPermissions(Activity activity, int requestCode, String... permissions) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static void requestPermissions(Fragment fragment, int requestCode, String... permissions) {
        if (fragment == null) {
            return;
        }
        fragment.requestPermissions(permissions, requestCode);
    }

    /**
     * 跳转到应用设置页面
     *
     * @param context Context
     */
    public static void jumpApplicationSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        try {
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否具有安装未知应用的权限，8.0版本及以上需要用户进入设置手动打开权限
     *
     * @param context 上下文
     * @return true or false
     */
    public static boolean isHasInstallPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.canRequestPackageInstalls();
        }
        return true;
    }

    public static void jumpInstallPermissionForResult(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri uri = Uri.parse("package:" + activity.getPackageName());
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, uri);
            activity.startActivityForResult(intent, requestCode);
        }
    }


}
