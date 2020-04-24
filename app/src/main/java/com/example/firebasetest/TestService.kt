package com.example.firebasetest

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.*
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.util.Log
import android.widget.Toast
import java.util.*

class TestNormalService: Service(){

    private val mBinder = LocalBinder()
    private val mRandom = Random()

    public fun getRandomInt():Int {
        return mRandom.nextInt(100)
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    inner class LocalBinder: Binder(){
        fun getService(): TestNormalService = this@TestNormalService
    }
}

const val SAY_HELLO = 0
class MessengerService: Service(){

    private lateinit var mSendToClientMessenger:Messenger

    inner class localHandler(
        val context:Context
    ):Handler(){
        override fun handleMessage(msg: Message) {
            if(msg.replyTo != null){
                mSendToClientMessenger = msg.replyTo
                Message.obtain().also{
                    it.what = 1
                    try{
                        Thread.sleep(5000)
                    }catch (ex:InterruptedException){

                    }
                    mSendToClientMessenger.send(it)
                }
            }
            when(msg.what){
                SAY_HELLO ->{
                    Log.d("akito", "HELLO")
                    Toast.makeText(context.applicationContext, "HELLO", Toast.LENGTH_SHORT).show()
                }
                else ->
                    super.handleMessage(msg)
            }

            super.handleMessage(msg)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        val messenger = Messenger(localHandler(this))
        return messenger.binder
    }
}

class TestService: IntentService("testService"){

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "TestService開始", Toast.LENGTH_SHORT).show()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        try{
            Thread.sleep(5000)
        }catch (ex:InterruptedException){
            Thread.currentThread().isInterrupted
        }
    }

    override fun onDestroy() {
        Toast.makeText(this, "TestService終了", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }
}

class TestServiceWithNormalService: Service(){
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper):Handler(looper){

        override fun handleMessage(msg: Message) {
            try{
                Thread.sleep(5000)
            }catch (ex:InterruptedException){
                Thread.currentThread().isInterrupted
            }
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate(){
        HandlerThread("ServiceStartArguments", THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "TestService開始", Toast.LENGTH_SHORT).show()
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "TestService終了", Toast.LENGTH_SHORT).show()
    }
}

class AidlService: Service(){

    private val mBinder = object: IMyAidlInterface.Stub(){
        override fun getProcessNumber(): Int {
            return 10
        }

        override fun getRect(bundle: Bundle){
            bundle.classLoader = classLoader
            val rect = bundle.getParcelable<MyRect>("MyRect")
            Toast.makeText(applicationContext, "rect's top = ${rect.top}", Toast.LENGTH_SHORT)
        }

        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }
}