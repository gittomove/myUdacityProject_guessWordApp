package com.example.android.guesstheword.screens.game

import android.content.IntentSender
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel(){

    companion object{
        const val DONE = 0L
        const val ONE_SECOND = 1000L
        const val COUNTDOWN_TIME = 10000L
    }

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    private val countDownTimer: CountDownTimer

    val currentTimeString = Transformations.map(currentTime, {time ->
        DateUtils.formatElapsedTime(time)
    })
    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish



    // The list of words - the front of the list is the next word to guess
    lateinit var wordList: MutableList<String>

    init {
        Log.i("GameViewModel", "GameViewModel created!")
        resetList()
        nextWord()

        _score.value = 0
        _word.value = ""
        _eventGameFinish.value = false


        countDownTimer = object: CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUnitFinished: Long) {
/*
                _currentTime.value?.plus(1)
*/

                _currentTime.value = (millisUnitFinished/ ONE_SECOND)

            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventGameFinish.value = true
            }
        }

        countDownTimer.start()
    }


    private fun nextWord() {
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)


    }

    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }


    override fun onCleared() {
        super.onCleared()
        countDownTimer.cancel()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }


    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (_score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (_score.value)?.plus(1)
        nextWord()
    }

    fun gameHasFinished(){
        _eventGameFinish.value = false;
    }

}