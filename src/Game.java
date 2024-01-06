import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Game {
    static String[] polscyAktorzyIAktorki =
            {"Andrzej Grabowski", "Janusz Gajos", "Anna Przybylska", "Franciszek Pieczka",
                    "Pola Negri", "Borys Szyc", "Joanna Kulig", "Jan Englert",
                    "Joanna Koroniewska", "Anita Sokołowska", "Katarzyna Figura", "Anna Dereszowska",
                    "Małgorzata Kożuchowska", "Adrianna Biedrzyńska", "Barbara Brylska", "Wacław Kowalski",
                    "Artur Żmijewski", "Adam Fidusiewicz", "Jerzy Cnota", "Mieczysław Hryniewicz"};
    static String[] geografiaSwiata =
            {"Mauna Kea", "Morze Żółte", "Morze Arafura", "Ramla Bay",
                    "Sognefjorden", "Nanga Parbat", "Orinoko", "Zambezi",
                    "Rzeka Świętego Wawrzyńca", "Rio de Janeiro", "Belo Horizonte", "Kuala Lumpur",
                    "Ponta Delgada", "Dolina Śmierci", "Góry Brooksa", "Cieśnina Maltańska",
                    "Góry Kaledońskie", "Zatoka Botnicka", "Wielka Nizina Węgierska", "Góry Kantabryjskie"};
    static String[] jedzenie =
            {"Śledź pod pierzynką", "Sałatka jarzynowa", "Gulasz segedyński", "Ratatouille",
                    "Grzyby marynowane", "Czeburek", "Pierogi ruskie", "Zupa z dyni",
                    "Ciasto drożdżowe", "Omlet biszkoptowy", "Sznycel wiedeński", "Mizeria",
                    "Placek zbójnicki", "Placki ziemniaczane", "Jajka po benedyktyńsku", "Potrawka z królika",
                    "Kiszka ziemniaczana", "Zupa mleczna", "Wodzianka", "Barszcz ukraiński"};
    static String[] zwierzeta =
            {"Zając szarak", "Mysz domowa", "Morświn", "Norka amerykańska",
                    "Jeleń szlachetny", "Tchórz stepowy", "Mewa kanadyjska", "Koliberek czarnobrody",
                    "Sikora dwubarwna", "Wrona meksykańska", "Goryl górski", "Hipopotam nilowy",
                    "Słoń afrykański", "Wydra europejska", "Zebra sawannowa", "Dingo australijski ",
                    "Wombat tasmański", "Kangur rdzawoszyi", "Dziobak australijski", "Mrówkożer workowaty"};
    static String[] rosliny =
            {"Buk pospolity", "Daglezja zielona", "Klon jawor", "Lipa szerokolistna",
                    "Modrzew europejski", "Robinia akacjowa", "Sosna wejmutka", "Topola osika",
                    "Wiąz szypułkowy", "Wierzba biała", "Dziewanna fioletowa", "Fiołek ogrodowy",
                    "Goździk majowy", "Malwa różowa", "Niezapominajka", "Szałwia błyszcząca",
                    "Begonia królewska", "Filodendron", "Różanecznik indyjski", "Lubczyk ogrodowy"};
    static String[][] categories = {polscyAktorzyIAktorki, geografiaSwiata, jedzenie, zwierzeta, rosliny};
    static String[] passwordsFromFile = new String[20];
    static char[] drawnPassword;
    static char[] tempPassword;
    static int numberOfUnsuccessfulAttempts = 0;

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        /*readPasswordsFromFile();
        System.out.println(Arrays.toString(passwordsFromFile));*/
        prepareGame();
        playGame();
    }

    static String[] readPasswordsFromFile() {
        try {
            Scanner sc = new Scanner(new File("aktorzy.csv"));
            int arrayIndex = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String password = line.substring(0, line.length() - 1);
                passwordsFromFile[arrayIndex] = password;
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
        String textBlock = """
                Wybierz kategorię:
                                
                1. Polscy Aktorzy i Aktorki
                2. Geografia Świata
                3. Jedzenie
                4. Zwierzęta
                5. Rośliny
                                
                Wpisz liczbę od 1 do 5.
                """;
        System.out.println(textBlock);
    }

    static void preparePassword() {
        String[] selectedCategory = selectCategory();
        scanner.nextLine();
        String password = drawPassword(selectedCategory);
        System.out.println(password);
        drawnPassword = createCharArrayFromPassword(password);
        prepareTempPassword();
    }

    static String[] selectCategory() {
        int category = scanner.nextInt();
        if (category < 0 || category > categories.length) {
            System.out.println("Podana kategoria wykracza poza zakres 1 - " + categories.length + "\n" +
                    "Podaj kategorię:");
            return selectCategory();
        }
        return categories[category - 1];
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