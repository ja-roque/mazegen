/*
 * Copyright (c) 2019 Nicolas Maltais
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

package com.maltaisn.mazegen.maze

import com.maltaisn.mazegen.render.SvgCanvas
import org.junit.jupiter.api.Test
import java.io.File

internal class SvgCanvasTest {

    @Test
    fun arc() {
        val canvas = SvgCanvas()
        canvas.init(100.0, 100.0)
        canvas.optimization = SvgCanvas.OPTIMIZATION_POLYPOINTS

//        canvas.drawLine(10.0, 0.0, 20.0, 0.0)
//        canvas.drawLine(0.0, 0.0, 10.0, 0.0)
//        canvas.drawLine(20.0, 0.0, 30.0, 0.0)

//        canvas.drawLine(20.0, 0.0, 10.0, 0.0)
//        canvas.drawLine(0.0, 0.0, 10.0, 0.0)
//        canvas.drawLine(20.0, 0.0, 30.0, 0.0)

//        canvas.drawLine(0.0, 0.0, 1.0, 0.0)
//        canvas.drawLine(1.0, 0.0, 1.0, 1.0)
//        canvas.drawLine(1.0, 1.0, 0.0, 1.0)
//        canvas.drawLine(0.0, 1.0, 0.0, 0.0)

        canvas.exportTo(File("testMazes/testSvg.svg"))
    }

}
