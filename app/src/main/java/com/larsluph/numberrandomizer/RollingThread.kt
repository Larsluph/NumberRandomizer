package com.larsluph.numberrandomizer

class RollingThread(private val timeout: Int, private val rollMethod: () -> Unit) : Runnable {
    override fun run() {
        val timestamp = System.currentTimeMillis()

        do {
            rollMethod()
            Thread.sleep(40)
        } while (System.currentTimeMillis() - timestamp < timeout)
    }
}
