package maze_runner.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import maze_runner.Maze;
import maze_runner.MazeTile;

public class SimpleImperfectMazeGenerator implements MazeGenerator {

    @Override
    public Maze generate(int width, int height) {
        Maze maze = new Maze(width, height);
        Pos pos = new Pos(0, 0);
        byte prevDirection = MazeTile.getValueOfTileType(false, false, false, true);
        int loopCount = 0;
        // if loopCount is higher or equal to this value, try to go to the exit
        final int maxLoopCount = width*height*Math.max(width, height);

        while(!maze.isEndTile(pos.getX(), pos.getY())) {

            if (loopCount == maxLoopCount) {
                pos = new Pos(maze.getEndTilePos()[0], maze.getEndTilePos()[1]);
                prevDirection = MazeTile.getValueOfTileType(false, false, true, false);
            }

            byte availableDirections = getValidTiles(pos.getX(), pos.getY(), maze);

            // if invalid maze (1 tile wide)
            if (availableDirections == 0) {
                break;
            }

            List<Pos> directionsPos = new ArrayList<Pos>(3);
            List<Byte> directionsType = new ArrayList<Byte>(3);
            if (prevDirection != MazeTile.getValueOfTileType(true, false, false, false) && MazeTile.hasLeftMask(availableDirections)) {
                directionsPos.add(new Pos(pos.getX()-1, pos.getY()));
                directionsType.add(MazeTile.getValueOfTileType(true, false, false, false));
            }
            if (!(loopCount >= maxLoopCount) && prevDirection != MazeTile.getValueOfTileType(false, true, false, false) && MazeTile.hasRightMask(availableDirections)) {
                directionsPos.add(new Pos(pos.getX()+1, pos.getY()));
                directionsType.add(MazeTile.getValueOfTileType(false, true, false, false));
            }
            if (prevDirection != MazeTile.getValueOfTileType(false, false, true, false) && MazeTile.hasUpMask(availableDirections)) {
                directionsPos.add(new Pos(pos.getX(), pos.getY()-1));
                directionsType.add(MazeTile.getValueOfTileType(false, false, true, false));
            }
            if (!(loopCount >= maxLoopCount) && prevDirection != MazeTile.getValueOfTileType(false, false, false, true) && MazeTile.hasDownMask(availableDirections)) {
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

            boolean shouldBreak = false;
            if (loopCount >= maxLoopCount) {
                if (MazeTile.hasLeftMask(directionOfNextPos)
                        && !maze.getTile(nextPos.getX(), nextPos.getY()).noConnections()
                        && !maze.getTile(nextPos.getX(), nextPos.getY()).isConnectedLeft()) {
                    shouldBreak = true;
                } else if (MazeTile.hasUpMask(directionOfNextPos)
                        && !maze.getTile(nextPos.getX(), nextPos.getY()).noConnections()
                        && !maze.getTile(nextPos.getX(), nextPos.getY()).isConnectedUp()) {
                    shouldBreak = true;
                }
            }

            // link next tile to current tile
            maze.getTile(nextPos.getX(), nextPos.getY()).addTypeMasks(
                    MazeTile.hasRightMask(directionOfNextPos),
                    MazeTile.hasLeftMask(directionOfNextPos),
                    MazeTile.hasDownMask(directionOfNextPos),
                    MazeTile.hasUpMask(directionOfNextPos));
            if (shouldBreak) {
                break;
            }
            prevDirection = directionOfNextPos;
            pos = nextPos;
            loopCount++;
        }

        return maze;
    }

    /*
     * returns a byte between 0 and 15 indicating the valid directions.
     */
    private byte getValidTiles(int x, int y, Maze maze) {
        return MazeTile.getValueOfTileType(
                maze.isPosValid(x-1, y),
                maze.isPosValid(x+1, y),
                maze.isPosValid(x, y-1),
                maze.isPosValid(x, y+1));
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