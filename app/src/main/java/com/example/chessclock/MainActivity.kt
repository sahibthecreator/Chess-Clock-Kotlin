package com.example.chessclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var player1Time: Long = 10000
    var player2Time: Long = 10000
    var isPlayer1Turn = false
    var isPaused = true
    private var currentTimer: CountDownTimer? = null
    var player1TimerView : TextView? = null
    var player2TimerView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        player1TimerView = findViewById<TextView>(R.id.player1TimerView)
        player2TimerView = findViewById<TextView>(R.id.player2TimerView)

        findViewById<Button>(R.id.changePlayer1Time).setOnClickListener{
            findViewById<LinearLayout>(R.id.timePickerPlayer1).visibility = View.VISIBLE
        }

        player1TimerView!!.setText(String.format("%02d:%02d", player1Time / 1000 / 60, (player1Time / 1000) % 60))
        player2TimerView!!.setText(String.format("%02d:%02d", player2Time / 1000 / 60, (player2Time / 1000) % 60))

        val screenLayout = findViewById<LinearLayout>(R.id.screenLayout)
        screenLayout.setOnClickListener {
            isPlayer1Turn = !isPlayer1Turn
            startTimer()
        }

    }

    private fun startTimer() {
        currentTimer?.cancel() // to stop previous players timer

        val remainingTime = if (isPlayer1Turn) player1Time else player2Time
        val timerTextView : TextView = if (isPlayer1Turn) player1TimerView!! else player2TimerView!!


        currentTimer = object : CountDownTimer(remainingTime, 100) {
            var lastTickTime = System.currentTimeMillis()

            override fun onTick(millisUntilFinished: Long) {
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - lastTickTime

                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60

                val formattedTime = String.format("%02d:%02d", minutes, seconds)
                updatePlayerTime(formattedTime);
                //timerTextView.text = formattedTime
                if (isPlayer1Turn)
                    player1Time -= elapsedTime
                else
                    player2Time -= elapsedTime
            }

            override fun onFinish() {
                timerTextView.setText("Out of time!")
            }
        }.start()
    }

    // Function to pause the timer
    fun pauseTimer() {
        isPaused = true
    }



    private fun resetTimer() {
        player1Time = 10000
        player2Time = 10000
        isPaused = true
        isPlayer1Turn = false
    }

    fun updatePlayerTime(formattedTime: String) {
        if(isPlayer1Turn){
            var player1TimerView = findViewById<TextView>(R.id.player1TimerView)
            player1TimerView.text = formattedTime;
        }else{
            var player2TimerView = findViewById<TextView>(R.id.player2TimerView)
            player2TimerView.text = formattedTime;
        }
    }

}