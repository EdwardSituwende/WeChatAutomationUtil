package per.edward.wechatautomationutil;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

import per.edward.wechatautomationutil.utils.LogUtil;

/**
 * Created by popfisher on 2017/7/6.
 */

@TargetApi(16)
public class AccessibilitySampleService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
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
                    sendCircleOfFriend();
                    openAlbum();
                }

                if (!sendSuccess && event.getClassName().equals("android.widget.GridView")) {
                    choosePicture(0, 5);
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (!sendSuccess && event.getClassName().equals("com.tencent.mm.plugin.sns.ui.SnsUploadUI")) {//com.tencent.mm.plugin.sns.ui.SnsTimeLineUI
                    inputContentFinish("Hello World");
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
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cyh");
        for (int i = 0; i < accessibilityNodeInfoList.size(); i++) {
            for (int j = startPicIndex; j < picCount; j++) {
                AccessibilityNodeInfo t111 = accessibilityNodeInfoList.get(i).getChild(j);
                List<AccessibilityNodeInfo> list11 = t111.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bnl");
                list11.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

        List<AccessibilityNodeInfo> accessibilityNodeInfoList1111 = accessibilityNodeInfo.findAccessibilityNodeInfosByText("完成(" + picCount + "/9)");//点击确定
        accessibilityNodeInfoList1111.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            }
//        }, 3000);

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
                List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/axk");
                if (accessibilityNodeInfoList != null && accessibilityNodeInfoList.get(0).getChildCount() != 0) {
                    accessibilityNodeInfoList.get(0).getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    LogUtil.e("打开相册成功!");
                }
            }
        }, 1500);
    }


    @Override
    public void onInterrupt() {

    }
}
