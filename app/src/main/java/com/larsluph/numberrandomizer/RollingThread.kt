package com.larsluph.numberrandomizer

import android.content.Context

class RollingThread(private val context: Context, private val timeout: Int) : Runnable {
    override fun run() {
        val timestamp = System.currentTimeMillis()

        do {
            rollOnce()
            Thread.sleep(40)
        } while (System.currentTimeMillis() - timestamp < timeout)
    }

    private fun rollOnce() {
        (context as MainActivity).rollOnce()
    }
}
