package maze_runner;

public class MazeTile {

    /*
     * masks:
     * 1 : LEFT		0b0001
     * 2 : RIGHT	0b0010
     * 4 : UP		0b0100
     * 8 : DOWN		0b1000
     */
    public static final String[] MAZE_TILE_PATTERNS = {
            "#########",	// 0 NONE

            "###..####",	// 1 LEFT
            "####..###",	// 2 RIGHT
            "###...###",
            "#.##.####",	// 4 UP
            "#.#..####",
            "#.##..###",
            "#.#...###",
            "####.##.#",	// 8 DOWN
            "###..##.#",
            "####..#.#",
            "###...#.#",
            "#.##.##.#",
            "#.#..##.#",
            "#.##..#.#",
            "#.#...#.#"

            // "####.####"		// 16 NO_CONNECTIONS
    };

    /*
     * holds a value between 0 and 15, indicating the type of the tile.
     *
     * masks:
     * 1 : LEFT		0b0001
     * 2 : RIGHT	0b0010
     * 4 : UP		0b0100
     * 8 : DOWN		0b1000
     *
     * Examples:
     * - LEFT+RIGHT = 0b0011
     * - RIGHT+DOWN = 0b1010
     */
    private byte type;

    private static final byte LEFT = 0b0001;
    private static final byte RIGHT = 0b0010;
    private static final byte UP = 0b0100;
    private static final byte DOWN = 0b1000;

    public MazeTile() {
        type = 0;
    }

    public byte getTileType() {
        return type;
    }

    public boolean noConnections() {
        return type == 0;
    }

    public boolean isConnectedLeft() {
        return hasLeftMask(type);
    }

    public boolean isConnectedRight() {
        return hasRightMask(type);
    }

    public boolean isConnectedUp() {
        return hasUpMask(type);
    }

    public boolean isConnectedDown() {
        return hasDownMask(type);
    }

    /*
     * sets the tile type by adding masks to type
     */
    public void setTileType(boolean left, boolean right, boolean up, boolean down) {
        type = getValueOfTileType(left, right, up, down);
    }

    /*
     * sets the masks to the current tile type if not already present
     */
    public void addTypeMasks(boolean left, boolean right, boolean up, boolean down) {
        type = getValueOfTileType(left || isConnectedLeft(), right || isConnectedRight(), up || isConnectedUp(), down || isConnectedDown());
    }

    public void setLeft() {
        if (!isConnectedLeft()) { type += LEFT; }
    }

    public void setRight() {
        if (!isConnectedRight()) { type += RIGHT; }
    }

    public void setUp() {
        if (!isConnectedUp()) { type += UP; }
    }

    public void setDown() {
        if (!isConnectedDown()) { type += DOWN; }
    }

    public static byte getValueOfTileType(boolean left, boolean right, boolean up, boolean down) {
        byte type = 0;

        if (left) { type += LEFT; }
        if (right) { type += RIGHT; }
        if (up) { type += UP; }
        if (down) { type += DOWN; }

        return type;
    }

    public static boolean hasLeftMask(byte type) {
        type = (byte) (type % 16);
        return type % 2 == 1;
    }

    public static boolean hasRightMask(byte type) {
        type = (byte) (type % 16);
        // remove LEFT mask
        if (hasLeftMask(type)) {
            type -= LEFT;
        }
        return (type % DOWN) % UP == RIGHT;
    }

    public static boolean hasUpMask(byte type) {
        type = (byte) (type % 16);
        // remove LEFT mask
        if (hasLeftMask(type)) {
            type -= LEFT;
        }
        // remove RIGHT mask
        if (hasRightMask(type)) {
            type -= RIGHT;
        }
        return type % DOWN == UP;
    }

    public static boolean hasDownMask(byte type) {
        type = (byte) (type % 16);
        return type >= DOWN;
    }

}