package starfishcollector

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Polygon

fun generateBoundaryPolygon(sides: Int, width: Float, height: Float): Polygon {

    val vertices = FloatArray(2 * sides)

    for (i in 0 until sides) {
        val angle: Float = i * 6.28f / sides
        // x-coordinate
        vertices[2 * i] = width / 2 * MathUtils.cos(angle) + width / 2
        // y-coordinate
        vertices[2 * i + 1] = height / 2 * MathUtils.sin(angle) + height / 2
    }

    return Polygon(vertices)
}

fun generateBoundaryRectangle(width: Float, height: Float): Polygon {
    val vertices = floatArrayOf(0f, 0f, width, 0f, width, height, 0f, height)
    return Polygon(vertices)
}