package com.example.compass

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CompassScreen(viewModel: CompassViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Нерухома стрілка напряму
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            DirectionArrow()
        }

        // Магнітна стрілка
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CompassRose(azimuth = viewModel.azimuth)
            CompassDirections(azimuth = viewModel.azimuth)
        }

        // Кнопка "Show/Hide strength"
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Button(
                onClick = { viewModel.toggleShowStrength() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = if (viewModel.showStrength) "Hide strength" else "Show strength",
                    fontSize = 16.sp
                )
            }
        }

        // Поле для виводу магнітної індукції
        if (viewModel.showStrength) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = "Magnetic field: %.0f μT".format(viewModel.magneticFieldStrength),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}