package com.example.compass

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.compass.ui.theme.CompassTheme

class MainActivity : ComponentActivity() {
    private lateinit var sensorManager: SensorManager
    private val viewModel: CompassViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ініціалізація менеджеру датчика
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (magneticSensor != null && accelerometerSensor != null) {
            viewModel.setHasSensors(true)
        }

        setContent {
            CompassTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    CompassScreen(viewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.hasSensors()) {
            sensorManager.registerListener(
                viewModel.sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME
            )

            sensorManager.registerListener(
                viewModel.sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (viewModel.hasSensors()) {
            sensorManager.unregisterListener(viewModel.sensorEventListener)
        }
    }
}