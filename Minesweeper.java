public class Minesweeper {
    public static void main (String[] unused) {

        // Initializing our arrays. "minefield[][]" keeps track of where mines are, "uncovered[][]" keeps track of which
        // spaces are uncovered!
        boolean minefield[][] = new boolean[10][10];
        boolean uncovered[][] = new boolean[10][10];

        // Setting up values like the total number of mines, then running setup methods to lay out the mines randomly
        // and draw the empty grid!
        StdOut.println("Welcome to Minesweeper!");
        int NUM_MINES = 10;
        initMinefield(minefield, NUM_MINES);
        drawGrid(minefield, uncovered);

        // This is the main thread, running the mouse click handler and checking if the player has won.
        while (true) {
            handleMouseClick(minefield, uncovered);
            if(hasWon(minefield, uncovered, NUM_MINES)) {
                StdOut.println("You win!");
                StdDraw.picture(5, 5, "kanye.jpg", 8.5, 6.5);
                break;
            }
        }
    }

    // Draw the grid on the canvas.
    public static void drawGrid(boolean[][] minefield, boolean[][] uncovered) {
        // Scaling it from 0 to 10 allows for much easier drawing math than working with decimals between 0 and 1!
        StdDraw.setScale(0, 10);
        StdDraw.setPenRadius(0.002);

        for(int x = 0; x < 10; x++) {
            for(int y = 0; y < 10; y++) {
                if(uncovered[x][y]) {
                    StdDraw.setPenColor(StdDraw.GRAY);
                    StdDraw.filledSquare(x + 0.5, y + 0.5, 0.5);
                    if(countNeighbors(minefield, x, y) != 0) {
                        StdDraw.setPenColor(StdDraw.BLACK);
                        StdDraw.text(x + 0.5, y + 0.5, "" + countNeighbors(minefield, x, y));
                    }
                    if(minefield[x][y]) {
                        StdDraw.setPenColor(StdDraw.RED);
                        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.3);
                        StdDraw.setPenColor(StdDraw.BLACK);
                    }
                }
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.square(x + 0.5, y + 0.5, 0.5);
            }
        }
    }
    // Place mines at random locations on the grid.
    public static void initMinefield(boolean[][] minefield, int numMines) {
        int x;
        int y;
        for(int i = 0; i < numMines; i++) {
            x = StdRandom.uniform(minefield.length);
            y = StdRandom.uniform(minefield[0].length);
            if(minefield[x][y]) {
                i--;
            }
            else minefield[x][y] = true;
        }
    }
    // Detect when the user clicks and where they clicked.
    public static void handleMouseClick(boolean[][] minefield, boolean[][] uncovered) {
        while(!StdDraw.isMousePressed()) {} // do nothing :)
        int mineX = (int) StdDraw.mouseX();
        int mineY = (int) StdDraw.mouseY();
        // Now mineX and mineY are the row and column of the mine that was clicked, respectively.
        uncover(minefield, uncovered, mineX, mineY);
        drawGrid(minefield, uncovered);
    }
    public static void uncover(boolean[][] minefield, boolean[][] uncovered, int x, int y) {
        // Check for number of neighbors
        if(!minefield[x][y] && countNeighbors(minefield, x, y) == 0) {
            percolate(minefield, uncovered, x, y);
        }
        uncovered[x][y] = true;
        if(minefield[x][y]) {
            // Mine go boom :(
            StdOut.println("Boom!");
            return;
        }
    }

    public static int countNeighbors(boolean[][] minefield, int x, int y) {
        // Our total count
        int mineCount = 0;

        // Starting values for the loop, they start at the "bottom left" of the 3x3 grid around the subject square.
        int startX = x - 1;
        int startY = y - 1;
        int endX = x + 2;
        int endY = y + 2;

        // If the subject is at 0 on either axis, set the start values to 0 to avoid an index out of bounds exception!
        if (x == 0) startX = 0;
        if (y == 0) startY = 0;
        if (x == minefield.length - 1) endX = minefield.length;
        if (y == minefield[0].length - 1) endY = minefield[0].length;

        // Looping through both axes of the array, starting on the starting values we've established.
        for (int loopX = startX; loopX < endX; loopX++) {
            for (int loopY = startY; loopY < endY; loopY++) {
                // Of course, if the one we're counting is a mine, add 1 to the mine count.
                // Note: We don't need to check if the mine that's been clicked is a mine, because then we wouldn't need to
                // count its neighbors!
                if (minefield[loopX][loopY]) mineCount++;
            }
        }
        // Return our final count! (This commented out println is for debugging!)
        // StdOut.println("mineCount at " + "[" + x + "][" + y + "] returned as " + mineCount);
        return mineCount;
    }

    // New version of percolate using iteration!
    public static void percolate(boolean[][] minefield, boolean[][]uncovered, int x, int y) {
        if(uncovered[x][y]) return;
        assert !uncovered[x][y];
        uncovered[x][y] = true;
        if(countNeighbors(minefield, x, y) == 0) {
            for(int x1 = x - 1; x1 <= x + 1; x1++) {
                for(int y1 = y - 1; y1 <= y + 1; y1++) {
                    if(x1 >= 0 && x1 < minefield.length &&
                        y1 >= 0 && y1 < minefield.length &&
                        (x1 != x || y1 != y)) {
                            uncover(minefield, uncovered, x1, y1);
                    }
                }
            }
        }
    }

    // Deprecated version of percolate using iteration
//    private static void percolate(boolean[][] minefield, boolean[][]uncovered, int x, int y) {
//        // Most of this is copied from the neighbors loop
//        int startX = x - 1;
//        int startY = y - 1;
//        int endX = x + 2;
//        int endY = y + 2;
//
//        // If the subject is at 0 on either axis, set the start values to 0 to avoid an index out of bounds exception!
//        if (x == 0) startX = 0;
//        if (y == 0) startY = 0;
//        if (x == minefield.length - 1) endX = minefield.length;
//        if (y == minefield[0].length - 1) endY = minefield[0].length;
//
//        // Looping through both axes of the array, starting on the starting values we've established.
//        for (int loopX = startX; loopX < endX; loopX++) {
//            for (int loopY = startY; loopY < endY; loopY++) {
//                // Check if the mine is uncovered, if not, then uncover it! We've already confirmed that it has
//                // no neighbors!
//                if(!uncovered[loopX][loopY]) {
//                    uncover(minefield, uncovered, loopX, loopY);
//                }
//            }
//        }
//    }

    // Detect when the game has been won (ALl spaces without mines have been revealed)
    private static boolean hasWon(boolean[][] minefield, boolean[][] uncovered, int NUM_MINES) {
        for(int x = 0; x < NUM_MINES; x++) {
            for(int y = 0; y < NUM_MINES; y++) {
                if(uncovered[x][y] && minefield[x][y]) {
                    return false;
                }
                if(!uncovered[x][y] && !minefield[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }
}
