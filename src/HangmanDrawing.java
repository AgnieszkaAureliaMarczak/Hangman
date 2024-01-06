import java.util.Arrays;

public class HangmanDrawing {
    static char[][] hangmanArray = new char[16][40];
    static void prepareHangmanArray() {
        Arrays.fill(hangmanArray[0], '—');
        for (int row = 1; row < hangmanArray.length - 1; row++) {
            hangmanArray[row][0] = '|';
            for (int column = 1; column < hangmanArray[row].length - 1; column++) {
                hangmanArray[row][column] = ' ';
            }
            hangmanArray[row][hangmanArray[row].length - 1] = '|';
        }
        Arrays.fill(hangmanArray[hangmanArray.length - 1], '—');
    }

    static void drawHangman() {
        switch (Game.numberOfUnsuccessfulAttempts) {
            case 1 -> {
                for (int row = 2; row < hangmanArray.length - 1; row++) {
                    hangmanArray[row][30] = '|';
                }
            }
            case 2 -> {
                Arrays.fill(hangmanArray[2], 16, 29, '_');
                for (int row = 3; row < 6; row++) {
                    hangmanArray[row][16] = '|';
                }
            }
            case 3 -> {
                for (int column = 14; column < 19; column++) {
                    hangmanArray[6][column] = '-';
                }
                hangmanArray[7][14] = '|';
                hangmanArray[7][18] = '|';
                for (int column = 14; column < 19; column++) {
                    hangmanArray[8][column] = '-';
                }
            }
            case 4 -> {
                for (int row = 9; row < 12; row++) {
                    hangmanArray[row][16] = '|';
                }
            }
            case 5 -> {
                hangmanArray[12][15] = '╱';
                hangmanArray[12][17] = '╲';
            }
            case 6 -> {
                hangmanArray[13][14] = '╱';
                hangmanArray[13][18] = '╲';
            }
            case 7 -> hangmanArray[10][15] = '╲';
            case 8 -> hangmanArray[10][17] = '╱';
            case 9 -> {
                hangmanArray[10][15] = '╱';
                hangmanArray[10][17] = '╲';
            }
            default -> System.out.println();
        }
    }

    static void printHangmanArray() {
        for (int row = 0; row < hangmanArray.length; row++) {
            for (int column = 0; column < hangmanArray[row].length; column++) {
                System.out.print(hangmanArray[row][column]);
            }
            System.out.println();
        }
    }
}
