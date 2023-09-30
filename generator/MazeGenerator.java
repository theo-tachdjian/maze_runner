package maze_runner.generator;

import maze_runner.Maze;

public interface MazeGenerator {

    default public Maze generate(int width, int height) {
        return new Maze(width, height);
    }

}