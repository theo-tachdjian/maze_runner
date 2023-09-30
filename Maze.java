package maze_runner;

public class Maze {

    private int[] startTilePos;
    private int[] endTilePos;
    private MazeTile[][] tiles;

    public Maze(int width, int height) {
        tiles = new MazeTile[width][height];

        // create MazeTile objects
        for(int y = 0; y < width; y++) {
            for(int x = 0; x < height; x++) {
                tiles[x][y] = new MazeTile();
            }
        }

        // set start and end tiles pos & types
        startTilePos = new int[2];
        endTilePos = new int[2];

        startTilePos[0] = 0;
        startTilePos[1] = 0;

        endTilePos[0] = width-1;
        endTilePos[1] = height-1;

        tiles[startTilePos[0]][startTilePos[1]].setUp();
        tiles[endTilePos[0]][endTilePos[1]].setDown();
    }

    public MazeTile getTile(int x, int y) {
        return isPosValid(x, y) ? tiles[x][y] : null;
    }

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }

    public boolean isPosValid(int x, int y) {
        return (x >= 0 && x < getWidth()) && (y >= 0 && y < getHeight());
    }

    public int[] getStartTilePos() {
        return startTilePos;
    }

    public int[] getEndTilePos() {
        return endTilePos;
    }

    public boolean isStartTile(int x, int y) {
        return x == startTilePos[0] && y == startTilePos[1];
    }

    public boolean isEndTile(int x, int y) {
        return x == endTilePos[0] && y == endTilePos[1];
    }

    /*
     * end tile is considered empty if it has only the DOWN mask
     */
    public boolean isEndTileEmpty() {
        return getTile(getEndTilePos()[0], getEndTilePos()[1]).getTileType() == MazeTile.getValueOfTileType(false, false, false, true);
    }

}