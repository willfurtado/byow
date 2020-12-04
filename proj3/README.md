# Build Your Own World Design Document

**Partner 1: `William Furtado`**

**Partner 2: `Edward Liu`**

## Classes and Data Structures

Classes include *Main*, *Engine*, *MapGenerator*, *Room*, and *Point*.

#### MapGenerator
The `MapGenerator` class is largely responsible for the random generation
of the world (a 2d-Array of Tiles). Its constructor takes in a `SEED` provided by the 
user input, and then creates a random world using that seed.

**Instance variables**: int WIDTH of the world window, int HEIGHT of the world window, Random random to generate pseudorandom numbers, and ArrayList<Room> room that keeps track of all Room objects.

#### Room
The `Room` class stores information about the rooms that occupy space within the world.
Each `Room` instance has variables describing its dimensions, and maps its four
corners to instance variables to account for overlap.

**Instance variables**: int width of the room, int height of the room, Point lowerLeft corner, Point upperRight corner, Point upperLeft corner, Point lowerRight corner, Point horizontalDoor of a door that is oriented horizontally, Point verticalDoor of a door that is oriented vertically, and Random random to generate pseudorandom numbers.

#### Point
The `Point` class is representative of a given location within the world. It represents
an (x,y) pair and contains the corresponding instance attributes to do so.

**Instance variables**: int x specifying the horizontal axis location and int y specifying the vertical axis location.

## Algorithms

#### MapGenerator
1. **Constructor**: Takes a seed (long) that will be used to deterministically create
the MapGenerator object. 

2. **createWorld**: Returns a 2D array of Tiles that represents the 
world after adding a specified number of random rooms and their corresponding hallways.

3. **setEmptyWorld**: Takes in a 2D array of TETiles and iterates through all tiles within the world and fills them with Tileset.WATER.

4. **setCorners**: Takes in a 2D array of TETiles and a singular TETile and sets all corners of every Room within the ArrayList of Rooms to the given TETile.

5. **fixDoors**: Takes in a 2D array of TETiles and sets all doors of every Room within the MapGenerator instance to Tileset.UNLOCKED_DOOR.

6. **fixTilesAroundHorizontalDoor**: Takes in a 2D array of TETiles and a Point and checks each of the diagonal tiles respective to the given Point to see if they are Tileset.WATER. If this is the case, change them to Tileset.TREE.

7. **fixTilesAroundVerticalDoor**: Takes in a 2D array of TETiles and a Point and checks each of the diagonal tiles respective to the given Point to see if they are Tileset.WATER. If this is the case, change them to Tileset.TREE.

8. **addNRooms**: Takes in a 2D array of TETiles and an int (numRooms) and adds numRooms number of random Rooms to the given world.

9. **createAllHallways**: Takes in a 2D array of TETiles and creates all the hallway paths stemming from all the open doors within each Room object.

10. **generateRandomRoom**: Returns a Room of random width and height using the seed from the MapGenerator class.

11. **isValidRoom**: Takes in a Room object and returns whether or not the Room should be added to the world.

12. **addRandomRoom**: Takes in a 2D array of TETiles and adds a random Room instance to its respective location using its lowerLeft and upperRight corners. Using the seed from the MapGenerator class, this method will generator a random room, and then using that random Room's dimensions and corners, add the appropriate tiles to the world.

13. **createFloor**: Takes in a 2D array of TETiles and a Room object and lays down all Tileset.GRASS tiles onto the given area of a Room instance.

14. **drawVerticalWalls**: Takes in a 2D array of TETiles and a Room object and draws the vertical walls of the given Room instance.

15. **drawHorizontalWalls**: Takes in a 2D array of TETiles and a Room object and draws the horizontal walls of the given Room instance.

16. **openDoors**: Takes in a 2D array of TETiles and a Room object and opens the tile at the given Room's door Point position by making it Tileset.MOUNTAIN.

17. **tunnelLeft**: Takes in a 2D array of TETiles and a Room object and starting at the given door of the Room, builds a hallway to the left until it arrives at a wall (Tileset.TREE) or at the edge of the world window.

18. **tunnelRight**: Takes in a 2D array of TETiles and a Room object and starting at the given door of the Room, builds a hallway to the right until it arrives at a wall (Tileset.TREE) or at the edge of the world window.

19. **tunnelUp**: Takes in a 2D array of TETiles and a Room object and starting at the given door of the Room, builds a hallway up until it arrives at a wall (Tileset.TREE) or at the edge of the world window.

20. **tunnelDown**: Takes in a 2D array of TETiles and a Room object and starting the given door of the Room, builds a hallway down until it arrives at a wall (Tileset.TREE) or at the edge of the world window.

21. **fixCorners**: Takes in a 2D array of TETiles and two ints (x and y) and fixes hallways that travel directly into the corners of a given room.

#### Room
1. **Constructor**: The Room constructor takes in a `Random` object that will be used
to randomly determine the dimensions and starting location of a given room. Rooms are
constructed with minimum and maximum side lengths set.

2. **randomSideLength**: Returns a random side length (Integer) that is between the specified minimum and maximum range.

3. **randomStartingPoint**: Returns a random starting Point object that serves as the Room's lowerLeft corner.

4. **getUpperRight**: Takes in an int inputWidth, int inputHeight, and Point inputLowerLeft and returns the corresponding upperRight Point of a room given its width, height, and its lowerLeft Point.

5. **getUpperLeft**: Takes in an int inputHeight and Point inputLowerLeft and returns the corresponding upperLeft Point of a Room.

6. **getLowerRight**: Takes in an int inputWidth and Point inputLowerLeft and returns the corresponding lowerRight Point of a Room.

7. **isInside**: Takes in a Point and returns a boolean describing whether the Point lies within the corners of a Room.

8. **sideOverlap**: Takes in a Room and returns a boolean describing whether the input Room possesses no corners that lie within the corners of another Room but still overlaps.

9. **getHorizontalDoor**: Returns the Point of a Room's horizontally-facing door.

10. **getVerticalDoor**: Returns the Point of a Room's vertically-facing door.

#### Point
1. **Constructor**: The Point constructor takes in an int xPos and int yPos and sets the Point object's x and y instance variables to those passed in.

2. **getX:** Returns the x-value that is mapped to a given `Point` instance.

3. **getY:** Returns the y-value that is mapped to a given `Point` instance.

## Persistence

[TBD]
