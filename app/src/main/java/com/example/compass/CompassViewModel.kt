package com.example.compass

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.hypot

class CompassViewModel : ViewModel() {
    // Датчики
    private var accelerometerReading = FloatArray(3)
    private var magnetometerReading = FloatArray(3)
    private var magneticValues = FloatArray(3)

    var azimuth by mutableStateOf(0f)
    var magneticFieldStrength by mutableStateOf(0f)
    var showStrength by mutableStateOf(false)
    private var hasSensorsAvailable by mutableStateOf(false)

    //Час для оновлення положення стрілки компасу та інформації про магнітну індукцію
    private var lastUpdateTime = 0L
    private val updateIntervalInMs = 80

    fun hasSensors(): Boolean = hasSensorsAvailable

    fun setHasSensors(available: Boolean) {
        hasSensorsAvailable = available
    }

    fun toggleShowStrength() {
        showStrength = !showStrength
    }

    // Event listener нашого датчика
    val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
                    updateOrientation()
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
                    System.arraycopy(event.values, 0, magneticValues, 0, event.values.size)
                    updateOrientation()
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) { }
    }

    private fun calculateMagneticFieldStrength(values: FloatArray): Float {
        val strength = hypot(hypot(values[0], values[1]), values[2])
        return strength
    }

    private fun updateOrientation() {
        val rotationMatrix = FloatArray(9)
        val orientationAngles = FloatArray(3)

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTime < updateIntervalInMs) {
            return
        }
        lastUpdateTime = currentTime


        magneticFieldStrength = calculateMagneticFieldStrength(magneticValues)

        if (magnetometerReading.all { it != 0f } && accelerometerReading.all { it != 0f }) {
            val success = android.hardware.SensorManager.getRotationMatrix(
                rotationMatrix,
                null,
                accelerometerReading,
                magnetometerReading
            )

            if (success) {
                android.hardware.SensorManager.getOrientation(rotationMatrix, orientationAngles)
                azimuth = (Math.toDegrees(orientationAngles[0].toDouble()).toFloat() + 360) % 360
            }
        }
    }
}