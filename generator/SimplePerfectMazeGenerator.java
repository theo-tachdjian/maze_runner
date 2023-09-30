package maze_runner.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import maze_runner.Maze;
import maze_runner.MazeTile;

public class SimplePerfectMazeGenerator implements MazeGenerator {

    @Override
    public Maze generate(int width, int height) {
        Maze maze = new Maze(width, height);
        // stack to keep track of previous tiles
        List<Pos> stack = new ArrayList<Pos>(width*height-1);

        stack.add(new Pos(maze.getStartTilePos()[0], maze.getStartTilePos()[1]));

        while(!stack.isEmpty()) {
            Pos pos = stack.get(stack.size()-1);
            byte availableDirections = getEmptyNeighbors(pos.getX(), pos.getY(), maze);

            // if no available tiles, continue to go to previous tile
            if (availableDirections == 0) {
                stack.remove(stack.size()-1);
                continue;
            }

            List<Pos> directionsPos = new ArrayList<Pos>(3);
            List<Byte> directionsType = new ArrayList<Byte>(3);
            if (MazeTile.hasLeftMask(availableDirections)) {
                directionsPos.add(new Pos(pos.getX()-1, pos.getY()));
                directionsType.add(MazeTile.getValueOfTileType(true, false, false, false));
            }
            if (MazeTile.hasRightMask(availableDirections)) {
                directionsPos.add(new Pos(pos.getX()+1, pos.getY()));
                directionsType.add(MazeTile.getValueOfTileType(false, true, false, false));
            }
            if (MazeTile.hasUpMask(availableDirections)) {
                directionsPos.add(new Pos(pos.getX(), pos.getY()-1));
                directionsType.add(MazeTile.getValueOfTileType(false, false, true, false));
            }
            if (MazeTile.hasDownMask(availableDirections)) {
                directionsPos.add(new Pos(pos.getX(), pos.getY()+1));
                directionsType.add(MazeTile.getValueOfTileType(false, false, false, true));
            }

            // choose a random direction
            Random r = new Random();
            int index = r.nextInt(0, directionsPos.size());
            Pos nextPos = directionsPos.get(index);
            byte directionOfNextPos = directionsType.get(index);

            // set the current tile's new direction
            maze.getTile(pos.getX(), pos.getY()).addTypeMasks(
                    MazeTile.hasLeftMask(directionOfNextPos),
                    MazeTile.hasRightMask(directionOfNextPos),
                    MazeTile.hasUpMask(directionOfNextPos),
                    MazeTile.hasDownMask(directionOfNextPos));
            // link next tile to current tile
            maze.getTile(nextPos.getX(), nextPos.getY()).addTypeMasks(
                    MazeTile.hasRightMask(directionOfNextPos),
                    MazeTile.hasLeftMask(directionOfNextPos),
                    MazeTile.hasDownMask(directionOfNextPos),
                    MazeTile.hasUpMask(directionOfNextPos));

            // add pos of next tile to stack
            stack.add(nextPos);
        }

        return maze;
    }

    /*
     * returns a byte between 0 and 15 indicating the directions of empty tiles.
     */
    private byte getEmptyNeighbors(int x, int y, Maze maze) {
        return MazeTile.getValueOfTileType(
                maze.isPosValid(x-1, y) && maze.getTile(x-1, y).noConnections() || (maze.isEndTile(x-1, y) && maze.isEndTileEmpty()),
                maze.isPosValid(x+1, y) && maze.getTile(x+1, y).noConnections() || (maze.isEndTile(x+1, y) && maze.isEndTileEmpty()),
                maze.isPosValid(x, y-1) && maze.getTile(x, y-1).noConnections() || (maze.isEndTile(x, y-1) && maze.isEndTileEmpty()),
                maze.isPosValid(x, y+1) && maze.getTile(x, y+1).noConnections() || (maze.isEndTile(x, y+1) && maze.isEndTileEmpty()));
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