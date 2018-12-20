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

package com.maltaisn.mazegen

import com.maltaisn.mazegen.generator.Generator
import com.maltaisn.mazegen.maze.Maze
import com.maltaisn.mazegen.maze.Position
import com.maltaisn.mazegen.render.Canvas
import com.maltaisn.mazegen.render.OutputFormat
import com.maltaisn.mazegen.render.RasterCanvas
import com.maltaisn.mazegen.render.SvgCanvas
import java.awt.BasicStroke
import java.awt.Color
import java.io.File
import kotlin.reflect.KClass


/**
 * Class representing the JSON configuration file content.
 * Use [ConfigurationParser] to create it from JSON.
 */
class Configuration(val mazeSets: List<MazeSet>,
                    val output: Output,
                    val style: Style) {

    /**
     * A set of mazes with the same settings.
     */
    class MazeSet(var name: String,
                  val count: Int,
                  val type: KClass<out Maze>,
                  val parameters: Array<*>,
                  val generator: Generator,
                  val braiding: Maze.Braiding?,
                  val openings: List<Position>,
                  val solve: Boolean)

    /**
     * Output settings.
     */
    open class Output(val format: OutputFormat, val path: File) {

        open fun createCanvas(): Canvas = RasterCanvas(format)
    }

    /**
     * Output settings for the SVG format.
     */
    class SvgOutput(path: File, private val optimize: Boolean,
                    private val precision: Int) : Output(OutputFormat.SVG, path) {

        override fun createCanvas(): Canvas {
            val canvas = SvgCanvas()
            canvas.optimize = optimize
            canvas.precision = precision
            return canvas
        }

    }

    /**
     * Style settings for drawing the mazes.
     */
    class Style(val cellSize: Double,
                val backgroundColor: Color?,
                val color: Color,
                val stroke: BasicStroke,
                val solutionColor: Color,
                val solutionStroke: BasicStroke,
                val antialiasing: Boolean)

}