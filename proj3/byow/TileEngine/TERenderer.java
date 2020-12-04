package byow.TileEngine;

import byow.Core.MapGenerator;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class TERenderer {
    private static final int TILE_SIZE = 16;
    private static final Font NORMAL_FONT = new Font("Georgia", Font.PLAIN, 20);
    private static final Font TITLE_FONT = new Font("Georgia", Font.PLAIN, 28);
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;
    private int hudSize;

    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, (height * TILE_SIZE) + hudSize);
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);      
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initializatiom method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *                     
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void renderFrame(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x + xOffset, y + yOffset);
            }
        }
        StdDraw.show();
    }

    /** Renders the Heads-Up Display (HUD) for a given world. Takes in a score and a number
     * of steps remaining and displays them at the top of the screen. */
    public void renderHUD(TETile[][] world, int score, int stepsRemaining) {
        drawHUD(MapGenerator.getWidth() + (MapGenerator.getHUDSize() / 2),
                MapGenerator.getStartingHeight() + (MapGenerator.getHUDSize() / 2),
                score, stepsRemaining, world);
    }

    /** Renders the Heads-Up Display (HUD) for a given world at its finish. */
    public void renderHUDFinish() {
        drawHUDFinish(MapGenerator.getWidth() + (MapGenerator.getHUDSize() / 2),
                MapGenerator.getStartingHeight() + (MapGenerator.getHUDSize() / 2));
    }

    /** Renders the Heads-Up Display (HUD) for a given world during replayMode. Takes in
     * a score and a number of steps remaining and displays them at the top of the screen. */
    public void renderHUDReplay(TETile[][] world, int score, int stepsRemaining) {
        drawHUDReplay(MapGenerator.getWidth() + (MapGenerator.getHUDSize() / 2),
                MapGenerator.getStartingHeight() + (MapGenerator.getHUDSize() / 2),
                score, stepsRemaining, world);
    }

    /** Renders the Heads-Up Display (HUD) for a given world at its finish during replayMode. */
    public void renderHUDFinishReplay() {
        drawHUDFinishReplay(MapGenerator.getWidth() + (MapGenerator.getHUDSize() / 2),
                MapGenerator.getStartingHeight() + (MapGenerator.getHUDSize() / 2));
    }

    /** Renders the finish screen depending on whether the user has won the game. */
    public void renderFinishScreen(boolean wonGame, int stepsTaken) {
        if (wonGame) {
            drawWinningScreen(MapGenerator.getWidth(), MapGenerator.getHeight(), stepsTaken);
        } else {
            drawLosingScreen(MapGenerator.getWidth(), MapGenerator.getHeight(), stepsTaken);
        }
    }


    /** Uses StdDraw library to draw Heads-Up Display (HUD) to our game. */
    private void drawHUD(int x, int y, int score, int stepsRemaining, TETile[][] world) {
        StdDraw.setPenColor(new Color(69, 102, 34));
        StdDraw.filledRectangle(x / 2, y, x / 2, MapGenerator.getHUDSize() / 2);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(NORMAL_FONT);
        StdDraw.text(x / 2, y, "Tile: " + mouseTile(world) + "         "
                + "Coins Collected: " + score + "         "
                + "Steps Remaining: " + stepsRemaining + "         "
                + "Press 'E' to exchange 2 coins for 25 steps");
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /** Uses StdDraw library to draw Heads-Up Display (HUD) to our game in replayMode. */
    private void drawHUDReplay(int x, int y, int score, int stepsRemaining, TETile[][] world) {
        StdDraw.setPenColor(new Color(69, 102, 34));
        StdDraw.filledRectangle(x / 2, y, x / 2, MapGenerator.getHUDSize() / 2);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(NORMAL_FONT);
        StdDraw.text(x / 2, y, "Instant Replay Zone" + "         "
                + "Coins Collected: " + score + "         "
                + "Steps Remaining: " + stepsRemaining);
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /** Uses StdDraw library to draw Heads-Up Display (HUD) to our game in
     * replayMode at its conclusion. */
    private void drawHUDFinishReplay(int x, int y) {
        StdDraw.setPenColor(new Color(69, 102, 34));
        StdDraw.filledRectangle(x / 2, y, x / 2, MapGenerator.getHUDSize() / 2);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(NORMAL_FONT);
        StdDraw.text(x / 2, y, "Instant Replay Has Finished!");
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /** Uses StdDraw library to draw Heads-Up Display (HUD) to our game at its conclusion. */
    private void drawHUDFinish(int x, int y) {
        StdDraw.setPenColor(new Color(69, 102, 34));
        StdDraw.filledRectangle(x / 2, y, x / 2, MapGenerator.getHUDSize() / 2);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(NORMAL_FONT);
        StdDraw.text(x / 2, y, "Game Finished!");
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /** Uses StdDraw library to draw a screen associated to the user winning the game. */
    private void drawWinningScreen(int x, int y, int stepsTaken) {
        StdDraw.clear();
        StdDraw.setPenColor(new Color(69, 102, 34));
        StdDraw.setFont(TITLE_FONT);
        StdDraw.text(x / 2, y * 0.55, "Congrats, you won!");
        StdDraw.text(x / 2, y * 0.45, "You took a total of " + stepsTaken + " steps!");
        StdDraw.text(x / 2, y * 0.1, "Press ':Q' to save and quit");
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /** Uses StdDraw library to draw a screen associated to the user losing the game. */
    private void drawLosingScreen(int x, int y, int stepsTaken) {
        StdDraw.clear();
        StdDraw.setPenColor(new Color(69, 102, 34));
        StdDraw.setFont(TITLE_FONT);
        StdDraw.text(x / 2, y * 0.55, "You lost... Better luck next time!");
        StdDraw.text(x / 2, y * 0.45, "You've exhausted all " + stepsTaken + " steps!");
        StdDraw.text(x / 2, y * 0.1, "Press 'Q' to quit");
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /** Returns the corresponding description of the tile that a user's mouse is hovering over. */
    private String mouseTile(TETile[][] world) {
        int mouseX = (int) Math.floor(StdDraw.mouseX());
        int mouseY = (int) Math.floor(StdDraw.mouseY());
        if (mouseX >= MapGenerator.getWidth() || mouseX < 0
            || mouseY >= MapGenerator.getStartingHeight() || mouseY < 0) {
            return "HUD";
        }
        return world[mouseX][mouseY].description();
    }


}
