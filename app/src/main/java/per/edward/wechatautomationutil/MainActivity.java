package per.edward.wechatautomationutil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import per.edward.wechatautomationutil.utils.Constant;

/**
 * 使用条件
 * 1、Android设备必须安装微信app
 * 2、Android设备版本必须大于18
 * 3、自动化过程中请勿操作Android设备屏幕!!!
 * <p>
 * Created by Edward on 2018-03-15.
 */
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

    public boolean checkParams() {
        if (TextUtils.isEmpty(editIndex.getText().toString())) {
            Toast.makeText(getBaseContext(), "起始下标不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(editCount.getText().toString())) {
            Toast.makeText(getBaseContext(), "图片总数不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Integer.valueOf(editCount.getText().toString()) > 9) {
            Toast.makeText(getBaseContext(), "图片总数不能超过9张", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveData() {
        if (!checkParams()) {
            return;
        }

        int index = Integer.valueOf(editIndex.getText().toString());
        int count = Integer.valueOf(editCount.getText().toString());

        SharedPreferences sharedPreferences = getSharedPreferences(Constant.WECHAT_STORAGE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.CONTENT, edit.getText().toString());
        editor.putInt(Constant.INDEX, index);
        editor.putInt(Constant.COUNT, count);
        if (editor.commit()) {
            AccessibilitySampleService.flag = false;
            Toast.makeText(getBaseContext(), "保存成功"+AccessibilitySampleService.flag, Toast.LENGTH_LONG).show();
            openWeChatApplication();//打开微信应用
        } else {
            Toast.makeText(getBaseContext(), "保存失败", Toast.LENGTH_LONG).show();
        }
    }

    private void openWeChatApplication() {
        PackageManager packageManager = getBaseContext().getPackageManager();
        Intent it = packageManager.getLaunchIntentForPackage("com.tencent.mm");
        startActivity(it);
    }
}
