package pl.yahoo.pawelpiedel.features.vibrations

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import pl.yahoo.pawelpiedel.injection.ApplicationContext
import javax.inject.Inject

class VibrationsService @Inject
constructor(@param:ApplicationContext private val context: Context) {

    fun vibrate(miliseconds: Int) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(miliseconds.toLong(), VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(miliseconds.toLong())
        }

    }
}
