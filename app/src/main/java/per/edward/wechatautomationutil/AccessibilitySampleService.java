package per.edward.wechatautomationutil;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

import per.edward.wechatautomationutil.utils.Constant;
import per.edward.wechatautomationutil.utils.LogUtil;

/**
 * Created by Edward on 2018-01-30.
 */
@TargetApi(18)
public class AccessibilitySampleService extends AccessibilityService {

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
        }, 1500);
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
                if (accessibilityNodeInfoList1.size() > 0) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", contentStr);
                    if (clipboard == null) {
                        return;
                    }
                    clipboard.setPrimaryClip(clip);
                    accessibilityNodeInfoList1.get(0).performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                    accessibilityNodeInfoList1.get(0).performAction(AccessibilityNodeInfo.ACTION_PASTE);
                    LogUtil.e("写入内容");
                }

                List<AccessibilityNodeInfo> accessibilityNodeInfoList =
                        accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/h5");//点击发送
                if (accessibilityNodeInfoList == null || accessibilityNodeInfoList.size() == 0) {
                    LogUtil.e("发送朋友圈失败");
                    return;
                }
                accessibilityNodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Toast.makeText(getApplicationContext(), "发送朋友圈成功", Toast.LENGTH_LONG).show();
                flag = true;//标记为已发送
            }
        }, 1500);
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
                List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cyh");
                for (int i = 0; i < accessibilityNodeInfoList.size(); i++) {
                    for (int j = startPicIndex; j < startPicIndex + picCount; j++) {
                        AccessibilityNodeInfo childNodeInfo = accessibilityNodeInfoList.get(i).getChild(j);
                        if (childNodeInfo != null) {
                            List<AccessibilityNodeInfo> childNodeInfoList = childNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bnl");
                            if (childNodeInfoList != null && childNodeInfoList.size() != 0) {
                                childNodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                    }
                }

                List<AccessibilityNodeInfo> finishList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/h5");//点击确定
                if (finishList != null && finishList.size() != 0) {
                    finishList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    LogUtil.e("完成图片选择!");
                }
            }
        }, 1500);
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

                List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/g1");
                if (accessibilityNodeInfoList != null && accessibilityNodeInfoList.size() != 0 &&
                        accessibilityNodeInfoList.get(0) != null && accessibilityNodeInfoList.get(0).getParent() != null) {
                    accessibilityNodeInfoList.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                openAlbum();
            }
        }, 1500);
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

                List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/axk");
                if (accessibilityNodeInfoList != null && accessibilityNodeInfoList.size() != 0 && accessibilityNodeInfoList.get(0).getChildCount() != 0) {
                    accessibilityNodeInfoList.get(0).getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    LogUtil.e("打开相册!");

                    SharedPreferences sharedPreferences = getSharedPreferences(Constant.WECHAT_STORAGE, Activity.MODE_PRIVATE);
                    if (sharedPreferences != null) {
                        int index = sharedPreferences.getInt(Constant.INDEX, 0);
                        int count = sharedPreferences.getInt(Constant.COUNT, 0);
                        choosePicture(index, count);
                    }
                }
            }
        }, 1500);
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
