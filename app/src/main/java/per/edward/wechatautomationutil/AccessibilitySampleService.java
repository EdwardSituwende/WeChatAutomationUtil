package per.edward.wechatautomationutil;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

import per.edward.wechatautomationutil.utils.Constant;
import per.edward.wechatautomationutil.utils.LogUtil;

/**
 * Created by popfisher on 2017/7/6.
 */

@TargetApi(16)
public class AccessibilitySampleService extends AccessibilityService {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        sharedPreferences = getSharedPreferences(Constant.WECHAT_STORAGE, Activity.MODE_PRIVATE);
    }

    private AccessibilityNodeInfo accessibilityNodeInfo;

    public static boolean sendSuccess = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        int eventType = event.getEventType();
        Log.e("事件类型:", Integer.toHexString(eventType) + "         " + event.getClassName());
        accessibilityNodeInfo = getRootInActiveWindow();
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                if (!sendSuccess && event.getClassName().equals("android.widget.ListView")) {
                    if (accessibilityNodeInfo == null) {
                        return;
                    }
                    sendCircleOfFriend();
                    openAlbum();
                }

                if (!sendSuccess && event.getClassName().equals("android.widget.GridView")) {
                    if (sharedPreferences != null) {
                        int index = sharedPreferences.getInt(Constant.INDEX, 0);
                        int count = sharedPreferences.getInt(Constant.COUNT, 0);
                        Log.e("输出", index + "      " + count);
                        choosePicture(index, count);
                    } else {
                        Log.e("TYPE_VIEW_SCROLLED", "sharedPreferences is null!");
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (accessibilityNodeInfo == null) {
                    return;
                }

                if (!sendSuccess && event.getClassName().equals("com.tencent.mm.plugin.sns.ui.SnsUploadUI")) {//com.tencent.mm.plugin.sns.ui.SnsTimeLineUI
                    if (sharedPreferences != null) {
                        String content = sharedPreferences.getString(Constant.CONTENT, "");
                    inputContentFinish("哈哈");
                    }else{
                        Log.e("TYPE_WINDOW_STATE_CHANGED", "sharedPreferences is null!");
                    }
                }
                break;
        }
    }

    private void inputContentFinish(String contentStr) {
        List<AccessibilityNodeInfo> accessibilityNodeInfoList1 = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/dba");
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, contentStr);
        if (accessibilityNodeInfoList1.size() > 0) {
            accessibilityNodeInfoList1.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            LogUtil.e("成功写入内容");
        }

        List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/h5");//点击发送
        if (accessibilityNodeInfoList == null || accessibilityNodeInfoList.size() == 0) {
            LogUtil.e("发送朋友圈失败");
        }

        if (accessibilityNodeInfoList.size() > 0) {
            accessibilityNodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Toast.makeText(getApplicationContext(), "发送朋友圈成功", Toast.LENGTH_LONG).show();
            sendSuccess = true;
        }
    }


    /**
     * 选择图片
     *
     * @param startPicIndex 从第startPicIndex张开始选
     * @param picCount      总共选picCount张
     */
    private void choosePicture(final int startPicIndex, final int picCount) {
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cyh");
        for (int i = 0; i < accessibilityNodeInfoList.size(); i++) {
            for (int j = startPicIndex; j < picCount; j++) {
                AccessibilityNodeInfo childNodeInfo = accessibilityNodeInfoList.get(i).getChild(j);
                if (childNodeInfo != null) {
                    List<AccessibilityNodeInfo> childNodeInfoList = childNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bnl");
                    if (childNodeInfoList != null && childNodeInfoList.size() != 0) {
                        childNodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
        }

        List<AccessibilityNodeInfo> finishList = accessibilityNodeInfo.findAccessibilityNodeInfosByText("完成(" + picCount + "/9)");//点击确定
        if (finishList != null && finishList.size() != 0) {
            finishList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Toast.makeText(getBaseContext(), "选择图片", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 发送朋友圈
     */
    private void sendCircleOfFriend() {
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/go");
        for (int i = 0; i < accessibilityNodeInfoList.size(); i++) {
            for (int j = 0; j < accessibilityNodeInfoList.get(i).getChildCount(); j++) {
                AccessibilityNodeInfo temp = accessibilityNodeInfoList.get(i).getChild(j);
                if (temp.getContentDescription() != null) {
                    String t1 = temp.getContentDescription().toString();
                    if (t1.equals("更多功能按钮")) {
                        accessibilityNodeInfoList.get(i).getChild(j).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
        }
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
                    LogUtil.e("打开相册成功!");
                }
            }
        }, 1500);
    }


    @Override
    public void onInterrupt() {

    }

    public static void main(String[] args) {
        System.out.print("测试");
    }
}
