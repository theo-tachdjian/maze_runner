package maze_runner.generator;

import maze_runner.Maze;

public class OptimizedMazeGenerator implements MazeGenerator {

    private boolean usePerfectType;

    public OptimizedMazeGenerator(boolean usePerfectType) {
        this.usePerfectType = usePerfectType;
    }

    @Override
    public Maze generate(int width, int height) {
        return null;
    }

    private class Pos {

        private int x;
        private int y;

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() { return x; }
        public int getY() { return y; }
    }

}