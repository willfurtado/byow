package byow.Core;

import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

import static byow.Core.Engine.*;

public class ScreenDrawer {
    
    private Engine engine;

    /** Constructs an instance of the ScreenDrawer class to display the UI for the game. */
    public ScreenDrawer(Engine inputEngine) {
        this.engine = inputEngine;
    }

    /** Initializes the TERenderer object using respective WIDTH, HEIGHT and renders the world. */
    protected void drawGameScreen() {
        engine.ter.initialize(WIDTH, HEIGHT);
        engine.mapGen = new MapGenerator(engine.SEED);
        engine.world =  engine.mapGen.createWorld();
        engine.worldInitialized = true;
        engine.ter.renderFrame(engine.world);
    }

    /** Draws the initial landing screen to the page. */
    protected void drawSeedPromptScreen(String seed) {
        edu.princeton.cs.introcs.StdDraw.clear();
        edu.princeton.cs.introcs.StdDraw.setPenColor(new Color(69, 102, 34));
        edu.princeton.cs.introcs.StdDraw.filledSquare(0, 0, 1);
        edu.princeton.cs.introcs.StdDraw.setPenColor(Color.white);
        edu.princeton.cs.introcs.StdDraw.setFont(NORMAL_FONT);
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.75, "Enter Random Number:");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.5, seed);
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.25, "Press 'S' to Create World");

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }
    
    /** Draws the loading screen for the Instant Replay mode of the corresponding game instance. */
    protected void drawReplayLoadingScreen() {
        edu.princeton.cs.introcs.StdDraw.clear();
        edu.princeton.cs.introcs.StdDraw.setPenColor(new Color(69, 102, 34));
        edu.princeton.cs.introcs.StdDraw.filledSquare(0, 0, 1);
        edu.princeton.cs.introcs.StdDraw.setPenColor(Color.white);
        edu.princeton.cs.introcs.StdDraw.setFont(engine.TITLE_FONT);
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.75, "Instant Replay Zone");
        edu.princeton.cs.introcs.StdDraw.setFont(NORMAL_FONT);
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.40, "Press 'R' to enter Replay Mode");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.15, "While in Replay Mode, "
                + "press 'C' to see next move");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.10, "While in Replay Mode, "
                + "press 'Q' to quit the game");
    }

    /** Outputs the initial loading screen of our world generation game. */
    void drawLoadingScreen() {
        StdDraw.setCanvasSize(WIDTH * TILE_SIZE, (HEIGHT * TILE_SIZE));
        edu.princeton.cs.introcs.StdDraw.setPenColor(new Color(69, 102, 34));
        edu.princeton.cs.introcs.StdDraw.filledSquare(0, 0, 1);
        edu.princeton.cs.introcs.StdDraw.setPenColor(Color.white);
        edu.princeton.cs.introcs.StdDraw.setFont(TITLE_FONT);
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.75, "CS61B: The Game");
        edu.princeton.cs.introcs.StdDraw.setFont(NORMAL_FONT);
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.3, "New Game: (N)");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.25, "Load Game: (L)");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.2, "Replay Last Game: (R)");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.15, "Quit: (Q)");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.45,
                "Be Warned: Portals Can Teleport You Across The Map");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.5,
                "Objective: Collect " + SCORE_LIMIT +  " Coins Within " +  STEP_LIMIT +  " Steps");

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /** Renders the finish screen depending on whether the user has won the game. */
    public void renderFinishScreen(boolean wonGame, int stepsTaken) {
        if (wonGame) {
            drawWinningScreen(MapGenerator.getWidth(), MapGenerator.getHeight(), stepsTaken);
        } else {
            drawLosingScreen(MapGenerator.getWidth(), MapGenerator.getHeight(), stepsTaken);
        }
    }

    /** Uses StdDraw library to draw a screen associated to the user winning the game. */
    protected void drawWinningScreen(int x, int y, int stepsTaken) {
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
    protected void drawLosingScreen(int x, int y, int stepsTaken) {
        StdDraw.clear();
        StdDraw.setPenColor(new Color(69, 102, 34));
        StdDraw.setFont(TITLE_FONT);
        StdDraw.text(x / 2, y * 0.55, "You lost... Better luck next time!");
        StdDraw.text(x / 2, y * 0.45, "You've exhausted all " + stepsTaken + " steps!");
        StdDraw.text(x / 2, y * 0.1, "Press 'Q' to quit");
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /** Overloads the game with oski. */
    protected void oskiOverload() {
        for (int y = HEIGHT - 1; y >= 0; y--) {
            for (int x = 0; x < WIDTH; x++) {
                engine.world[x][y] = Tileset.OSKI;
            }
            engine.ter.renderFrame(engine.world);
            StdDraw.pause(2);
        }
        StdDraw.pause(1000);
        StdDraw.clear();
        StdDraw.setFont(TITLE_FONT);
        StdDraw.setPenColor(new Color(69, 102, 34));
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Nice try...");
        StdDraw.show();
        StdDraw.pause(1000);
        quitGame();
    }
    
    /** Closes the current game instance. */
    protected void quitGame() {
        System.exit(0);
    }
}
