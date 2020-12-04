package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

/**
 * MapLauncher Class is responsible for creating a rendering of the world generated within
 * the MapGenerator class. The main method does the bulk of the work
 */
public class MapLauncher {

    /** Draws the associated world of a MapGenerator instance to the screen. */
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(MapGenerator.WIDTH, MapGenerator.HEIGHT);

        MapGenerator mapGen = new MapGenerator(12345434352999L);
        TETile[][] finalWorldFrame = mapGen.createWorld();

        ter.renderFrame(finalWorldFrame);
    }
}
