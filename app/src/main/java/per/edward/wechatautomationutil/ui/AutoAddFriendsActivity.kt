package per.edward.wechatautomationutil.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_friends.*
import per.edward.wechatautomationutil.R
import per.edward.wechatautomationutil.utils.*
import java.io.*


/**
 * 自动添加好友
 */
class AutoAddFriendsActivity : AppCompatActivity() {
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)



        initCallback()
    }

    override fun onResume() {
        super.onResume()
        loadFriendsIndex()
        Log.e("输出","123123")
    }

    private fun loadFriendsIndex() {
        sharedPreferences = getSharedPreferences(Constant.FILE_FRIEND_INDEX, Activity.MODE_MULTI_PROCESS)
        if (sharedPreferences != null) {
            var indexStr = sharedPreferences!!.getInt(Constant.GET_FRIENDS, 0)
            btn_remove_friends_index.text = "删除下标（当前下标：$indexStr)"
        }
    }

    private fun initCallback() {
        btn_remove_friends_index.setOnClickListener {
            if (sharedPreferences != null) {
                var edit = sharedPreferences!!.edit()
                edit.clear()
                edit.apply()
                Toast.makeText(baseContext, "删除成功", Toast.LENGTH_LONG).show()
                loadFriendsIndex()//重新加载数据
            }
        }

        btn_sure.setOnClickListener {
            getNumber(et)
        }

        open_accessibility_setting.setOnClickListener {
            AccessibilityOpenHelperActivity.jumpToSettingPage(baseContext)
        }

        btn_loading_txt.setOnClickListener {
            requestStoragePermission()
        }
    }


    private fun loadFile() {
        var list = Environment.getExternalStorageDirectory().listFiles()
        if (list == null) {
            Toast.makeText(this, "应用读写权限未开启", Toast.LENGTH_LONG).show()
            return
        }
        var filePath: String? = null
        for (i in list) {//寻找指定文件
            if (i.name.contains(Constant.WX_NUMBER_FILE_NAME)) {
                filePath = i.path
                break
            }
        }

        if (TextUtils.isEmpty(filePath)) {
            Toast.makeText(baseContext, "没有找到文件", Toast.LENGTH_LONG).show()
            return
        }

        var file = File(filePath)
        if (file == null) {
            Toast.makeText(this, "没有找my_folder.txt文件", Toast.LENGTH_LONG).show()
            return
        }

        if (file.isFile) {
            var content = FileUtils.readTxtFile(file)
            if (!TextUtils.isEmpty(content)) {
                btn_sure.isEnabled = true
                LogUtil.e("输出：$content")//输出内容方便调试
                Toast.makeText(this, "加载成功", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "my_folder.txt文件内容为空", Toast.LENGTH_LONG).show()
            }
        }
    }


    /**
     * 请求权限
     */
    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            val writeSDCardPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            if (writeSDCardPermission == PackageManager.PERMISSION_GRANTED) {
                loadFile()
            } else {
                var strings: Array<String> = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(strings, 1)
            }
        } else {
            loadFile()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (permissions.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "读取权限授权失败，请允许存储权限后再试", Toast.LENGTH_SHORT).show()
            } else {
                loadFile()
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1) {
//            Log.e("输出", "测试")
//        }
//    }

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
        WxUtils.openWx(this)//打开微信
    }


}