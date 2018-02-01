package per.edward.wechatautomationutil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.OutputStream;

import per.edward.wechatautomationutil.utils.Constant;

public class MainActivity extends AppCompatActivity {
    EditText edit, editIndex, editCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        edit = findViewById(R.id.edit);
        editIndex = findViewById(R.id.edit_index);
        editCount = findViewById(R.id.edit_count);

        findViewById(R.id.open_accessibility_setting).setOnClickListener(clickListener);
        findViewById(R.id.btn_save).setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.open_accessibility_setting:
                    OpenAccessibilitySettingHelper.jumpToSettingPage(getBaseContext());
                    break;
                case R.id.btn_save:
                    saveData();
                    break;
            }
        }
    };

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.WECHAT_STORAGE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.CONTENT, edit.getText().toString());
        editor.putInt(Constant.INDEX, Integer.valueOf(editIndex.getText().toString()));
        editor.putInt(Constant.COUNT, Integer.valueOf(editCount.getText().toString()));
        if (editor.commit()) {
            Toast.makeText(getBaseContext(), "保存成功", Toast.LENGTH_LONG).show();
            startCountTimer();
            PackageManager packageManager = getBaseContext().getPackageManager();
            Intent it= packageManager.getLaunchIntentForPackage("com.tencent.mm");
            startActivity(it);
        } else {
            Toast.makeText(getBaseContext(), "保存失败", Toast.LENGTH_LONG).show();
        }
    }

    private void fun(String cmd) {
        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void startCountTimer() {
        //点击底部tab按钮坐标 550 1674
        //点击朋友圈坐标196 300
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//               fun("input tap 550 1674");
                try {
                    // 申请获取root权限，这一步很重要，不然会没有作用
                    Process process = Runtime.getRuntime().exec("su");
                    // 获取输出流
                    OutputStream outputStream = process.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(
                            outputStream);
                    dataOutputStream.writeBytes("input tap 550 1674");
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    outputStream.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }, 3000);



    }

}
