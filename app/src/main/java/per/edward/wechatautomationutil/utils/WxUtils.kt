package per.edward.wechatautomationutil.utils

import android.app.Activity


class WxUtils {
    companion object {
        fun openWx(activity:Activity) {
            val packageManager = activity.packageManager
            val it = packageManager.getLaunchIntentForPackage("com.tencent.mm")
            activity.startActivity(it)
        }
    }
}