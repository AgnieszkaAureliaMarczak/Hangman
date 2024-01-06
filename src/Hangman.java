import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
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
    static char[][] hangmanArray = new char[16][40];
    static String podaneLitery = "";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        readPasswordsFromFile();
        System.out.println(Arrays.toString(passwordsFromFile));
        System.exit(0);
        prepareGame();
        play();
        System.out.println("Koniec gry.");
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
        prepareHangmanArray();
        printCommunicates();
        preparePassword();
    }

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
        if (category < 0 || category >= categories.length) {
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
        fillInTempPasswordWithUnderscores();
    }

    static void fillInTempPasswordWithUnderscores() {
        for (int i = 0; i < drawnPassword.length; i++) {
            tempPassword[i] = switch (drawnPassword[i]) {
                case ' ' -> ' ';
                case '-' -> '-';
                default -> '_';
            };
        }
    }

    static String encodePassword() {
        String zakodowaneHaslo = "";
        for (char symbol : drawnPassword) {
            if (symbol == ' ' || symbol == '-' || podaneLitery.contains(symbol + "")) {
                zakodowaneHaslo += symbol;
            } else {
                zakodowaneHaslo += "_";
            }
        }
        return zakodowaneHaslo;
    }

    static void play() {
        wyswietlHasloDoZgadywania();
        boolean uzytkownikZgadlHaslo = false;
        do {
            char litera = podajLitere();
            podaneLitery += litera;
            if (sprawdzCzyPoprawnaLitera(litera)) {
                uzytkownikZgadlHaslo = zareagujNaTrafienieUzytkownika(litera);
            } else {
                zareagujNaNietrafienieUzytkownika();
            }
        } while ((numberOfUnsuccessfulAttempts < 9) && (!czyHasloOdgadniete()) && (!uzytkownikZgadlHaslo));
    }

    static void wyswietlHasloDoZgadywania() {
        for (char litera : tempPassword) {
            System.out.print(Character.toUpperCase(litera));
        }
        System.out.println();
        System.out.println();
    }

    static char podajLitere() {
        System.out.println("Podaj literę:");
        String podanaLitera = scanner.nextLine();
        return podanaLitera.charAt(0);
    }

    static boolean sprawdzCzyPoprawnaLitera(char podanaLitera) {
        for (int i = 0; i < drawnPassword.length; i++) {
            if (drawnPassword[i] == podanaLitera) {
                return true;
            }
        }
        return false;
    }

    static boolean zareagujNaTrafienieUzytkownika(char litera) {
        wpiszPodanaLitere(litera);
        System.out.println();
        wyswietlHasloDoZgadywania();
        //System.out.println(zakodujHaslo());
        return zgadnijHaslo();
    }

    static void wpiszPodanaLitere(char podanaLitera) {
        for (int i = 0; i < drawnPassword.length; i++) {
            if (drawnPassword[i] == podanaLitera) {
                tempPassword[i] = podanaLitera;
            }
        }
    }

    static boolean zgadnijHaslo() {
        System.out.println("Zgadnij hasło lub wciśnij \"Enter\"");
        String probaHasla = scanner.nextLine();
        if (probaHasla.equals(new String(drawnPassword))) {
            System.out.println("Koniec gry! Brawo, hasło odgadnięte.");
            wyswietlHaslo();
            return true;
        }
        return false;
    }

    static void zareagujNaNietrafienieUzytkownika() {
        numberOfUnsuccessfulAttempts++;
        narysujWisielca();
        wyswietlTabliceWisielca();
    }

    static boolean czyHasloOdgadniete() {
        for (int i = 0; i < tempPassword.length; i++) {
            if (tempPassword[i] == '_') {
                return false;
            }
        }
        return true;
    }

    static void wyswietlHaslo() {
        for (char litera : drawnPassword) {
            System.out.print(Character.toUpperCase(litera));
        }
        System.out.println();
        System.out.println();
    }

    static void narysujWisielca() {
        switch (numberOfUnsuccessfulAttempts) {
            case 1 -> {
                for (int wiersz = 2; wiersz < hangmanArray.length - 1; wiersz++) {
                    hangmanArray[wiersz][30] = '|';
                }
            }
            case 2 -> {
                Arrays.fill(hangmanArray[2], 16, 29, '_');
                for (int wiersz = 3; wiersz < 6; wiersz++) {
                    hangmanArray[wiersz][16] = '|';
                }
            }
            case 3 -> {
                for (int kolumna = 14; kolumna < 19; kolumna++) {
                    hangmanArray[6][kolumna] = '-';
                }
                hangmanArray[7][14] = '|';
                hangmanArray[7][18] = '|';
                for (int kolumna = 14; kolumna < 19; kolumna++) {
                    hangmanArray[8][kolumna] = '-';
                }
            }
            case 4 -> {
                for (int wiersz = 9; wiersz < 12; wiersz++) {
                    hangmanArray[wiersz][16] = '|';
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

    static void wyswietlTabliceWisielca() {
        for (int wiersz = 0; wiersz < hangmanArray.length; wiersz++) {
            for (int kolumna = 0; kolumna < hangmanArray[wiersz].length; kolumna++) {
                System.out.print(hangmanArray[wiersz][kolumna]);
            }
            System.out.println();
        }
    }
}