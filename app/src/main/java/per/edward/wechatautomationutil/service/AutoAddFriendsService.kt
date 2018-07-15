package per.edward.wechatautomationutil.service

import android.accessibilityservice.AccessibilityService
import android.app.Activity
import android.os.Handler
import android.view.accessibility.AccessibilityEvent
import per.edward.wechatautomationutil.utils.Constant
import per.edward.wechatautomationutil.utils.LogUtil

class AutoAddFriendsService : AccessibilityService() {
    override fun onInterrupt() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
//        val sharedPreferences = getSharedPreferences(Constant.WECHAT_STORAGE, Activity.MODE_MULTI_PROCESS)
        val eventType = event!!.eventType
        LogUtil.e(eventType.toString() + "             " + Integer.toHexString(eventType) + "         " + event.getClassName())
//        accessibilityNodeInfo = rootInActiveWindow
//        when (eventType) {
//            AccessibilityEvent.TYPE_VIEW_SCROLLED -> if (!flag && event.getClassName() == "android.widget.ListView") {
//                clickCircleOfFriendsBtn()//点击发送朋友圈按钮
//            }
//            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
//
//                if (event.getClassName() == "com.tencent.mm.ui.LauncherUI") {//第一次启动app
//                    flag = false
//                    jumpToCircleOfFriends()//进入朋友圈页面
//                }
//
//                if (!flag && event.getClassName() == "com.tencent.mm.plugin.sns.ui.SnsUploadUI") {
//                    val content = sharedPreferences!!.getString(Constant.CONTENT, "")
//                    inputContentFinish(content)//写入要发送的朋友圈内容
//                }
//
//                if (!flag && event.getClassName() == "com.tencent.mm.plugin.gallery.ui.AlbumPreviewUI") {
//                    Handler().postDelayed({
//                        if (sharedPreferences != null) {
//                            val index = sharedPreferences.getInt(Constant.INDEX, 0)
//                            val count = sharedPreferences.getInt(Constant.COUNT, 0)
//                            choosePicture(index, count)
//                        }
//                    }, TEMP.toLong())
//                }
//            }
//        }
    }

    /**
     * 点击微信右上角更多
     */
    fun clickRightMore() {

    }

    /**
     * 点击添加好友选项
     */
    fun clickAddFriends() {

    }

    /**
     * 点击搜索好友
     */
    fun clickSearchFriends() {

    }

    /**
     * 复制好友微信号
     */
    fun pasteFriendsNumber() {

    }

    /**
     * 开始搜索好友
     */
    fun startSearchFriends() {

    }

    /**
     * 添加好友到联系人列表
     */
    fun addToContacts() {

    }

    /**
     * 发送完成
     */
    fun sendFinish() {

    }


}








