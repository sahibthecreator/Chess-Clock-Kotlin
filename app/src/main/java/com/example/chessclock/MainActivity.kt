package com.example.chessclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

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

        findViewById<ConstraintLayout>(R.id.timeSettingsMenu).visibility = View.GONE
        player1TimerView = findViewById<TextView>(R.id.player1TimerView)
        player2TimerView = findViewById<TextView>(R.id.player2TimerView)

        findViewById<Button>(R.id.changePlayer1Time).setOnClickListener{
            findViewById<ConstraintLayout>(R.id.timeSettingsMenu).visibility = View.VISIBLE
        }
        findViewById<Button>(R.id.saveTimeBtn).setOnClickListener{
            findViewById<ConstraintLayout>(R.id.timeSettingsMenu).visibility = View.GONE
        }
        findViewById<ImageView>(R.id.restartBtn).setOnClickListener{
            resetGame()
        }
        findViewById<ImageView>(R.id.pauseBtn).setOnClickListener{
            if(isPaused)
                resumeGame()
            else
                pauseGame()
        }


        player1TimerView!!.setText(String.format("%02d:%02d", player1Time / 1000 / 60, (player1Time / 1000) % 60))
        player2TimerView!!.setText(String.format("%02d:%02d", player2Time / 1000 / 60, (player2Time / 1000) % 60))

        val screenLayout = findViewById<LinearLayout>(R.id.screenLayout)
        screenLayout.setOnClickListener {
            if(!isPaused || findViewById<TextView>(R.id.pressToStartText).visibility == View.VISIBLE){
                isPaused = false
                findViewById<ImageView>(R.id.pauseBtn).visibility = View.VISIBLE
                findViewById<ImageView>(R.id.restartBtn).visibility = View.VISIBLE
                findViewById<TextView>(R.id.pressToStartText).visibility = View.INVISIBLE
                findViewById<Button>(R.id.changePlayer1Time).visibility = View.INVISIBLE
                isPlayer1Turn = !isPlayer1Turn
                switchPlayerUI()
                startTimer()
            }
        }

    }

    private fun startTimer() {
        currentTimer?.cancel() // to stop previous players timer

        val remainingTime = if (isPlayer1Turn) player1Time else player2Time
        val timerTextView : TextView = if (isPlayer1Turn) player1TimerView!! else player2TimerView!!

        var lastTickTime = System.currentTimeMillis()

        currentTimer = object : CountDownTimer(remainingTime, 100) {

            override fun onTick(millisUntilFinished: Long) {
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - lastTickTime

                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60

                val formattedTime = String.format("%02d:%02d", minutes, seconds)
                updatePlayerTime(millisUntilFinished);
                //timerTextView.text = formattedTime
                if (isPlayer1Turn)
                    player1Time -= 100
                else
                    player2Time -= 100
            }

            override fun onFinish() {
                timerTextView.setText("Out of time!")
            }
        }.start()
    }

    // Function to pause the timer
    fun pauseGame() {
        currentTimer?.cancel() // to stop previous players timer
        findViewById<ImageView>(R.id.pauseBtn).setImageResource(R.drawable.resume_icon)
        isPaused = true
    }
    fun resumeGame(){
        findViewById<ImageView>(R.id.pauseBtn).setImageResource(R.drawable.pause_icon)
        isPaused = false
        startTimer()
    }



    private fun resetGame() {
        player1Time = 10000
        player2Time = 10000
        isPaused = true
        isPlayer1Turn = false
        currentTimer?.cancel() // to stop previous players timer
        resetPlayerTime(player1Time)
        findViewById<View>(R.id.view1).setBackgroundColor(android.graphics.Color.parseColor("#535353"))
        findViewById<View>(R.id.view2).setBackgroundColor(android.graphics.Color.parseColor("#535353"))
        player1TimerView?.setTextColor(android.graphics.Color.parseColor("#2C2C2C"))
        player2TimerView?.setTextColor(android.graphics.Color.parseColor("#2C2C2C"))
        findViewById<TextView>(R.id.pressToStartText).visibility = View.VISIBLE
        findViewById<Button>(R.id.changePlayer1Time).visibility = View.VISIBLE
        findViewById<ImageView>(R.id.pauseBtn).visibility = View.INVISIBLE
        findViewById<ImageView>(R.id.restartBtn).visibility = View.INVISIBLE
    }

    fun resetPlayerTime(millis: Long){
        val minutes = millis / 1000 / 60
        val seconds = (millis / 1000) % 60

        val formattedTime = String.format("%02d:%02d", minutes, seconds)
        var player1TimerView = findViewById<TextView>(R.id.player1TimerView)
        player1TimerView.text = formattedTime;
        var player2TimerView = findViewById<TextView>(R.id.player2TimerView)
        player2TimerView.text = formattedTime;
    }


    fun updatePlayerTime(millis: Long) {
        val minutes = millis / 1000 / 60
        val seconds = (millis / 1000) % 60

        val formattedTime = String.format("%02d:%02d", minutes, seconds)
        if(isPlayer1Turn){
            player1TimerView?.text = formattedTime;
        }else{
            player2TimerView?.text = formattedTime;
        }
    }

    fun switchPlayerUI(){
        if(isPlayer1Turn){
            // Player Part color
            findViewById<View>(R.id.view1).setBackgroundColor(android.graphics.Color.parseColor("#2D1E3E"))
            findViewById<View>(R.id.view2).setBackgroundColor(android.graphics.Color.parseColor("#535353"))
            // Text color
            player1TimerView?.setTextColor(android.graphics.Color.parseColor("#FFFFFF"))
            player2TimerView?.setTextColor(android.graphics.Color.parseColor("#2C2C2C"))
        }else{
            // Player Part color
            findViewById<View>(R.id.view2).setBackgroundColor(android.graphics.Color.parseColor("#2D1E3E"))
            findViewById<View>(R.id.view1).setBackgroundColor(android.graphics.Color.parseColor("#535353"))
            // Text color
            player2TimerView?.setTextColor(android.graphics.Color.parseColor("#FFFFFF"))
            player1TimerView?.setTextColor(android.graphics.Color.parseColor("#2C2C2C"))
        }
    }

}