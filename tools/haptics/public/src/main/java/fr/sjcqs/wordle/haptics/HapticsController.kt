package fr.sjcqs.wordle.haptics

interface HapticsController {
    fun vibrate(effect: Vibration)
    fun cancel()
}

fun HapticsController.tick() = vibrate(Vibration.Predefined.Tick)
fun HapticsController.click() = vibrate(Vibration.Predefined.Click)
fun HapticsController.heavyClick() = vibrate(Vibration.Predefined.HeavyClick)
fun HapticsController.doubleClick() = vibrate(Vibration.Predefined.DoubleClick)