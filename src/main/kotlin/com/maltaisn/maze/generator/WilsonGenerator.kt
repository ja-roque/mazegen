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

package com.maltaisn.maze.generator

import com.maltaisn.maze.maze.Maze


/**
 * Implementation of Wilson's algorithm as described
 * [here](http://weblog.jamisbuck.org/2011/1/20/maze-generation-wilson-s-algorithm)
 * and [here](https://bl.ocks.org/mbostock/11357811).
 *
 * 1. Make the initial cell the current cell and mark it as visited.
 * 2. Perform a random walk without changing the maze, adding cells to a list.
 *    If a cell walked to is part of the maze, add the random walk to the maze.
 *    If a cell is already part of the random walk list, start the walk over from that cell.
 * 3. Repeat step 2 until all cells have been added to the maze.
 *
 * Generated mazes are bias-free, meaning all possible mazes are generated
 * with equal probability. However this comes to the cost of efficiency,
 * which is very low, because each cell can be visited many times and the
 * algorithm has to find all the unvisited cells by walking randomly.
 * The algorithm is still more efficient than [AldousBroderGenerator].
 */
class WilsonGenerator(maze: Maze) : Generator(maze) {

    override fun generate() {
        maze.reset(false)

        val unvisitedCells = maze.getAllCells()

        // Make the initial cell visited
        val initialCell = maze.getRandomCell()
        initialCell.visited = true
        unvisitedCells.remove(initialCell)

        while (unvisitedCells.isNotEmpty()) {
            // Perform a random walk from an unvisited cell until it meets with a visited cell
            val walk = mutableListOf(unvisitedCells.random())
            while (true) {
                val current = walk.last()

                // Get a random neighbor
                val neighbors = current.getNeighbors().toMutableList()
                if (walk.size > 1) {
                    // Can't go back where it came from
                    neighbors.remove(walk[walk.size - 2])
                }
                val neighbor = neighbors.random()

                if (neighbor.visited) {
                    // The maze contains this cell, end the walk
                    walk.add(neighbor)
                    break
                } else {
                    val index = walk.indexOf(neighbor)
                    if (index != -1) {
                        // The walk already contains that cell, start over from there
                        walk.subList(index, walk.size).clear()
                    }
                }
                walk.add(neighbor)
            }
            for (i in 1 until walk.size) {
                val cell = walk[i - 1]
                cell.connectWith(walk[i])
                cell.visited = true
                unvisitedCells.remove(cell)
            }
        }
    }

}