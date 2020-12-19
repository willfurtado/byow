# Build Your Own World #
**A 2D tile-based game made for Josh Hug's CS61B at the University of California, Berkeley**

A collaborative project by `William Furtado` and `Edward Liu`. Written in Java, our two-dimensional tile-based game finds itself
well within the intersection of Nintendo's *Mario* and Namco's *Pac-Man*. The game supports basic movements using the *'W'*, *'A'*, *'S'*, and *'D'* keys
and special interactions with certain tiles within the world.

Each world is pseudo-randomly generated from a seed entered by the current user; this randomness manifests in a unique player experience every time.
The game supports any seed up Java's maximum long value (9,223,372,036,854,775,807). The world consists of rooms that are connected to each other
via hallways that sprawl across the world.

The objective of the game is to collect **10 coins** in a total of **150 steps**. To add elements of strategy to the game, we have introduced
portals within each room that will randomly transport the current player to another random portal anywhere within the world. Additionally, we have implemented
a **coin exchange**, where a user can press **'E'** to exchanged two of their collected coins for 25 additional steps.

The game supports the creation of new worlds via a random seed, but also supports a `Save and Quit` feature. This allows for a user to save their current game and 
return back at a later time in order to continue. The current state of the world is only saved if the user chooses to do so. Additionally, we have implemented a 
`Replay Mode`, in which a user can "play back" their last saved game and watch their movements.

At all times, the playing screen will have a Heads-Up Display rendered at its top. Thus HUD contains the current number of coins collected, the number of steps remaining,
and the current tile that a user's mouse is currently hovering over.

