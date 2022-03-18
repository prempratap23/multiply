package com.example.glideimage

import android.graphics.*
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream
import java.net.URL


import java.io.FileNotFoundException


class MainActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val image=findViewById<ImageView>(R.id.imageView)

        val url = "https://media.gamtoss.com/web/uploads/images/team/-1646915230.png"

        val task=BackgroundTask(url)

        val icon2 = BitmapFactory.decodeResource(this@MainActivity.resources, R.drawable.ic_login_bg_round)
        image.setImageBitmap(processingBitmap( task.execute().get(),icon2))

    }

    private fun processingBitmap(srcBmp:Bitmap, destBmp:Bitmap): Bitmap? {
        var newBitmap: Bitmap? = null
        try {
            val w: Int = if (srcBmp.width >= destBmp.width) {
                srcBmp.width
            } else {
                destBmp.width
            }
            val h: Int = if (srcBmp.height >= destBmp.height) {
                srcBmp.height
            } else {
                destBmp.height
            }

            var config = srcBmp.config
            if (config == null) {
                config = Bitmap.Config.ARGB_8888
            }

            val paintDst = Paint()
            val paint = Paint()
            val fullRect by lazy { Rect(0, 0, w, h) }
            val selectedMode: PorterDuff.Mode = PorterDuff.Mode.MULTIPLY
            paint.xfermode = PorterDuffXfermode(selectedMode)

            newBitmap = Bitmap.createBitmap(w, h, config)
            val newCanvas = Canvas(newBitmap)

            newCanvas.drawBitmap(srcBmp, null, fullRect, paintDst)
            newCanvas.drawBitmap(destBmp, null, fullRect, paint)
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return newBitmap
    }

    private class BackgroundTask(val url:String)  : AsyncTask<String, Bitmap?, Bitmap>() {

        override fun onPostExecute(result: Bitmap) {
            myMethod(result)
        }

        override fun doInBackground(vararg p0: String): Bitmap {
            var theBitmap:Bitmap?=null
            try{
                val stringUrl: String = url
                val inputStream: InputStream = URL(stringUrl).openStream()
                theBitmap = BitmapFactory.decodeStream(inputStream) }
            catch (e: Exception) {
                Log.e("Exception :", e.message!!)
            }
            catch (e: InterruptedException) {
                Log.e("doInBackground :", e.message!!)
            }
            return theBitmap!!

        }

        fun myMethod(myValue: Bitmap): Bitmap {
            return myValue
        }

    }

//    http://android-er.blogspot.com/2013/08/merge-images-with-porterduffxfermode.html
    //    fun getBitMap(){
//        object : AsyncTask<String, Void?, Bitmap>() {
//
//            override fun onPostExecute(dummy: Bitmap) {
//                if (null != dummy) {
//
//                    image.setImageBitmap(ProcessingBitmap(dummy,icon2))
//                }
//            }
//
//            override fun doInBackground(vararg p0: String): Bitmap {
//                var theBitmap:Bitmap?=null
//                try{
//                    val stringUrl: String = url
//                    val inputStream: InputStream = URL(stringUrl).openStream()
//                    theBitmap = BitmapFactory.decodeStream(inputStream) }
//                catch (e: Exception) {
//                    Log.e("Exception :", e.message!!)
//                }
//                catch (e: InterruptedException) {
//                    Log.e("doInBackground :", e.message!!)
//                }
//                return theBitmap!!
//            }
//
//        }.execute(url)
//    }


}