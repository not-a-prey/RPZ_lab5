package com.example.compass

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Стрілка напряму (тобто куди ми дивимось, завжди вгору показує)
@Composable
fun DirectionArrow() {
    Canvas(modifier = Modifier.size(60.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val center = Offset(canvasWidth / 2, canvasHeight / 2)

        // Draw a red triangle pointing up
        val triangleHeight = canvasHeight * 0.5f
        val triangleWidth = canvasWidth * 0.4f

        // Define the three points of the triangle
        val topPoint = Offset(center.x, center.y - triangleHeight / 2)
        val leftPoint = Offset(center.x - triangleWidth / 2, center.y + triangleHeight / 2)
        val rightPoint = Offset(center.x + triangleWidth / 2, center.y + triangleHeight / 2)

        // Draw the triangle
        val trianglePath = Path().apply {
            moveTo(topPoint.x, topPoint.y)
            lineTo(leftPoint.x, leftPoint.y)
            lineTo(rightPoint.x, rightPoint.y)
            close()
        }

        drawPath(
            path = trianglePath,
            color = Color.Red,
            style = Fill
        )
    }
}

//Так звана "роза" компасу, де розміщені магнітна стрілка, назви напрямків
@Composable
fun CompassRose(azimuth: Float) {
    Canvas(modifier = Modifier.size(240.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val center = Offset(canvasWidth / 2, canvasHeight / 2)
        val radius = minOf(canvasWidth, canvasHeight) / 2 * 0.8f

        drawCircle(
            color = Color.White,
            center = center,
            radius = radius + 20f
        )

        rotate(-azimuth) {
            // Магнітна стрілка (червона на північ (N), сіра - на південь (S))
            val arrowLength = radius * 0.9f
            val arrowWidth = radius * 0.15f

            //Стрілка на північ
            drawLine(
                color = Color.Red,
                start = center,
                end = Offset(center.x, center.y - arrowLength),
                strokeWidth = 8f
            )

            drawLine(
                color = Color.Red,
                start = Offset(center.x, center.y - arrowLength),
                end = Offset(center.x - arrowWidth, center.y - arrowLength + arrowWidth),
                strokeWidth = 8f
            )

            drawLine(
                color = Color.Red,
                start = Offset(center.x, center.y - arrowLength),
                end = Offset(center.x + arrowWidth, center.y - arrowLength + arrowWidth),
                strokeWidth = 8f
            )

            //Стрілка на південь
            drawLine(
                color = Color.LightGray,
                start = center,
                end = Offset(center.x, center.y + arrowLength),
                strokeWidth = 8f
            )

            drawLine(
                color = Color.LightGray,
                start = Offset(center.x, center.y + arrowLength),
                end = Offset(center.x - arrowWidth, center.y + arrowLength - arrowWidth),
                strokeWidth = 8f
            )

            drawLine(
                color = Color.LightGray,
                start = Offset(center.x, center.y + arrowLength),
                end = Offset(center.x + arrowWidth, center.y + arrowLength - arrowWidth),
                strokeWidth = 8f
            )

            // Кружечки білого кольору, куди вставимо літери напрямку
            drawCircle(
                color = Color.White,
                center = Offset(center.x, center.y - radius),
                radius = 20f
            )

            drawCircle(
                color = Color.White,
                center = Offset(center.x, center.y + radius),
                radius = 20f
            )

            drawCircle(
                color = Color.White,
                center = Offset(center.x + radius, center.y),
                radius = 20f
            )

            drawCircle(
                color = Color.White,
                center = Offset(center.x - radius, center.y),
                radius = 20f
            )
        }
    }
}

//Малюємо компас
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCardinalDirections(
    center: Offset,
    radius: Float
) {
    val textSize = radius * 0.2f

    //Коло для компасу (тобто стрілки магнітної та літер)
    drawCircle(
        color = Color.White,
        center = center,
        radius = radius
    )

    // Коло для N (півночі)
    drawCircle(
        color = Color.Red,
        center = Offset(center.x, center.y - radius),
        radius = textSize / 2
    )

    // Коло для S (півдня)
    drawCircle(
        color = Color.Black,
        center = Offset(center.x, center.y + radius),
        radius = textSize / 2
    )

    // Коло для E (сходу)
    drawCircle(
        color = Color.Black,
        center = Offset(center.x + radius, center.y),
        radius = textSize / 2
    )

    // Коло для W (заходу)
    drawCircle(
        color = Color.Black,
        center = Offset(center.x - radius, center.y),
        radius = textSize / 2
    )

    drawCardinalText("N", Offset(center.x, center.y - radius), textSize)
    drawCardinalText("S", Offset(center.x, center.y + radius), textSize)
    drawCardinalText("E", Offset(center.x + radius, center.y), textSize)
    drawCardinalText("W", Offset(center.x - radius, center.y), textSize)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCardinalText(
    text: String,
    position: Offset,
    textSize: Float
) { }

//Позначення напрямків, які будуть у кругах
@Composable
fun CompassDirections(azimuth: Float) {
    Box(
        modifier = Modifier.rotate(-azimuth),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "N",
            color = Color.Red,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .offset(y = (-100).dp)
        )

        Text(
            text = "S",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .offset(y = 100.dp)
        )

        Text(
            text = "E",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .offset(x = 100.dp)
        )

        Text(
            text = "W",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .offset(x = (-100).dp)
        )
    }
}