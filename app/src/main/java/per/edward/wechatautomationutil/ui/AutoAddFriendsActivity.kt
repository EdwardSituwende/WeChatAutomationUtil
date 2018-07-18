package per.edward.wechatautomationutil.ui

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import per.edward.wechatautomationutil.R
import per.edward.wechatautomationutil.utils.Constant
import per.edward.wechatautomationutil.utils.WxUtils
import java.util.ArrayList

/**
 * 自动添加好友
 */
class AutoAddFriendsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        var et = findViewById<EditText>(R.id.et)
        findViewById<Button>(R.id.btn_sure).setOnClickListener {
            getNumber(et)
        }

        findViewById<Button>(R.id.open_accessibility_setting).setOnClickListener {
            AccessibilityOpenHelperActivity.jumpToSettingPage(baseContext)
        }
    }


    private fun getNumber(et: EditText) {
        var numberStr = et.text.toString()
        var list1 = numberStr.split("\n")
        if (list1.size > 15) {
            Toast.makeText(baseContext, "最多只能添加15个微信号！", Toast.LENGTH_LONG).show()
            return
        }
        val sharedPreferences = getSharedPreferences(Constant.WECHAT_STORAGE, Activity.MODE_MULTI_PROCESS)
        val editor = sharedPreferences.edit()
        editor.putString("content", et.text.toString())
        editor.apply()
        Toast.makeText(baseContext, et.text.toString(), Toast.LENGTH_SHORT).show()
        WxUtils.openWx(this)//打开微信
    }

}