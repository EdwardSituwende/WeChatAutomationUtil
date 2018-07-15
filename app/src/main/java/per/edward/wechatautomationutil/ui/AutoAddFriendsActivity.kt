package per.edward.wechatautomationutil.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import per.edward.wechatautomationutil.R
import per.edward.wechatautomationutil.utils.WxUtils

/**
 * 自动添加好友
 */
class AutoAddFriendsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        var et= findViewById<EditText>(R.id.et)
        findViewById<Button>(R.id.btn_sure).setOnClickListener {
            getNumber(et)
        }
    }

   private fun getNumber(et: EditText) {
        var list=et.text.split("\n")
        Toast.makeText(baseContext, list[1],Toast.LENGTH_SHORT).show()

       WxUtils.openWx(this)//打开微信
    }

}