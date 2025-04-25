package com.github.ityeri.utils
import com.badlogic.gdx.graphics.Color
import kotlin.math.floor

fun fromHsv(hue: Float, saturation: Float = 1f, value: Float = 1f, alpha: Float = 1f): Color {
    val h = (hue % 360 + 360) % 360 / 60f  // 0~6 구간으로 변환, 음수 방지
    val c = value * saturation
    val x = c * (1 - kotlin.math.abs(h % 2 - 1))
    val m = value - c

    val (r1, g1, b1) = when (floor(h).toInt()) {
        0 -> Triple(c, x, 0f)
        1 -> Triple(x, c, 0f)
        2 -> Triple(0f, c, x)
        3 -> Triple(0f, x, c)
        4 -> Triple(x, 0f, c)
        5 -> Triple(c, 0f, x)
        else -> Triple(0f, 0f, 0f) // Fallback (shouldn't happen)
    }

    return Color(r1 + m, g1 + m, b1 + m, alpha)
}
