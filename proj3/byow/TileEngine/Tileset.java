package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile LAND = new TETile('"', Color.GREEN,
            new Color(7, 108, 12), "land",
            "/Users/willfurtado/Desktop/cs61b/fa20-proj3-g579/proj3/byow/TileEngine/turf.png");
    public static final TETile OCEAN = new TETile('~', Color.blue,
            new Color(178, 239, 243), "ocean",
            "/Users/willfurtado/Desktop/cs61b/fa20-proj3-g579/proj3/byow/TileEngine/waves.png");
    public static final TETile SHORE = new TETile('.', Color.black,
            new Color(243, 231, 178), "shore",
            "/Users/willfurtado/Desktop/cs61b/fa20-proj3-g579/proj3/byow/TileEngine/sand.png");
    public static final TETile ARROW = new TETile('◉', Color.white,
            new Color(7, 108, 12), "arrow",
            "/Users/willfurtado/Desktop/cs61b/fa20-proj3-g579/proj3/byow/TileEngine/turf.png");
    public static final TETile OSKI = new TETile('O', Color.blue,
            Color.white, "oski",
            "/Users/willfurtado/Desktop/cs61b/fa20-proj3-g579/proj3/byow/TileEngine/oski.png");
    public static final TETile COIN = new TETile('C', Color.YELLOW,
            Color.BLACK, "coin",
            "/Users/willfurtado/Desktop/cs61b/fa20-proj3-g579/proj3/byow/TileEngine/coin.png");
}


