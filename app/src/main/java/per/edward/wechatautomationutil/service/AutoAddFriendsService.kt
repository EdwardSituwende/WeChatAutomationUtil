package per.edward.wechatautomationutil.service

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import per.edward.wechatautomationutil.utils.LogUtil
import per.edward.wechatautomationutil.utils.OperationUtils
import java.util.ArrayList

class AutoAddFriendsService : AccessibilityService() {
    override fun onInterrupt() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val TEMP = 2000
    private  var accessibilityNodeInfo: AccessibilityNodeInfo?=null
    private var stepOne=false
    private var stepTwo=false
    private var stepThree=false
    private var stepFour=false
    private var sendFinish=false
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        accessibilityNodeInfo = rootInActiveWindow
        val eventType = event!!.eventType
        var classNameStr=event.className
        LogUtil.e(eventType.toString() + "             " + Integer.toHexString(eventType) + "         " + event.className)
        if (classNameStr == "com.tencent.mm.ui.LauncherUI") {
            clickRightMore()
        }else if (stepOne&&classNameStr == "android.widget.ListView") {
            clickAddFriendsButton()
        }else if (stepTwo&&classNameStr == "android.widget.FrameLayout") {
            clickSearchFriends()
        }else if (classNameStr == "com.tencent.mm.plugin.fts.ui.FTSAddFriendUI") {
            pasteFriendsNumber()
        }else if (eventType.toString()=="4096") {
            startSearchFriends()
        }else if (!sendFinish&&classNameStr == "com.tencent.mm.plugin.profile.ui.ContactInfoUI") {
            addToContacts()
        }else if (classNameStr == "com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI") {
            sendFinish()
        }else if (sendFinish&&classNameStr == "com.tencent.mm.plugin.profile.ui.ContactInfoUI") {//回到联系人页面点返回
            goBackSearch()
        }
    }

    /**
     * @param step
     */
    private fun checkStep(step: Int):Boolean {
        for (i in 0..1) {
            val booleans = ArrayList<Boolean>()
            booleans.add(true)
            return false
        }
        return true
    }

    /**
     * 点击微信右上角更多
     */
    private fun clickRightMore() {
        val list = accessibilityNodeInfo?.findAccessibilityNodeInfosByText("更多功能按钮")//微信6.6.6版本修改为发表
        stepOne=OperationUtils.performClickBtn(list)
    }

    /**
     * 点击添加好友选项
     */
    private fun clickAddFriendsButton() {
        var ac= accessibilityNodeInfo?.getChild(0)
        if (ac != null && ac.childCount > 2) {
            stepTwo=OperationUtils.performClickBtn(ac.getChild(1))
            stepOne=!stepTwo
        }
    }

    /**
     * 点击搜索好友
     */
    private fun clickSearchFriends() {
//        Handler().postDelayed({
//            val ac = accessibilityNodeInfo?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/j2")//微信6.6.7版本修改为发表
//            if (ac != null && ac.size!=0) {
//                var temp=ac[0]
//                if (temp != null && temp.childCount > 2) {
//                    LogUtil.e(temp.getChild(1).childCount.toString())
////                    stepTwo=performClickBtn(temp.getChild(1))
//                }
//            }
//        }, TEMP.toLong())
    }

    /**
     * 复制好友微信号
     */
    private fun pasteFriendsNumber() {
//32             20         com.tencent.mm.plugin.fts.ui.FTSAddFriendUI
        Handler().postDelayed({
            val list = accessibilityNodeInfo?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hz")
            if (list != null && list.size != 0) {
                OperationUtils.pasteContent(this,list[0],"13427561170")
            }
        }, TEMP.toLong())
    }

    /**
     * 开始搜索好友
     */
    private fun startSearchFriends() {
        Handler().postDelayed({
            val ac = accessibilityNodeInfo?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bcq")
            if (ac != null && ac.size!=0) {
                var ac1= ac[0]
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
        Handler().postDelayed({
            val ac = accessibilityNodeInfo?.findAccessibilityNodeInfosByText("添加到通讯录")
            if (ac != null && ac.size != 0) {
                OperationUtils.performClickBtn(ac[0])
            }
        }, TEMP.toLong())
    }

    /**
     * 发送完成
     */
    private fun sendFinish() {
        Handler().postDelayed({
            val ac = accessibilityNodeInfo?.findAccessibilityNodeInfosByText("发送")
            if (ac != null && ac.size != 0) {
                sendFinish=OperationUtils.performClickBtn(ac[1])
            }
        }, TEMP.toLong())
    }

    /**
     * 返回搜索页面
     */
    private fun goBackSearch() {
        sendFinish=false
        Handler().postDelayed({
            val ac = accessibilityNodeInfo?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hs")
            if (ac != null && ac.size!=0) {
                OperationUtils.performClickBtn(ac)
            }
        }, TEMP.toLong())

    }


}








