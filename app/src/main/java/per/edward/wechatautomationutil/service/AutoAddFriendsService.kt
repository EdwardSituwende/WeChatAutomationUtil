package per.edward.wechatautomationutil.service

import android.accessibilityservice.AccessibilityService
import android.app.Activity
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import per.edward.wechatautomationutil.ui.AutoAddFriendsActivity
import per.edward.wechatautomationutil.utils.Constant
import per.edward.wechatautomationutil.utils.FileUtils
import per.edward.wechatautomationutil.utils.LogUtil
import per.edward.wechatautomationutil.utils.OperationUtils
import java.io.File
import java.util.ArrayList

class AutoAddFriendsService : AccessibilityService() {
    override fun onServiceConnected() {
        loadFile()
    }

    private fun loadFile() {
        var list = Environment.getExternalStorageDirectory().listFiles()
        var filePath: String? = null
        for (i in list) {//寻找指定文件
            if (i.name.contains("my_folder.txt")) {
                filePath = i.path
                break
            }
        }

        var file = File(filePath)
        if (file.isFile) {
            var content = FileUtils.readTxtFile(file)
//            Log.e("输出",content)
            listNumber = content.split("\n") as ArrayList<String>
        }
    }

    override fun onInterrupt() {
    }

    private val TEMP = 2000
    private var accessibilityNodeInfo: AccessibilityNodeInfo? = null
    private var stepOne = false
    private var stepTwo = false
    private var stepThree = false
    private var stepFour = false
    private var sendFinish = false
    private var listNumber = ArrayList<String>()
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        accessibilityNodeInfo = rootInActiveWindow
        val eventType = event!!.eventType
        var classNameStr = event.className
        LogUtil.e(eventType.toString() + "             " + Integer.toHexString(eventType) + "         " + event.className)
//        if (classNameStr == "com.tencent.mm.ui.LauncherUI") {
//            clickRightMore()
//        } else if (stepOne && classNameStr == "android.widget.ListView") {
//            clickAddFriendsButton()
//        } else
        if (classNameStr == "com.tencent.mm.plugin.subapp.ui.pluginapp.AddMoreFriendsUI") {
            i = 0
//            val sharedPreferences = getSharedPreferences(Constant.WECHAT_STORAGE, Activity.MODE_MULTI_PROCESS)
//            var contentStr = sharedPreferences.getString("content", "")
//            listNumber = contentStr.split("\n") as ArrayList<String>
        } else if (classNameStr == "com.tencent.mm.plugin.fts.ui.FTSAddFriendUI") {
            clearPasteFriendsNumber()
        } else if (classNameStr == "com.tencent.mm.plugin.profile.ui.ContactInfoUI") {
            addToContacts()
        } else if (classNameStr == "com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI") {
            sendFinish()
        }
    }

//    /**
//     * @param step
//     */
//    private fun checkStep(step: Int): Boolean {
//        for (i in 0..1) {
//            val booleans = ArrayList<Boolean>()
//            booleans.add(true)
//            return false
//        }
//        return true
//    }
//
//    /**
//     * 点击微信右上角更多
//     */
//    private fun clickRightMore() {
//        val list = accessibilityNodeInfo?.findAccessibilityNodeInfosByText("更多功能按钮")//微信6.6.6版本修改为发表
//        stepOne = OperationUtils.performClickBtn(list)
//    }
//
//    /**
//     * 点击添加好友选项
//     */
//    private fun clickAddFriendsButton() {
//        var ac = accessibilityNodeInfo?.getChild(0)
//        if (ac != null && ac.childCount > 2) {
//            stepTwo = OperationUtils.performClickBtn(ac.getChild(1))
//            stepOne = !stepTwo
//        }
//    }
//
//    /**
//     * 点击搜索好友
//     */
//    private fun clickSearchFriends() {
//        Handler().postDelayed({
//            val ac = accessibilityNodeInfo?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/j2")//微信6.6.7版本修改为发表
//            if (ac != null && ac.size != 0) {
//                var temp = ac[0].parent
//                if (temp != null) {
////                    LogUtil.e(temp.getChild(1).childCount.toString())
//                    stepTwo = OperationUtils.performClickBtn(temp)
//                }
//            }
//        }, TEMP.toLong())
//    }

    private fun clearPasteFriendsNumber() {//com.tencent.mm.plugin.fts.ui.FTSAddFriendUI
        if (!stepThree) {
            Handler().postDelayed({
                stepThree = true
                val list1 = accessibilityNodeInfo?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hv")//点击清除微信号文本按钮
                if (list1 != null && list1.size != 0) {
                    OperationUtils.performClickBtn(list1[0])
                }
                pasteFriendsNumber()
            }, TEMP.toLong())
        }
    }

    var i: Int = 0

    /**
     * 复制好友微信号
     */
    private fun pasteFriendsNumber() {
        if (i < listNumber.size) {
            Handler().postDelayed({
                val list = accessibilityNodeInfo?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hz")
                if (list != null && list.size != 0) {
                    var temp = OperationUtils.pasteContent(this, list[0], listNumber[i++])
                    if (temp) {
                        goSearchFriends()
                    }
                }
            }, TEMP.toLong())
        } else {
            Toast.makeText(baseContext, "已添加" + listNumber.size + "个好友，好友添加完毕！", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 开始搜索好友
     */
    private fun goSearchFriends() {
        Handler().postDelayed({
            val ac = accessibilityNodeInfo?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bcq")
            if (ac != null && ac.size != 0) {
                var ac1 = ac[0]
                if (ac1 != null && ac1.childCount != 0) {
                    OperationUtils.performClickBtn(ac1.getChild(0))
                }
            }
        }, TEMP.toLong())
    }

    /**
     * 添加好友到联系人列表
     */
    private fun addToContacts() {
        if (sendFinish) {
            goBackSearch()
        } else {
            Handler().postDelayed({
                val ac = accessibilityNodeInfo?.findAccessibilityNodeInfosByText("添加到通讯录")
                if (ac != null && ac.size != 0) {
                    OperationUtils.performClickBtn(ac[0])
                }
            }, TEMP.toLong())
        }
    }

    /**
     * 发送完成
     */
    private fun sendFinish() {
        Handler().postDelayed({
            val ac = accessibilityNodeInfo?.findAccessibilityNodeInfosByText("发送")
            if (ac != null && ac.size != 0) {
                sendFinish = OperationUtils.performClickBtn(ac[1])
            }
        }, TEMP.toLong())
    }

    /**
     * 返回搜索页面
     */
    private fun goBackSearch() {
        stepThree = false
        sendFinish = false
        Handler().postDelayed({
            val ac = accessibilityNodeInfo?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hs")
            if (ac != null && ac.size != 0) {
                OperationUtils.performClickBtn(ac)
            }
        }, TEMP.toLong())

    }


}








