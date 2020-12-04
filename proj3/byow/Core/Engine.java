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
    public static final int WIDTH = MapGenerator.WIDTH;
    public static final int HEIGHT = MapGenerator.HEIGHT;
    public static final Font TITLE_FONT = new Font("Source Code Pro", Font.BOLD, 40);
    public static final Font NORMAL_FONT = new Font("Source Code Pro", Font.PLAIN, 17);
    private boolean worldInitialized = false;
    private long SEED;
    private MapGenerator mapGen;
    private TETile[][] world;
    private String userInput = "";
    private boolean quitCheck = false;
    private boolean replayMode = false;
    private boolean gameOver = false;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputSource inputSource = new KeyboardInputSource();
        String seed = "";
        drawLoadingScreen();

        while (inputSource.possibleNextInput()) {
            char nextKey = inputSource.getNextKey();
            if (!worldInitialized) {
                if (nextKey == 'N') {
                    saveWorld("");
                    inputSource = new KeyboardInputSource();
                    drawSeedPromptScreen(seed);
                    nextKey = inputSource.getNextKey();
                    while (nextKey != 'S') {
                        seed += nextKey;
                        drawSeedPromptScreen(seed);
                        nextKey = inputSource.getNextKey();
                    }
                    this.SEED = Long.parseLong(seed);
                    userInput += this.SEED + ":";
                    drawGameScreen();
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
                ter.renderHUDFinish();
            }

            while (!StdDraw.hasNextKeyTyped() && !replayMode
                    && mapGen.score != MapGenerator.SCORELIMIT
                    && !gameOver) {
                ter.renderHUD(world, mapGen.getScore(), mapGen.getStepsRemaining());
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
            drawReplayLoadingScreen();
            InputSource loadingSource = new KeyboardInputSource();
            if (loadingSource.getNextKey() == 'R') {
                ter.initialize(WIDTH, HEIGHT);
                replayMode = true;
                mapGen = new MapGenerator(Long.parseLong(scannedSeed));
                world = mapGen.createWorld();
                ter.renderFrame(world);
                ter.renderHUDReplay(world, mapGen.score, mapGen.stepsRemaining);

                while (!StdDraw.hasNextKeyTyped()) {
                    ter.renderHUDReplay(world, mapGen.score, mapGen.stepsRemaining);
                }

                for (int i = 1; i < scannedMovement.length(); i++) {
                    InputSource inputSource = new KeyboardInputSource();
                    char nextMove = inputSource.getNextKey();
                    if (nextMove == 'C') {
                        char c = scannedMovement.charAt(i);
                        moveAvatar(world, mapGen, c, true);
                        ter.renderHUDReplay(world, mapGen.score, mapGen.stepsRemaining);
                    }
                    if (nextMove == 'Q') {
                        quitGame();
                    }

                }
                if (!StdDraw.hasNextKeyTyped()) {
                    ter.renderHUDFinishReplay();
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

    private void drawReplayLoadingScreen() {
        edu.princeton.cs.introcs.StdDraw.clear();
        edu.princeton.cs.introcs.StdDraw.setPenColor(new Color(69, 102, 34));
        edu.princeton.cs.introcs.StdDraw.filledSquare(0, 0, 1);
        edu.princeton.cs.introcs.StdDraw.setPenColor(Color.white);
        edu.princeton.cs.introcs.StdDraw.setFont(TITLE_FONT);
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.75, "Instant Replay Zone");
        edu.princeton.cs.introcs.StdDraw.setFont(NORMAL_FONT);
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.40, "Press 'R' to enter Replay Mode");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.15, "While in Replay Mode, "
                + "press 'C' to see next move");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.10, "While in Replay Mode, "
                + "press 'Q' to quit the game");
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
            ter.renderHUD(inWorld, inMapGen.score, inMapGen.stepsRemaining);
        }
        if (inMapGen.score == MapGenerator.SCORELIMIT) {
            ter.renderFinishScreen(true);
            gameOver = true;
        } else if (inMapGen.stepsRemaining <= 0) {
            gameOver = true;
            saveWorld("");
            ter.renderFinishScreen(false);

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

    /** Outputs the initial loading screen of our world generation game. */
    private void drawLoadingScreen() {
        edu.princeton.cs.introcs.StdDraw.setPenColor(new Color(69, 102, 34));
        edu.princeton.cs.introcs.StdDraw.filledSquare(0, 0, 1);
        edu.princeton.cs.introcs.StdDraw.setPenColor(Color.white);
        edu.princeton.cs.introcs.StdDraw.setFont(TITLE_FONT);
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.65, "CS61B: The Game");
        edu.princeton.cs.introcs.StdDraw.setFont(NORMAL_FONT);
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.35, "New Game: (N)");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.3, "Load Game: (L)");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.25, "Replay Last Game: (R)");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.2, "Quit: (Q)");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.5,
                "Objective: collect 5 coins within 75 steps");
    }

    /** Draws the initial landing screen to the page. */
    private void drawSeedPromptScreen(String seed) {
        edu.princeton.cs.introcs.StdDraw.clear();
        edu.princeton.cs.introcs.StdDraw.setPenColor(new Color(69, 102, 34));
        edu.princeton.cs.introcs.StdDraw.filledSquare(0, 0, 1);
        edu.princeton.cs.introcs.StdDraw.setPenColor(Color.white);
        edu.princeton.cs.introcs.StdDraw.setFont(NORMAL_FONT);
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.75, "Enter Random Number:");
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.5, seed);
        edu.princeton.cs.introcs.StdDraw.text(0.5, 0.25, "Press 'S' to Create World");
    }

    /** Initializes the TERenderer object using respective WIDTH, HEIGHT and renders the world. */
    private void drawGameScreen() {
        ter.initialize(WIDTH, HEIGHT);
        this.mapGen = new MapGenerator(this.SEED);
        this.world =  mapGen.createWorld();
        worldInitialized = true;
        ter.renderFrame(world);
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
}
