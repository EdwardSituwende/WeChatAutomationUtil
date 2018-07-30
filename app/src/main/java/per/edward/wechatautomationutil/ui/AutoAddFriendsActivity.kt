package per.edward.wechatautomationutil.ui

import android.Manifest
import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import per.edward.wechatautomationutil.ConstantData
import per.edward.wechatautomationutil.R
import per.edward.wechatautomationutil.utils.Constant
import per.edward.wechatautomationutil.utils.FileUtils
import per.edward.wechatautomationutil.utils.WxUtils
import java.io.*
import java.util.ArrayList


/**
 * 自动添加好友
 */
class AutoAddFriendsActivity : AppCompatActivity() {
    var btn:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        var et = findViewById<EditText>(R.id.et)
        btn= findViewById<Button>(R.id.btn_sure)
        btn?.setOnClickListener {
            getNumber(et)
        }

        findViewById<Button>(R.id.open_accessibility_setting).setOnClickListener {
            AccessibilityOpenHelperActivity.jumpToSettingPage(baseContext)
        }

        findViewById<Button>(R.id.btn_loading_txt).setOnClickListener {
            loadFile()
        }
    }


    private fun loadFile() {
        requestPermission()
        var list=Environment.getExternalStorageDirectory().listFiles()
        if (list == null) {
            Toast.makeText(this,"应用读写权限未开启",Toast.LENGTH_LONG).show()
            return
        }
        var filePath: String? =null
        for (i in list) {//寻找指定文件
            if (i.name.contains("my_folder.txt")) {
                filePath= i.path
                break
            }
        }

        if (filePath == null) {
            Toast.makeText(this,"没有找my_folder.txt文件",Toast.LENGTH_LONG).show()
            return
        }

        var file = File(filePath)
        if (file.isFile) {
           var content= FileUtils.readTxtFile(file)
            if (!TextUtils.isEmpty(content)) {
                btn?.isEnabled=true
                Toast.makeText(this,"加载成功",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"my_folder.txt文件内容为空",Toast.LENGTH_LONG).show()
            }
        }
    }



    /**
     * 请求权限
     */
    private fun requestPermission() {
//        var code=checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        //TODO 待完成
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