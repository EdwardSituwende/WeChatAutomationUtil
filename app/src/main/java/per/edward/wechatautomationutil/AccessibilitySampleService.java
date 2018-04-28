package per.edward.wechatautomationutil;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import per.edward.wechatautomationutil.utils.Constant;
import per.edward.wechatautomationutil.utils.LogUtil;

/**
 * Created by Edward on 2018-01-30.
 */
@TargetApi(18)
public class AccessibilitySampleService extends AccessibilityService {
    private final int TEMP = 1500;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        flag = false;
    }

    private AccessibilityNodeInfo accessibilityNodeInfo;

    /**
     * 是否已经发送过朋友圈，true已经发送，false还未发送
     */
    public static boolean flag = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.WECHAT_STORAGE, Activity.MODE_PRIVATE);
        int eventType = event.getEventType();
        LogUtil.e(eventType + "             " + Integer.toHexString(eventType) + "         " + event.getClassName());
        accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                if (!flag && event.getClassName().equals("android.widget.ListView")) {
                    clickCircleOfFriendsBtn();//点击发送朋友圈按钮
                }

                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (event.getClassName().equals("com.tencent.mm.ui.LauncherUI")) {//第一次启动app
                    flag = false;
                    jumpToCircleOfFriends();//进入朋友圈页面
                }

                if (!flag && event.getClassName().equals("com.tencent.mm.plugin.sns.ui.SnsUploadUI")) {
                    String content = sharedPreferences.getString(Constant.CONTENT, "");
                    inputContentFinish(content);//写入要发送的朋友圈内容
                }
                break;
        }
    }

    /**
     * 跳进朋友圈
     */
    private void jumpToCircleOfFriends() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("android:id/list");
                if (accessibilityNodeInfoList != null && accessibilityNodeInfoList.size() != 0) {
                    AccessibilityNodeInfo accessibilityNodeInfo = accessibilityNodeInfoList.get(0);
                    if (accessibilityNodeInfo != null) {
                        accessibilityNodeInfo = accessibilityNodeInfo.getChild(1);
                        if (accessibilityNodeInfo != null) {
                            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }
            }
        }, TEMP);
    }

    /**
     * 写入朋友圈内容
     *
     * @param contentStr
     */
    private void inputContentFinish(final String contentStr) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<AccessibilityNodeInfo> accessibilityNodeInfoList1 = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/dba");
                for (int i = 0; i < accessibilityNodeInfoList1.size(); i++) {
                    AccessibilityNodeInfo accessibilityNodeInfo = accessibilityNodeInfoList1.get(i);
                    if (accessibilityNodeInfo != null) {
                        if (accessibilityNodeInfo.isFocusable() && accessibilityNodeInfo.isClickable() && accessibilityNodeInfo.isEnabled()) {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("text", contentStr);
                            if (clipboard == null) {
                                return;
                            }
                            clipboard.setPrimaryClip(clip);
                            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);

                            List<AccessibilityNodeInfo> list = accessibilityNodeInfo.findAccessibilityNodeInfosByText("发送");//点击发送按钮
                            if (performClickBtn(list)) {
                                flag = true;//标记为已发送
                            }
                        }
                    }
                }
            }
        }, TEMP);
    }

    /**
     * @param accessibilityNodeInfoList
     * @return
     */
    private boolean performClickBtn(List<AccessibilityNodeInfo> accessibilityNodeInfoList) {
        if (accessibilityNodeInfoList != null && accessibilityNodeInfoList.size() != 0) {
            for (int i = 0; i < accessibilityNodeInfoList.size(); i++) {
                AccessibilityNodeInfo accessibilityNodeInfo = accessibilityNodeInfoList.get(i);
                if (accessibilityNodeInfo != null) {
                    if (accessibilityNodeInfo.isClickable() && accessibilityNodeInfo.isEnabled()) {
                        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        return true;
                    }
                }
            }
        }
        return false;
//        LogUtil.e("发送朋友圈失败");
    }


    /**
     * 选择图片
     *
     * @param startPicIndex 从第startPicIndex张开始选
     * @param picCount      总共选picCount张
     */
    private void choosePicture(final int startPicIndex, final int picCount) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (accessibilityNodeInfo == null) {
                    return;
                }
                List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cyh");
                if (accessibilityNodeInfoList == null) {
                    return;
                }
                for (int i = 0; i < accessibilityNodeInfoList.size(); i++) {
                    for (int j = startPicIndex; j < startPicIndex + picCount; j++) {
                        AccessibilityNodeInfo childNodeInfo = accessibilityNodeInfoList.get(i).getChild(j);
                        if (childNodeInfo != null) {
                            List<AccessibilityNodeInfo> childNodeInfoList = childNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bnl");
                            performClickBtn(childNodeInfoList);
                        }
                    }
                }

                List<AccessibilityNodeInfo> finishList = accessibilityNodeInfo.findAccessibilityNodeInfosByText("完成(" + picCount + "/9)");//点击确定
                performClickBtn(finishList);
            }
        }, TEMP);
    }


    /**
     * 点击发送朋友圈按钮
     */
    private void clickCircleOfFriendsBtn() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (accessibilityNodeInfo == null) {
                    return;
                }

                List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText("更多功能按钮");
                performClickBtn(accessibilityNodeInfoList);
                openAlbum();
            }
        }, TEMP);
    }


    /**
     * 打开相册
     */
    private void openAlbum() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (accessibilityNodeInfo == null) {
                    return;
                }

                List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText("从相册选择");
                if (traverseNode(accessibilityNodeInfoList)) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Constant.WECHAT_STORAGE, Activity.MODE_PRIVATE);
                    if (sharedPreferences != null) {
                        int index = sharedPreferences.getInt(Constant.INDEX, 0);
                        int count = sharedPreferences.getInt(Constant.COUNT, 0);
                        choosePicture(index, count);
                    }
                }
            }
        }, TEMP);
    }

    private boolean traverseNode(List<AccessibilityNodeInfo> accessibilityNodeInfoList) {
        if (accessibilityNodeInfoList != null && accessibilityNodeInfoList.size() != 0) {
            AccessibilityNodeInfo accessibilityNodeInfo = accessibilityNodeInfoList.get(0).getParent();
            if (accessibilityNodeInfo != null && accessibilityNodeInfo.getChildCount() != 0) {
                accessibilityNodeInfo = accessibilityNodeInfo.getChild(0);
                if (accessibilityNodeInfo != null) {
                    accessibilityNodeInfo = accessibilityNodeInfo.getParent();
                    if (accessibilityNodeInfo != null) {
                        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);//点击从相册中选择
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public void onInterrupt() {

    }


    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("服务被杀死!");
    }
}
