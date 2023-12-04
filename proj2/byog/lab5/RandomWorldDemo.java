package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world that contains RANDOM tiles.
 */
public class RandomWorldDemo {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /**
     * Fills the given 2D array of tiles with RANDOM tiles.
     * @param tiles
     */
    public static void fillWithRandomTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = randomTile();
            }
        }
    }

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.MOUNTAIN;
            default: return Tileset.NOTHING;
        }
    }

    /**
     * Computes the width of row i for a size s hexagon.
     * @param s The size of the hex.
     * @param i The row number where i = 0 is the bottom row.
     * @return
     */
    public static int hexRowWidth(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - 1 - effectiveI;
        }

        return s + 2 * effectiveI;
    }

    /**
     * Computesrelative x coordinate of the leftmost tile in the ith
     * row of a hexagon, assuming that the bottom row has an x-coordinate
     * of zero. For example, if s = 3, and i = 2, this function
     * returns -2, because the row 2 up from the bottom starts 2 to the left
     * of the start position, e.g.
     *   xxxx
     *  xxxxxx
     * xxxxxxxx
     * xxxxxxxx <-- i = 2, starts 2 spots to the left of the bottom of the hex
     *  xxxxxx
     *   xxxx
     *
     * @param s size of the hexagon
     * @param i row num of the hexagon, where i = 0 is the bottom
     * @return
     */
    public static int hexRowOffset(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - 1 - effectiveI;
        }
        return -effectiveI;
    }

    /** Adds a row of the same tile.
     * @param world the world to draw on
     * @param p the leftmost position of the row
     * @param width the number of tiles wide to draw
     * @param t the tile to draw
     */
    public static void addRow(TETile[][] world, Position p, int width, TETile t) {
        for (int xi = 0; xi < width; xi += 1) {
            int xCoord = p.x + xi;
            int yCoord = p.y;
            world[xCoord][yCoord] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
        }
    }

    private static class Position {
        public int x;
        public int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    /**
     * Adds a hexagon to the world.
     * @param world the world to draw on
     * @param p the bottom left coordinate of the hexagon
     * @param s the size of the hexagon
     * @param t the tile to draw
     */
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {

        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }

        // hexagons have 2*s rows. this code iterates up from the bottom row,
        // which we call row 0.
        for (int yi = 0; yi < 2 * s; yi += 1) {
            int thisRowY = p.y + yi;

            int xRowStart = p.x + hexRowOffset(s, yi);
            Position rowStartP = new Position(xRowStart, thisRowY);

            int rowWidth = hexRowWidth(s, yi);

            addRow(world, rowStartP, rowWidth, t);

        }
    }

    private static final int HEX_SIZE = 3;
    /**
     * Draws a column of hexagons with random biomes.
     * @param world the world to draw on
     * @param p the bottom left coordinate of the column
     * @param numHexagons the number of hexagons in the column
     */
    private static void drawRandomVerticalHexes(TETile[][] world, Position p, int numHexagons) {
        for (int i = 0; i < numHexagons; i++) {
            Position hexagonPosition = new Position(p.x, p.y + i * 2 * HEX_SIZE);
            TETile hexagonTile = randomTile();
            addHexagon(world, hexagonPosition, HEX_SIZE, hexagonTile);
        }
    }

    /**
     * Generates a random world with a tesselation of hexagons.
     */
    private static void generateRandomWorldHex(TETile[][] world) {

        Position startPosition = new Position(10, 15);

        // Draw each column of hexagons
        drawRandomVerticalHexes(world, startPosition, 3);
        drawRandomVerticalHexes(world, bottomRightNeighbor(startPosition), 4);
        drawRandomVerticalHexes(world, bottomRightNeighbor(bottomRightNeighbor(startPosition)), 5);
        drawRandomVerticalHexes(world, bottomRightNeighbor(bottomRightNeighbor(topRightNeighbor(startPosition))), 4);
        drawRandomVerticalHexes(world, bottomRightNeighbor(bottomRightNeighbor(topRightNeighbor(topRightNeighbor(startPosition)))), 3);
    }

    /**
     * Computes the position of the top-right neighbor hexagon.
     * @param p the position of the current hexagon
     * @return the position of the top-right neighbor hexagon
     */
    private static Position topRightNeighbor(Position p) {
        return new Position(p.x + 2 * HEX_SIZE - 1, p.y + HEX_SIZE);
    }

    /**
     * Computes the position of the bottom-right neighbor hexagon.
     * @param p the position of the current hexagon
     * @return the position of the bottom-right neighbor hexagon
     */
    private static Position bottomRightNeighbor(Position p) {
        return new Position(p.x + 2 * HEX_SIZE - 1, p.y - HEX_SIZE);
    }


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] randomTiles = new TETile[WIDTH][HEIGHT];
        // Initialize
        for (int i = 0; i < randomTiles.length; ++i) {
            for (int j = 0; j < randomTiles[0].length; ++j) {
                randomTiles[i][j] = Tileset.NOTHING;
            }
        }

        // fillWithRandomTiles(randomTiles);

        // Position p = new Position(1, 0);
        // addHexagon(randomTiles, p, 2, randomTile());

        generateRandomWorldHex(randomTiles);

        ter.renderFrame(randomTiles);
    }


}
