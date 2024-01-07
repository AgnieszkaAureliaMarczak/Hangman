import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Game {
    static String[] categories = {"Polscy Aktorzy i Aktorki", "Geografia Świata", "Jedzenie",
            "Zwierzęta", "Rośliny"};
    static String[] selectedCategoryFromFile = new String[20];
    static char[] drawnPassword;
    static char[] tempPassword;
    static int numberOfUnsuccessfulAttempts = 0;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        prepareGame();
        playGame();
    }

    static String[] readCategoryPasswordsFromFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName + ".csv"));
            int arrayIndex = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String password = line.substring(0, line.length() - 1);
                selectedCategoryFromFile[arrayIndex] = password;
                arrayIndex++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku");
            e.printStackTrace();
        }
        return null;
    }

    static void prepareGame() {
        HangmanDrawing.prepareHangmanArray();
        printCommunicates();
        preparePassword();
    }

    static void printCommunicates() {
        printGreeting();
        printCategorySelection();
    }

    static void printGreeting() {
        System.out.println("Witaj w grze w wisielca!");
    }

    static void printCategorySelection() {
        System.out.println("Wybierz kategorię:");
        for (int i = 0; i < categories.length; i++) {
            System.out.println(i + 1 + ". " + categories[i]);
        }
        System.out.println("Wpisz liczbę od 1 do 5.");
    }

    static void preparePassword() {
        String selectedCategory = selectCategory();
        scanner.nextLine();
        readCategoryPasswordsFromFile(selectedCategory);
        String password = drawPassword(selectedCategoryFromFile);
     //   System.out.println(password);
        drawnPassword = createCharArrayFromPassword(password);
        prepareTempPassword();
    }

    static String selectCategory() {
        int category = scanner.nextInt();
        if (category < 0 || category > categories.length) {
            System.out.println("Podana kategoria wykracza poza zakres 1 - " + categories.length + "\n" +
                    "Podaj kategorię:");
            return selectCategory();
        }
        return switch(category){
            case 1 -> "polscy_aktorzy";
            case 2 -> "geografia_swiata";
            case 3 -> "jedzenie";
            case 4 -> "zwierzeta";
            case 5 -> "rosliny";
            default -> selectCategory();
        };
    }

    static String drawPassword(String[] category) {
        Random drawing = new Random();
        int passwordIndex = drawing.nextInt(20);
        return category[passwordIndex];
    }

    static char[] createCharArrayFromPassword(String password) {
        return password.toLowerCase().toCharArray();
    }

    static void prepareTempPassword() {
        tempPassword = new char[drawnPassword.length];
        encodeTempPassword();
    }

    static void encodeTempPassword() {
        for (int i = 0; i < drawnPassword.length; i++) {
            tempPassword[i] = switch (drawnPassword[i]) {
                case ' ' -> ' ';
                case '-' -> '-';
                default -> '_';
            };
        }
    }

    static void playGame() {
        displayEncodedPassword();
        boolean isPasswordGuessed;
        do {
            char letter = readPlayersLetter();
            isPasswordGuessed = respondToGivenLetter(letter);
        } while (numberOfUnsuccessfulAttempts < 9 && !isPasswordGuessed);
        endGameWhenPasswordNotGuessed();
    }

    static void displayEncodedPassword() {
        for (char letter : tempPassword) {
            System.out.print(Character.toUpperCase(letter));
        }
        System.out.println();
        System.out.println();
    }

    static char readPlayersLetter() {
        System.out.println("Podaj literę:");
        String givenLetter = scanner.nextLine();
        return givenLetter.charAt(0);
    }

    static boolean respondToGivenLetter(char letter) {
        if (checkIfPasswordsContainsLetter(letter)) {
            return reactWhenLetterGuessed(letter);
        } else {
            reactIfLetterNotGuessed();
            return false;
        }
    }

    static boolean checkIfPasswordsContainsLetter(char givenLetter) {
        for (int i = 0; i < drawnPassword.length; i++) {
            if (drawnPassword[i] == givenLetter) {
                return true;
            }
        }
        return false;
    }

    static boolean reactWhenLetterGuessed(char letter) {
        showGuessedLetterInPassword(letter);
        System.out.println();
        displayEncodedPassword();
        return guessPassword();
    }

    static void showGuessedLetterInPassword(char guessedLetter) {
        for (int i = 0; i < drawnPassword.length; i++) {
            if (drawnPassword[i] == guessedLetter) {
                tempPassword[i] = guessedLetter;
            }
        }
    }

    static boolean guessPassword() {
        System.out.println("Zgadnij hasło lub wciśnij \"Enter\"");
        String passwordAttempt = scanner.nextLine();
        if (passwordAttempt.equals(new String(drawnPassword))) {
            System.out.println("Brawo, hasło odgadnięte!");
            printPassword();
            return true;
        }
        System.out.println("Graj dalej.");
        return false;
    }

    static void reactIfLetterNotGuessed() {
        numberOfUnsuccessfulAttempts++;
        HangmanDrawing.drawHangman();
        HangmanDrawing.printHangmanArray();
    }

    static void printPassword() {
        for (char letter : drawnPassword) {
            System.out.print(Character.toUpperCase(letter));
        }
        System.out.println();
        System.out.println();
    }

    static void endGameWhenPasswordNotGuessed() {
        System.out.println("Koniec gry.");
    }
}