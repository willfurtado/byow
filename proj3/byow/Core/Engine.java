package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Engine {
    TERenderer ter = new TERenderer();
    HUD hud = new HUD();

    public static final int WIDTH = MapGenerator.WIDTH;
    public static final int HEIGHT = MapGenerator.HEIGHT;
    protected static final int STEP_LIMIT = MapGenerator.STEPLIMIT;
    protected static final int SCORE_LIMIT = MapGenerator.SCORELIMIT;
    protected static final int TILE_SIZE = 16;
    protected static final Font TITLE_FONT = new Font("Georgia", Font.BOLD, 40);
    protected static final Font NORMAL_FONT = new Font("Georgia", Font.PLAIN, 17);

    protected long SEED;
    protected MapGenerator mapGen;
    protected TETile[][] world;
    protected String userInput = "";
    protected boolean quitCheck = false;
    protected boolean replayMode = false;
    protected boolean gameOver = false;
    protected boolean worldInitialized = false;

    ScreenDrawer drawer = new ScreenDrawer(this);

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputSource inputSource = new KeyboardInputSource();
        String seed = "";
        drawer.drawLoadingScreen();

        while (inputSource.possibleNextInput()) {
            char nextKey = inputSource.getNextKey();
            if (!worldInitialized) {

                if (nextKey == 'N') {
                    saveWorld("");
                    inputSource = new KeyboardInputSource();
                    drawer.drawSeedPromptScreen(seed);
                    nextKey = inputSource.getNextKey();
                    while (nextKey != 'S') {
                        seed += nextKey;
                        drawer.drawSeedPromptScreen(seed);
                        nextKey = inputSource.getNextKey();
                    }
                    this.SEED = Long.parseLong(seed);
                    userInput += this.SEED + ":";
                    drawer.drawGameScreen();
                } else if (nextKey == 'L') {
                    loadPreviousGame(true);
                } else if (nextKey == 'R') {
                    replaySavedGame();
                } else if (nextKey == 'Q') {
                    quitGame();
                }
            }

            userInput += nextKey;
            if (!replayMode && !gameOver) {
                moveAvatar(world, mapGen, nextKey, true);
            }
            if (nextKey == 'E') {
                mapGen.purchaseSteps();
            }
            if (nextKey == 'O') {
                drawer.oskiOverload();
            }
            if (nextKey == ':') {
                this.quitCheck = true;
            }
            if (quitCheck && nextKey == 'Q') {
                saveWorld(userInput);
                quitGame();
            }
            if (gameOver && nextKey == 'Q') {
                quitGame();
            }
            if (gameOver && !replayMode) {
                hud.renderHUDFinish();
            }

            while (!StdDraw.hasNextKeyTyped() && !replayMode
                    && mapGen.score != MapGenerator.SCORELIMIT
                    && !gameOver) {
                hud.renderHUD(world, mapGen.getScore(), mapGen.getStepsRemaining());
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        InputSource inputSource = new StringInputDevice(input);
        String seed = "";

        while (inputSource.possibleNextInput()) {
            char nextKey = inputSource.getNextKey();

            if (!worldInitialized) {
                if (nextKey == 'N') {
                    saveWorld("");
                    nextKey = inputSource.getNextKey();
                    while (nextKey != 'S') {
                        seed += nextKey;
                        nextKey = inputSource.getNextKey();
                    }
                    this.SEED = Long.parseLong(seed);
                    this.mapGen = new MapGenerator(this.SEED);
                    this.world = mapGen.createWorld();
                    userInput += this.SEED + ":";
                    worldInitialized = true;
                } else if (nextKey == 'L') {
                    loadPreviousGame(false);
                } else if (nextKey == 'R') {
                    inputSource.getNextKey();
                    replaySavedGameString(input.substring(1));
                }
            }
            userInput += nextKey;
            if (!replayMode && !gameOver) {
                moveAvatar(world, mapGen, nextKey, false);
            }
            if (nextKey == ':') {
                quitCheck = true;
            }
            if (quitCheck && nextKey == 'Q') {
                saveWorld(userInput);
            }
        }
        return world;
    }

    /** Given a string of seeds and movements delimited by a colon, this method will return
     *  the corresponding saved world for a given input String. Avoids the recursive case
     *  when loading a new world. */
    public void loadPreviousWorldString(String input) {
        InputSource inputSource = new StringInputDevice(input);
        String seed = "";

        while (inputSource.possibleNextInput()) {
            char nextKey = inputSource.getNextKey();
            if (nextKey == 'N') {
                nextKey = inputSource.getNextKey();
                while (nextKey != 'S') {
                    seed += nextKey;
                    nextKey = inputSource.getNextKey();
                }

                this.SEED = Long.parseLong(seed);
                this.mapGen = new MapGenerator(this.SEED);
                this.world =  mapGen.createWorld();
                worldInitialized = true;
            }
            if (worldInitialized) {
                moveAvatar(world, mapGen, nextKey, false);
            }
        }
    }

    /** Replays a saved game instance by working through all the moves. */
    private void replaySavedGame() {
        try {
            Scanner scanner = new Scanner(new File("savedWorld.txt"));
            scanner.useDelimiter(":");
            String scannedSeed = "";
            String scannedMovement = "";
            while (scanner.hasNext()) {
                scannedSeed = scanner.next();
                scannedMovement = scanner.next();
                scanner.next(); // removes the Q
            }
            drawer.drawReplayLoadingScreen();
            InputSource loadingSource = new KeyboardInputSource();
            if (loadingSource.getNextKey() == 'R') {
                ter.initialize(WIDTH, HEIGHT);
                replayMode = true;
                mapGen = new MapGenerator(Long.parseLong(scannedSeed));
                world = mapGen.createWorld();
                ter.renderFrame(world);
                hud.renderHUDReplay(world, mapGen.score, mapGen.stepsRemaining);

                while (!StdDraw.hasNextKeyTyped()) {
                    hud.renderHUDReplay(world, mapGen.score, mapGen.stepsRemaining);
                }

                for (int i = 1; i < scannedMovement.length(); i++) {
                    InputSource inputSource = new KeyboardInputSource();
                    char nextMove = inputSource.getNextKey();
                    if (nextMove == 'C') {
                        char c = scannedMovement.charAt(i);
                        moveAvatar(world, mapGen, c, true);
                        hud.renderHUDReplay(world, mapGen.score, mapGen.stepsRemaining);
                    }
                    if (nextMove == 'Q') {
                        quitGame();
                    }

                }
                if (!StdDraw.hasNextKeyTyped()) {
                    hud.renderHUDFinishReplay();
                }
            }

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /** Replays a saved game instance by working through all the moves given an input String. */
    private void replaySavedGameString(String s) {
        try {
            Scanner scanner = new Scanner(new File("savedWorld.txt"));
            scanner.useDelimiter(":");
            String scannedSeed = "";
            String scannedMovement = "";
            while (scanner.hasNext()) {
                scannedSeed = scanner.next();
                scannedMovement = scanner.next();
                scanner.next(); // removes the Q
            }
            InputSource input = new StringInputDevice(s);
            if (input.getNextKey() == 'R') {
                replayMode = true;
                mapGen = new MapGenerator(Long.parseLong(scannedSeed));
                world = mapGen.createWorld();

                int scanIndex = 0;
                while (scanIndex < scannedMovement.length() && input.possibleNextInput()) {
                    char nextMove = input.getNextKey();
                    if (nextMove == 'C') {
                        char c = scannedMovement.charAt(scanIndex);
                        moveAvatar(world, mapGen, c, false);
                        scanIndex++;
                    }
                    if (nextMove == 'Q') {
                        break;
                    }
                }
            }

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /** Moves the avatar pieces within the given inputWorld using the given input MapGenerator
     * object and will render depending on the boolean passed into the method. */
    private void moveAvatar(TETile[][] inWorld, MapGenerator inMapGen, char key, boolean render) {
        if (key == 'W') {
            inWorld = inMapGen.moveAvatarUp(inWorld);
        } else if (key == 'A') {
            inWorld = inMapGen.moveAvatarLeft(inWorld);
        } else if (key == 'S') {
            inWorld = inMapGen.moveAvatarDown(inWorld);
        } else if (key == 'D') {
            inWorld = inMapGen.moveAvatarRight(inWorld);
        }
        if (render) {
            ter.renderFrame(inWorld);
            hud.renderHUD(inWorld, inMapGen.score, inMapGen.stepsRemaining);
        }
        if (inMapGen.score == MapGenerator.SCORELIMIT) {
            int stepsTaken = STEP_LIMIT - inMapGen.stepsRemaining;
            drawer.renderFinishScreen(true, stepsTaken);
            gameOver = true;
        } else if (inMapGen.stepsRemaining <= 0) {
            gameOver = true;
            saveWorld("");
            drawer.renderFinishScreen(false, STEP_LIMIT);

        }
    }

    /** Using the contents of the text file savedWorld, this method will read off the current
     * seed and the given moves that the user took previously. This will also render the frame. */
    private void loadPreviousGame(boolean render) {
        try {
            if (render) {
                ter.initialize(WIDTH, HEIGHT);
            }
            Scanner scanner = new Scanner(new File("savedWorld.txt"));
            scanner.useDelimiter(":");
            String scannedSeed = "";
            String scannedMovement = "";
            while (scanner.hasNext()) {
                scannedSeed = scanner.next();
                scannedMovement = scanner.next();
                scanner.next(); // removes the Q
            }
            loadPreviousWorldString("N" + scannedSeed + "S" + scannedMovement);
            if (render) {
                ter.renderFrame(world);
            }
            userInput += scannedSeed + ":" + scannedMovement;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Saves the given content (in the form of a String) to a file called savedWorld.txt. */
    private void saveWorld(String content) {
        try {
            FileWriter fileWriter = new FileWriter("savedWorld.txt");
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Exits the current game. */
    private void quitGame() {
        System.exit(0);
    }
}
