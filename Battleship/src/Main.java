import java.time.Duration;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int gridLength = 10;
        int[][] AIGrid = new int[gridLength][gridLength];
        int[][] userGrid = new int[gridLength][gridLength];
        int[] ships = {2, 3, 3, 4, 5};

        startScreen();
        drawAIShips(gridLength, AIGrid, ships);

        boolean validInput = true;
        int randOrManual = 0;
        do {
            validInput = true;
            System.out.print("Would you like to enter the ships manually (1) or random (2): ");
            try {
                randOrManual = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input!!");
                sc.nextLine();
                validInput = false;
            }
            if (randOrManual != 1 && randOrManual != 2 && validInput) {
                System.out.println("Please enter a valid option!!");
                validInput = false;
            }
        } while (!validInput);
        if (randOrManual == 1) {
            drawUserShips(gridLength, userGrid, ships);
        } else {
            drawAIShips(gridLength, userGrid, ships);
            System.out.println("This is your grid");
            showGraphicsGrid(userGrid);
        }

        System.out.println(LINE);
        //showGraphicsGrid(AIGrid);
        gameLoop(gridLength, AIGrid, userGrid, ships);
    }

    private static void gameLoop(int gridLength, int[][] AIGrid, int[][] userGrid, int[] ships) {
        int[][] userShotsGrid = new int[gridLength][gridLength];
        int[][] AIShotsGrid = new int[gridLength][gridLength];
        Scanner sc = new Scanner(System.in);
        boolean AIWin = false;
        boolean userWin = false;

        System.out.println(LINE);
        while (!AIWin && !userWin) {
            System.out.println("Now is your turn to shot!!");
            showGraphicsGrid(userShotsGrid);
            handleUserShot(gridLength, AIGrid, userShotsGrid);

            userWin = checkIfWin(AIGrid);

            if (!userWin) {
                System.out.println("Now is the AI turn!!");
                handleAIShot(gridLength, userGrid, AIShotsGrid);

                AIWin = checkIfWin(userGrid);
            }

            changeExplosionToHitGrid(userShotsGrid);
            changeExplosionToHitGrid(AIShotsGrid);
            changeExplosionToHitGrid(userGrid);
            changeExplosionToHitGrid(AIGrid);
        }

        if (userWin) {
            System.out.println("You win!");
        } else if (AIWin) {
            System.out.println("You lose!");
        }
        System.out.println(LINE);
        System.out.println("Your grid:");
        showGraphicsGrid(userGrid);
        System.out.println(LINE);
        System.out.println("AI grid:");
        showGraphicsGrid(AIGrid);
        System.out.println(LINE);
    }

    private static boolean checkIfWin(int[][] grid) {
        boolean win = true;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] > 0) {
                    win = false;
                }
            }
        }

        return win;
    }

    private static void changeExplosionToHitGrid(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == -1) {
                    grid[i][j] = -2;
                }
            }
        }
    }

    private static void handleAIShot(int gridLength, int[][] userGrid, int[][] AIShotsGrid) {
        Random rand = new Random();
        int shotX = 0;
        int shotY = 0;
        boolean hit = false;
        do {
            System.out.println("Shooting...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("The pause failed");
            }
            hit = false;
            boolean alreadyShot = false;
            do {
                alreadyShot = false;
                shotX = rand.nextInt(gridLength);
                shotY = rand.nextInt(gridLength);

                if (AIShotsGrid[shotY][shotX] < 0) {
                    alreadyShot = true;
                }
            } while (alreadyShot);

            if (userGrid[shotY][shotX] >= 1 && AIShotsGrid[shotY][shotX] >= 0) {
                System.out.println("The AI hit you!!!");
                AIShotsGrid[shotY][shotX] = -1;
                userGrid[shotY][shotX] = -1;
                hit = true;
            } else if (userGrid[shotY][shotX] == 0) {
                System.out.println("The AI missed");
                AIShotsGrid[shotY][shotX] = -3;
                userGrid[shotY][shotX] = -3;
            }
            System.out.println(LINE);
            System.out.println("Your grid:");
            showGraphicsGrid(userGrid);
            System.out.println(LINE);
        } while (hit);
    }

    private static void handleUserShot(int gridLength, int[][] AIGrid, int[][] userShotsGrid) {
        int shotX = 0;
        int shotY = 0;
        boolean hit = false;
        do {
            hit = false;
            boolean alreadyShot = false;
            do {
                alreadyShot = false;
                shotX = inputXCoordinate(gridLength);
                shotY = inputYCoordinate(gridLength);

                if (userShotsGrid[shotY][shotX] < 0) {
                    System.out.println("You have already shot here!!");
                    alreadyShot = true;
                }
            } while (alreadyShot);

            if (AIGrid[shotY][shotX] >= 1 && userShotsGrid[shotY][shotX] >= 0) {
                System.out.println("You hit a ship!!");
                userShotsGrid[shotY][shotX] = -1;
                AIGrid[shotY][shotX] = -1;
                hit = true;
            } else if (AIGrid[shotY][shotX] == 0) {
                System.out.println("You missed...");
                userShotsGrid[shotY][shotX] = -3;
                AIGrid[shotY][shotX] = -3;
            }
            System.out.println(LINE);
            System.out.println("Your shots:");
            showGraphicsGrid(userShotsGrid);
            System.out.println(LINE);
        } while (hit);
    }

    private static void startScreen() {
        System.out.println();
        System.out.println(LINE);
        System.out.println("⚓️ Welcome to Battleship! ⚓️");
        System.out.println(LINE);
        System.out.println();
        System.out.println("Get ready for an intense naval battle!");
        System.out.println("Your mission is to locate and destroy all enemy ships hidden on the grid.");
        System.out.println("Each ship is represented by a different color.");
        System.out.println();
        System.out.println("Grid Symbols:");
        System.out.println("🟥, 🟩, 🟦, 🟨, 🟪 - Ships (of different colors)");
        System.out.println("\uD83D\uDCA5 - Explosion");
        System.out.println("❌ - Ship hit");
        System.out.println();
        System.out.println("Game Rules:");
        System.out.println("1. The grid is labeled with letters and numbers for easy targeting.");
        System.out.println("2. Place your ships.");
        System.out.println("3. Enter coordinates to fire at a location (e.g., A5, C3).");
        System.out.println("4. Hit all parts of a ship to sink it.");
        System.out.println("5. The game ends when your opponents ships or yours haves sink.");
        System.out.println();
        System.out.println("Good luck, Admiral! May the seas be in your favor!");
        System.out.println();
    }

    public static final String LINE = "-----------------------------------------------------";

    private static void drawUserShips(int gridLength, int[][] grid, int[] ships) {
        Scanner sc = new Scanner(System.in);
        int[][] auxiliarGrid = new int[gridLength][gridLength];

        System.out.println(LINE);
        System.out.println("Now we are going to place your ships on the grid");

        for (int shipLength : ships) {
            boolean valid = false;
            int[][] allShipCoordinates = new int[shipLength][2];
            do {
                System.out.println(LINE);
                System.out.println("Let's place a " + shipLength + " block ship:");
                System.out.println(LINE);

                int[] shipCoordinates = new int[2];
                shipCoordinates[1] = inputXCoordinate(gridLength);
                shipCoordinates[0] = inputYCoordinate(gridLength);
                boolean validInitialCoordinates = shipCoordinates[0] >= 0 && shipCoordinates[1] >= 0 && shipCoordinates[0] < gridLength && shipCoordinates[1] < gridLength && grid[shipCoordinates[0]][shipCoordinates[1]] == 0;
                if (validInitialCoordinates) {
                    System.out.println("Initial coordinates of the ship will be placed here:");
                    auxiliarGrid[shipCoordinates[0]][shipCoordinates[1]] = shipLength;
                    showGraphicsGrid(auxiliarGrid);
                    System.out.println(LINE);
                    int shipDirectionAxis = inputShipDirectionAxis();
                    System.out.println(LINE);
                    int shipDirection = inputShipDirection(shipDirectionAxis);
                    valid = checkIfValidShipPosition(gridLength, grid, shipLength, shipDirectionAxis, shipDirection, shipCoordinates, allShipCoordinates);
                }
                if (!valid) {
                    System.out.println(LINE);
                    System.out.println("It's not possible to place a ship in this position.");
                }
                if (!valid && validInitialCoordinates) {
                    auxiliarGrid[shipCoordinates[0]][shipCoordinates[1]] = 0;
                }
            } while (!valid);
            System.out.println(LINE);
            System.out.println("This is the current state of your grid.");
            drawShip(grid, allShipCoordinates, shipLength);
            showGraphicsGrid(grid);
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    auxiliarGrid[i][j] = grid[i][j];
                }
            }
        }
        System.out.println(LINE);
        System.out.println("All ships has been placed correctly!");
        System.out.println(LINE);
    }

    private static int inputShipDirection(int shipDirectionAxis) {
        Scanner sc = new Scanner(System.in);
        int shipDirection = -1;
        boolean validDirection = true;
        do {
            if (shipDirectionAxis == 0) {
                System.out.print("Do you want the ship placed to the right (1) or to the left (2)?: ");
            } else if (shipDirectionAxis == 1) {
                System.out.print("Do you want the ship placed downwards (1) or upwards (2)?: ");
            }
            try {
                shipDirection = sc.nextInt() - 1;
                validDirection = true;
            } catch (InputMismatchException e) {
                System.out.println("Enter a valid input!");
                validDirection = false;
                sc.nextLine();
            }
            ;
            if (shipDirection != 0 && shipDirection != 1 && validDirection) {
                System.out.println("Please enter a value between 1 and 2.");
                validDirection = false;
            }
        } while (!validDirection);

        return shipDirection;
    }

    private static int inputShipDirectionAxis() {
        Scanner sc = new Scanner(System.in);
        int shipDirectionAxis = -1;
        boolean validDirectionAxis = true;
        do {
            System.out.print("Do you want the ship placed horizontal (1) or vertical (2)?: ");
            try {
                shipDirectionAxis = sc.nextInt() - 1;
                validDirectionAxis = true;
            } catch (InputMismatchException e) {
                System.out.println("Enter a valid input!");
                validDirectionAxis = false;
                sc.nextLine();
            }
            ;
            if (shipDirectionAxis != 0 && shipDirectionAxis != 1 && validDirectionAxis) {
                System.out.println("Please enter a value between 1 and 2.");
                validDirectionAxis = false;
            }
        } while (!validDirectionAxis);

        return shipDirectionAxis;
    }

    private static int inputYCoordinate(int gridLength) {
        Scanner sc = new Scanner(System.in);
        boolean validInput = true;
        char yCoordinateChar;
        int yCoordinate = -1;
        do {
            System.out.print("Enter the coordinate y (A - J): ");
            try {
                yCoordinateChar = sc.nextLine().toLowerCase().charAt(0);
                yCoordinate = convertCharToInt(yCoordinateChar) - 1;
                validInput = true;
            } catch (InputMismatchException | StringIndexOutOfBoundsException e) {
                validInput = false;
                System.out.println("Pleas enter a valid input!");
            }
            if (yCoordinate < 0 || yCoordinate > 9) {
                System.out.println("Pleas enter letter between A and J.");
                validInput = false;
            }
        } while (!validInput);
        return yCoordinate;
    }

    private static int convertCharToInt(char charToChange) {
        int charNumberValue = 0;
        switch (charToChange) {
            case 'a':
                charNumberValue = 1;
                break;
            case 'b':
                charNumberValue = 2;
                break;
            case 'c':
                charNumberValue = 3;
                break;
            case 'd':
                charNumberValue = 4;
                break;
            case 'e':
                charNumberValue = 5;
                break;
            case 'f':
                charNumberValue = 6;
                break;
            case 'g':
                charNumberValue = 7;
                break;
            case 'h':
                charNumberValue = 8;
                break;
            case 'i':
                charNumberValue = 9;
                break;
            case 'j':
                charNumberValue = 10;
                break;
            default:
                charNumberValue = -1;
                break;
        }
        return charNumberValue;
    }

    private static int inputXCoordinate(int gridLength) {
        Scanner sc = new Scanner(System.in);
        boolean validInput = true;
        int xCoordinate = -1;
        do {
            System.out.print("Enter the coordinate x (1-10): ");
            try {
                xCoordinate = sc.nextInt() - 1;
                validInput = true;
            } catch (InputMismatchException e) {
                validInput = false;
                System.out.println("Pleas enter a valid input!");
                sc.nextLine();
            }
            if ((xCoordinate < 0 || xCoordinate >= gridLength) && validInput) {
                validInput = false;
                System.out.println("Pleas enter a value between 1 and 10.");
            }
        } while (!validInput);

        return xCoordinate;
    }

    private static void showGraphicsGrid(int[][] grid) {
        String[] numberEmotes = {"1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "🔟"};
        String[] legend = {"Legend: ", "\uD83D\uDFE6: Water", "\uD83D\uDFE5: 2 Block Ship", "\uD83D\uDFE9: 3 Block Ship", "\uD83D\uDFE8: 4 Block Ship", "\uD83D\uDFEA: 5 Block Ship", "❌: Ship hit, ", "\uD83D\uDCA5: Explosion", "⭕: Missed"};

        System.out.printf("%-3s", "");
        int numColumns = Math.min(grid[0].length, numberEmotes.length);
        for (int col = 0; col < numColumns; col++) {
            System.out.printf("%-4s", numberEmotes[col]);
        }
        System.out.println();

        for (int i = 0; i < grid.length; i++) {
            System.out.printf("%-2s ", (char) ('A' + i));
            for (int j = 0; j < grid[i].length; j++) {
                switch (grid[i][j]) {
                    case -3:
                        System.out.printf("%-2s", "⭕"); // Missed
                        break;
                    case -2:
                        System.out.printf("%-2s", "❌"); // Hit
                        break;
                    case -1:
                        System.out.printf("%-3s", "\uD83D\uDCA5"); // Explosion
                        break;
                    case 0:
                        System.out.printf("%-3s", "\uD83D\uDFE6"); // Blue
                        break;
                    case 1:
                        System.out.printf("%-3s", "1");
                        break;
                    case 2:
                        System.out.printf("%-3s", "\uD83D\uDFE5"); // Red
                        break;
                    case 3:
                        System.out.printf("%-3s", "\uD83D\uDFE9"); // Green
                        break;
                    case 4:
                        System.out.printf("%-3s", "\uD83D\uDFE8"); // Yellow
                        break;
                    case 5:
                        System.out.printf("%-3s", "\uD83D\uDFEA"); // Purple
                        break;
                    default:
                        System.out.printf("%-3s", "\uD83D\uDFE6"); // Blue (default)
                        break;
                }
            }
            if (i < legend.length) {
                System.out.print("|");
                System.out.printf("%-20s", legend[i]);
            }
            System.out.println();
        }
    }

    private static void drawAIShips(int gridLength, int[][] grid, int[] ships) {
        Random rand = new Random();

        for (int shipLength : ships) {
            boolean valid = false;
            int[][] allShipCoordinates = new int[shipLength][2];
            do {
                int shipDirectionAxis = rand.nextInt(0, 2); // 0 = horizontal, 1 = vertical
                int shipDirection = rand.nextInt(0, 2); // 0 = right or down, 1 = left or up
                int[] shipCoordinates = {rand.nextInt(gridLength), rand.nextInt(gridLength)};
                valid = checkIfValidShipPosition(gridLength, grid, shipLength, shipDirectionAxis, shipDirection, shipCoordinates, allShipCoordinates);
            } while (!valid);
            drawShip(grid, allShipCoordinates, shipLength);
        }
    }

    private static void drawShip(int[][] grid, int[][] allShipCoordinates, int shipLength) {
        for (int i = 0; i < allShipCoordinates.length; i++) {
            grid[allShipCoordinates[i][0]][allShipCoordinates[i][1]] = shipLength;
        }
    }

    private static boolean checkIfValidShipPosition(int gridLength, int[][] grid, int shipLength, int shipDirectionAxis, int shipDirection, int[] shipCoordinates, int[][] allShipCoordinates) {
        boolean valid = true;

        allShipCoordinates = getAllShipCoordinates(shipLength, shipDirectionAxis, shipDirection, shipCoordinates, allShipCoordinates);
        for (int i = 0; i < allShipCoordinates.length; i++) {
            if (allShipCoordinates[i][0] >= gridLength || allShipCoordinates[i][1] >= gridLength || allShipCoordinates[i][0] < 0 || allShipCoordinates[i][1] < 0) {
                valid = false;
            } else if (grid[allShipCoordinates[i][0]][allShipCoordinates[i][1]] >= 1) {
                valid = false;
            }
        }

        return valid;
    }

    private static int[][] getAllShipCoordinates(int shipLength, int shipDirectionAxis, int shipDirection, int[] shipCoordinates, int[][] allShipCoordinates) {
        if (shipDirection == 0) {
            for (int i = 0; i < shipLength; i++) {
                if (shipDirectionAxis == 0) {
                    allShipCoordinates[i][0] = shipCoordinates[0];
                    allShipCoordinates[i][1] = shipCoordinates[1] + i;
                } else {
                    allShipCoordinates[i][0] = shipCoordinates[0] + i;
                    allShipCoordinates[i][1] = shipCoordinates[1];
                }
            }
        } else {
            for (int i = 0; i < shipLength; i++) {
                if (shipDirectionAxis == 0) {
                    allShipCoordinates[i][0] = shipCoordinates[0];
                    allShipCoordinates[i][1] = shipCoordinates[1] - i;
                } else {
                    allShipCoordinates[i][0] = shipCoordinates[0] - i;
                    allShipCoordinates[i][1] = shipCoordinates[1];
                }
            }
        }

        return allShipCoordinates;
    }
}