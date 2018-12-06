/*
 * Copyright (c) 2018 Nicolas Maltais
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to
 * deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom
 * the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice
 * shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.maltaisn.maze.render

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.AffineTransform
import java.awt.geom.Arc2D
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO


/**
 * Canvas for exporting raster image files (PNG, JPG, BMP, GIF).
 */
class RasterCanvas(format: OutputFormat) : Canvas(format) {

    private lateinit var graphics: Graphics2D
    private lateinit var buffImage: BufferedImage

    override var color: Color = Color.BLACK
        set(value) {
            field = if (format != OutputFormat.PNG && value.alpha != 255) {
                // If format is not PNG, can't allow colors with an alpha channel
                Color(value.rgb and 0xFFFFFF)
            } else {
                value
            }
            if (this::graphics.isInitialized) {
                graphics.color = color
            }
        }

    override var stroke: BasicStroke = BasicStroke(1f)
        set(value) {
            field = value
            if (this::graphics.isInitialized) {
                graphics.stroke = stroke
            }
        }

    /** Default transform saved to reset it when needed */
    private lateinit var transform: AffineTransform

    override var translate: Point? = null
        set(value) {
            super.translate = value
            field = super.translate
            if (this::graphics.isInitialized) {
                graphics.transform = transform
                if (value != null) {
                    graphics.translate(value.x.toDouble(), value.y.toDouble())
                }
            }
        }

    override var antialiasing = true
        set(value) {
            field = value
            if (this::graphics.isInitialized) {
                graphics.setRenderingHints(RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        if (value) RenderingHints.VALUE_ANTIALIAS_ON else RenderingHints.VALUE_ANTIALIAS_OFF))
            }
        }

    override fun init(width: Float, height: Float) {
        super.init(width, height)

        buffImage = BufferedImage(Math.ceil(width.toDouble()).toInt(),
                Math.ceil(height.toDouble()).toInt(),
                if (format == OutputFormat.PNG) BufferedImage.TYPE_INT_ARGB else BufferedImage.TYPE_INT_RGB)
        graphics = buffImage.createGraphics()
        transform = graphics.transform

        // Set all settings again now that graphics is created
        color = color
        stroke = stroke
        antialiasing = antialiasing
        translate = translate
    }

    override fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float) {
        graphics.draw(Line2D.Float(x1, y1, x2, y2))
    }

    override fun drawPolyline(points: LinkedList<Point>) {
        val xPoints = IntArray(points.size)
        val yPoints = IntArray(points.size)
        for (i in 0 until points.size) {
            val point = points[i]
            xPoints[i] = point.x.toInt()
            yPoints[i] = point.y.toInt()
        }
        graphics.drawPolyline(xPoints, yPoints, points.size)
    }

    override fun drawArc(x: Float, y: Float, rx: Float, ry: Float,
                         start: Double, extent: Double) {
        graphics.draw(Arc2D.Float(x - rx, y - ry, 2 * rx, 2 * ry,
                Math.toDegrees(start).toFloat(), Math.toDegrees(extent).toFloat(), Arc2D.OPEN))
    }

    override fun drawRect(x: Float, y: Float, width: Float, height: Float, filled: Boolean) {
        val rect = Rectangle2D.Float(x, y, width, height)
        if (filled) {
            graphics.draw(rect)
        } else {
            graphics.fill(rect)
        }
    }

    override fun exportTo(file: File) {
        ImageIO.write(buffImage, format.extension, file)
    }

}