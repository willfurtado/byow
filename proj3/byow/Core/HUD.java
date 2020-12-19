package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;
import static byow.Core.Engine.NORMAL_FONT;

public class HUD {

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
