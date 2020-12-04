package byow.Core;

/**
 * Represents a given element within the 2D-Array of Tilset objects. Coordinates.
 */
public class Point {
    int x;
    int y;

    /** Creates a point object with associated x and y values. */
    public Point(int xPos, int yPos) {
        this.x = xPos;
        this.y = yPos;
    }

    /** Return the x value of the associated Point. */
    public int getX() {
        return this.x;
    }

    /** Return the y value of the associated Point. */
    public int getY() {
        return this.y;
    }
}
