package per.edward.wechatautomationutil.service

import android.accessibilityservice.AccessibilityService
import android.app.Activity
import android.app.Instrumentation
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
import android.R.attr.y
import android.R.attr.x
import android.view.MotionEvent
import android.os.SystemClock



class AutoAddFriendsService : AccessibilityService() {
    override fun onServiceConnected() {
        loadFile()
    }

    private fun loadFile() {
        var list = Environment.getExternalStorageDirectory().listFiles()
        var filePath: String? = null
        for (i in list) {//寻找指定文件
            if (i.name.contains(Constant.WX_NUMBER_FILE_NAME)) {
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
    private var stepThree = false
    private var sendFinish = false
    private var listNumber = ArrayList<String>()
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        accessibilityNodeInfo = rootInActiveWindow
        val eventType = event!!.eventType
        var classNameStr = event.className
        LogUtil.e(eventType.toString() + "             " + Integer.toHexString(eventType) + "         " + event.className)
        if (classNameStr == "com.tencent.mm.plugin.subapp.ui.pluginapp.AddMoreFriendsUI") {
            i = 0
            stepOne=false
            stepThree=false
            sendFinish=false
        } else if (classNameStr == "com.tencent.mm.plugin.fts.ui.FTSAddFriendUI") {
//            temp=accessibilityNodeInfo
            clearPasteFriendsNumber()
        } else if (classNameStr == "com.tencent.mm.plugin.profile.ui.ContactInfoUI") {//1             1         android.widget.RelativeLayout
//            addToContacts()
        } else if (classNameStr == "com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI") {
//            sendFinish()
        }else if (eventType.toString() == "1" && classNameStr == "android.widget.RelativeLayout") {//查找不到用户的情况
//            stepThree=false
//            clearPasteFriendsNumber()

            clearText()
        }else if (classNameStr == "com.tencent.mm.ui.LauncherUI") {
            var thread=Thread({
//                try {
                    val inst = Instrumentation()
                    inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_DOWN, 170F,523F, 0))
                    inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_UP, 170F, 523F, 0))
                    LogUtil.e("点击位置")
//                } catch (e: Exception) {
//                    LogUtil.e("Exception when sendPointerSync")
//                }
            })
            thread.start()
        }
    }

//    var temp:AccessibilityNodeInfo? = null

//    /**
//     */
    private fun clearText(){
            Handler().postDelayed({
//                accessibilityNodeInfo.getChild(0).gets
//            val ac = accessibilityNodeInfo?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bd3")//微信6.6.7版本修改为发表
//            if (ac != null && ac.size != 0) {
////                var temp = ac[0].parent
////                if (temp != null) {
////                   OperationUtils.performClickBtn(temp)
////                }
//
//
//
//            }
        }, TEMP.toLong())

//         var list=accessibilityNodeInfo?
//    LogUtil.e("输出"+accessibilityNodeInfo?.parent?.className)
//        performGlobalAction(GLOBAL_ACTION_BACK)

//    if (pasteList != null && pasteList?.size != 0) {
//        var temp = OperationUtils.pasteContent(this, pasteList!![0], listNumber[i++])
//        if (temp) {
//            goSearchFriends()
//        }
//    }
    }
//
//    /**
//     * 点击微信右上角更多
//     */
    private fun modifyData() {
//        val list = accessibilityNodeInfo?.findAccessibilityNodeInfosByText("更多功能按钮")//微信6.6.6版本修改为发表
//        stepOne = OperationUtils.performClickBtn(list)
    }
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
    companion object {
//        var clearList:List<AccessibilityNodeInfo>?=null
        var pasteList:List<AccessibilityNodeInfo>?=null
    }

    private fun clearPasteFriendsNumber() {//com.tencent.mm.plugin.fts.ui.FTSAddFriendUI
        if (!stepThree) {
            Handler().postDelayed({
                stepThree = true
                var  clearList = accessibilityNodeInfo?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hv")//点击清除微信号文本按钮
                if (clearList != null && clearList.size != 0) {
                    OperationUtils.performClickBtn(clearList[0])
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
                if (pasteList == null) {
                    pasteList = accessibilityNodeInfo?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hz")
                }
                if (pasteList != null && pasteList?.size != 0) {
                    var temp = OperationUtils.pasteContent(this, pasteList!![0], listNumber[i++])
                    if (temp) {
                        goSearchFriends()
                    }
                }
            }, TEMP.toLong())
        } else {
            LogUtil.e("已添加" + listNumber.size + "个好友，好友添加完毕！")
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








