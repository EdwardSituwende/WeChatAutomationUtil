package per.edward.wechatautomationutil.service

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import per.edward.wechatautomationutil.utils.LogUtil
import java.util.ArrayList

class AutoAddFriendsService : AccessibilityService() {
    override fun onInterrupt() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val TEMP = 2000
    private  var accessibilityNodeInfo: AccessibilityNodeInfo?=null
    private var stepOne=false
    private var stepTwo=false
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        accessibilityNodeInfo = rootInActiveWindow
        val eventType = event!!.eventType
        var classNameStr=event.className
        LogUtil.e(eventType.toString() + "             " + Integer.toHexString(eventType) + "         " + event.className)
        if (classNameStr == "com.tencent.mm.ui.LauncherUI") {
            clickRightMore()
        }else if (stepOne&&classNameStr == "android.widget.ListView") {
            clickAddFriendsButton()
        }else if (stepTwo&&classNameStr == "com.tencent.mm.plugin.subapp.ui.pluginapp.AddMoreFriendsUI") {
            clickSearchFriends()
        }else if (classNameStr == "com.tencent.mm.plugin.fts.ui.FTSAddFriendUI") {

        }else if (classNameStr == "android.widget.EditText") {

        }else if (classNameStr == "com.tencent.mm.plugin.profile.ui.ContactInfoUI") {

        }else if (classNameStr == "com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI") {

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
        stepOne=performClickBtn(list)
    }

    /**
     * 点击添加好友选项
     */
    private fun clickAddFriendsButton() {
        var ac= accessibilityNodeInfo?.getChild(0)
        if (ac != null && ac.childCount > 2) {
            stepTwo=performClickBtn(ac.getChild(1))
            stepOne=!stepTwo
        }
    }

    /**
     * 点击搜索好友
     */
    private fun clickSearchFriends() {
        Handler().postDelayed({
            val ac = accessibilityNodeInfo?.findAccessibilityNodeInfosByViewId("android:id/list")//微信6.6.7版本修改为发表
            if (ac != null && ac.size!=0) {
                var temp=ac[0]
                if (temp != null && temp.childCount > 2) {
                    stepTwo=performClickBtn(temp.getChild(1))
                }
            }
        }, TEMP.toLong())
    }

    /**
     * 复制好友微信号
     */
    fun pasteFriendsNumber() {
//32             20         com.tencent.mm.plugin.fts.ui.FTSAddFriendUI
    }

    /**
     * 开始搜索好友
     */
    fun startSearchFriends() {
//16             10         android.widget.EditText
    }

    /**
     * 添加好友到联系人列表
     */
    fun addToContacts() {
//32             20         com.tencent.mm.plugin.profile.ui.ContactInfoUI
    }

    /**
     * 发送完成
     */
    fun sendFinish() {
//32             20         com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI
    }

    /**
     * 返回搜索页面
     */
    fun gobackSearch() {

    }

    /**
     * @param accessibilityNodeInfoList
     * @return
     */
    private fun performClickBtn(accessibilityNodeInfoList: List<AccessibilityNodeInfo>?): Boolean {
        if (accessibilityNodeInfoList != null && accessibilityNodeInfoList.size != 0) {
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

    private fun performClickBtn(accessibilityNodeInfo: AccessibilityNodeInfo): Boolean {
        if (accessibilityNodeInfo.isClickable && accessibilityNodeInfo.isEnabled) {
            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            return true
        }
        return false
    }
}








