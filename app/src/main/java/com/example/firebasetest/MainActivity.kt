package com.example.firebasetest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    private lateinit var mTestNormalService:TestNormalService
    private var mBound = false
    private lateinit var mMessenger:Messenger

    private lateinit var mAuth:FirebaseAuth
    private val mConnection = object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            val binder = service as TestNormalService.LocalBinder
//            mTestNormalService = binder.getService()
            mMessenger = Messenger(service)
            mBound = true
            Toast.makeText(applicationContext, "サービスにバインドしました", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
            Toast.makeText(applicationContext, "サービスのバインドを解除しました", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showToast(){
        val num = mTestNormalService.getRandomInt()
        Toast.makeText(applicationContext, "Number from service is $num", Toast.LENGTH_SHORT).show()
    }

    private fun sendMessage(){
        val message = Message.obtain(null, SAY_HELLO, 0,0)
        mMessenger.send(message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        //ユーザー登録処理
        register_button.setOnClickListener {
            val email = login_email_text.text.toString()
            val password = login_password_text.text.toString()
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(applicationContext, "ユーザー登録完了", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext, "ユーザー登録失敗", Toast.LENGTH_SHORT).show()
                }
            }
        }

        login_button.setOnClickListener {
            val email = login_email_text.text.toString()
            val password = login_password_text.text.toString()
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(applicationContext, "ログイン成功", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext, "ログイン失敗", Toast.LENGTH_SHORT).show()
                }
            }
        }

        bind_button.setOnClickListener {
            Intent(applicationContext, MessengerService::class.java).also {intent ->
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
            }
        }

        unbind_button.setOnClickListener {
            if(mBound){
                unbindService(mConnection)
                mBound = false
            }
        }

        service_button.setOnClickListener {
            sendMessage()
        }


        //サービス開始
//        val intent = Intent(applicationContext, TestService::class.java)
//        val intent = Intent(applicationContext, TestServiceWithNormalService::class.java)
//        startService(intent)
    }

    override fun onStop() {
        super.onStop()
        unbindService(mConnection)
        mBound = false
    }
}
