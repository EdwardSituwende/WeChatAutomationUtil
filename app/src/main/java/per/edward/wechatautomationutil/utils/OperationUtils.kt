package per.edward.wechatautomationutil.utils

import android.accessibilityservice.AccessibilityService
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.view.accessibility.AccessibilityNodeInfo

class OperationUtils {

    companion object {
        private const val TEMP = 2000
        /**
         * 粘贴文本
         *
         * @param tempInfo
         * @param contentStr
         * @return true 粘贴成功，false 失败
         */
         fun pasteContent(accessibilityService: AccessibilityService, tempInfo: AccessibilityNodeInfo?, contentStr: String): Boolean {
            if (tempInfo == null) {
                return false
            }
            if (tempInfo.isEnabled && tempInfo.isClickable && tempInfo.isFocusable) {
                val clipboard = accessibilityService.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                val clip = ClipData.newPlainText("text", contentStr)
                if (clipboard == null) {
                    return false
                }
                clipboard.primaryClip = clip
                tempInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                tempInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE)
                return true
            }
            return false
        }

        /**
         * 粘贴文本
         *
         * @param tempInfo
         * @param contentStr
         * @return true 粘贴成功，false 失败
         */
        fun pasteContent1(accessibilityService: AccessibilityService, tempInfo: AccessibilityNodeInfo?, contentStr: String): Boolean {
            if (tempInfo == null) {
                return false
            }
            if (tempInfo.isEnabled && tempInfo.isClickable && tempInfo.isFocusable) {
//                val clipboard = accessibilityService.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
//                val clip = ClipData.newPlainText("text", contentStr)
//                if (clipboard == null) {
//                    return false
//                }
//                clipboard.primaryClip = clip
                tempInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                tempInfo.performAction(AccessibilityNodeInfo.ACTION_SELECT)
                tempInfo.performAction(AccessibilityNodeInfo.ACTION_CUT)
                return true
            }
            return false
        }

        /**
         * @param accessibilityNodeInfoList
         * @return
         */
         fun performClickBtn(accessibilityNodeInfoList: List<AccessibilityNodeInfo>?): Boolean {
            if (accessibilityNodeInfoList != null && accessibilityNodeInfoList.isNotEmpty()) {
                for (i in accessibilityNodeInfoList.indices) {
                    val accessibilityNodeInfo = accessibilityNodeInfoList[i]
                    if (accessibilityNodeInfo != null) {
                        if (accessibilityNodeInfo.isClickable && accessibilityNodeInfo.isEnabled) {
                            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                            return true
                        }
                    }
                }
            }
            return false
        }

        fun performClickBtn(accessibilityNodeInfo: AccessibilityNodeInfo): Boolean {
//            Handler().postDelayed({
//            }, TEMP.toLong())
            if (accessibilityNodeInfo.isClickable && accessibilityNodeInfo.isEnabled) {
                accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                return true
            }
            return false
        }
    }
}