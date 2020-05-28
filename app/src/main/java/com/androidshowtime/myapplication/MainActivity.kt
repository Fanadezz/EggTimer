package com.androidshowtime.myapplication

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    var isCounterActive = false
    var duration = 0
    lateinit var mediaPlayer: MediaPlayer
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        seekBar.progress = 30
        textView.text = "00:" + seekBar.progress
        duration = seekBar.progress

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                duration = progress
                setTimerOnTextView(duration)


            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })







        button.setOnClickListener {
            if (isCounterActive) {
                resetTimer()
            } else {

                button.text = resources.getString(R.string.stop_button_text)
                isCounterActive = true
                seekBar.isEnabled = false

                timer = object : CountDownTimer((duration * 1000L), 1000) {
                    override fun onFinish() {
                        mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.aquarium)
                        mediaPlayer.start()

                        resetTimer()
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        val sec = milliToSec(millisUntilFinished)
                        setTimerOnTextView(sec)

                        Timber.i("Seconds Remaining $sec")
                    }
                }.start()
            }
        }


    }

    private fun resetTimer() {
        button.text = resources.getString(R.string.start_button_text)
        seekBar.progress = 30
        textView.text = "00:" + seekBar.progress
        duration = seekBar.progress
        isCounterActive = false
        timer.cancel()
        seekBar.isEnabled = true

    }


    fun setTimerOnTextView(sec: Int) {


        val minutes = sec / 60
        val seconds = sec % 60

        textView.text = String.format("%02d:%02d", minutes, seconds)
    }


    fun milliToSec(milli: Long): Int = (milli / 1000).toInt()

}
