package per.edward.wechatautomationutil.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import per.edward.wechatautomationutil.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.btn_send_circle_of_friends).setOnClickListener {
            startActivity(Intent(baseContext, AutoSendCircleOfFriendsActivity::class.java))
        }
//
        findViewById<Button>(R.id.btn_add_friends).setOnClickListener {
            startActivity(Intent(baseContext, AutoAddFriendsActivity::class.java))
        }
    }
}