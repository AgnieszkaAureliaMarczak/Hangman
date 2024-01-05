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
    static String[][] kategorie = {polscyAktorzyIAktorki,geografiaSwiata,jedzenie,zwierzeta,rosliny};
    static String [] haslaZpliku = new String[20];
    static char[] wylosowaneHaslo;
    static char[] hasloDoOperacji;
    static int iloscNietrafionychProb = 0;
    static char[][] tablicaWisielca = new char[16][40];
    static String podaneLitery = "";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        czytajHasla();
        System.out.println(Arrays.toString(haslaZpliku));
        System.exit(0);
        przygotujGre();
        zagraj();
        System.out.println("Koniec gry.");
    }

    static String[] czytajHasla() {
        try {
            Scanner skanowanie = new Scanner(new File("aktorzy.csv"));
            int pozycjaWtablicy = 0;
            while (skanowanie.hasNextLine()) {
                String line = skanowanie.nextLine();
                String haslo = line.substring(0,line.length()-1);
                haslaZpliku[pozycjaWtablicy] = haslo;
                pozycjaWtablicy++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku");
        }
        return null;
    }

    static void przygotujGre() {
        przygotujTabliceWisielca();
        wyswietlPowitanie();
        przygotujHaslo();
    }

    static void przygotujTabliceWisielca() {
        Arrays.fill(tablicaWisielca[0], '—');
        for (int wiersz = 1; wiersz < tablicaWisielca.length - 1; wiersz++) {
            tablicaWisielca[wiersz][0] = '|';
            for (int kolumna = 1; kolumna < tablicaWisielca[wiersz].length - 1; kolumna++) {
                tablicaWisielca[wiersz][kolumna] = ' ';
            }
            tablicaWisielca[wiersz][tablicaWisielca[wiersz].length - 1] = '|';
        }
        Arrays.fill(tablicaWisielca[tablicaWisielca.length - 1], '—');
    }

    static void wyswietlPowitanie() {
        System.out.println("Witaj w grze w wisielca!\nWybierz kategorię:\n" +
                "1. Polscy Aktorzy i Aktorki\n" +
                "2. Geografia Świata\n" +
                "3. Jedzenie\n" +
                "4. Zwierzęta\n" +
                "5. Rośliny\n" +
                "Wpisz liczbę od 1 do 5.");
    }

    static void przygotujHaslo() {
        String[] wybranaKategoria = wybierzKategorie();
        scanner.nextLine();
        String haslo = losujHaslo(wybranaKategoria);
        System.out.println(haslo);
        wylosowaneHaslo = haslo.toLowerCase().toCharArray();
        hasloDoOperacji = new char[wylosowaneHaslo.length];
        wypelnijRoboczeHasloPodkreslnikami();
    }

    static String[] wybierzKategorie() {
        int kategoria = scanner.nextInt();
        if (kategoria < 0 || kategoria >= kategorie.length) {
            System.out.println("Podaj max " + kategorie.length);
            return wybierzKategorie();
        }
        return kategorie[kategoria - 1];
       /* return switch (kategoria) {
            case 1 -> polscyAktorzyIAktorki;
            case 2 -> geografiaSwiata;
            case 3 -> jedzenie;
            case 4 -> zwierzeta;
            case 5 -> rosliny;
            default -> new String[0];
        };*/
    }

    static String losujHaslo(String[] kategoria) {
        Random losowanie = new Random();
        int pozycjaHasla = losowanie.nextInt(20);
        return kategoria[pozycjaHasla];
    }

    static void wypelnijRoboczeHasloPodkreslnikami() {
        for (int i = 0; i < wylosowaneHaslo.length; i++) {
           /* hasloDoOperacji[i] = wylosowaneHaslo[i];
            if (wylosowaneHaslo[i] != ' ' && wylosowaneHaslo[i] != '-') {
                hasloDoOperacji[i] = '_';
            }*/
            switch (wylosowaneHaslo[i]) {
                case ' ' -> hasloDoOperacji[i] = ' ';
                case '-' -> hasloDoOperacji[i] = '-';
                default -> hasloDoOperacji[i] = '_';
            }
        }
    }

    static String zakodujHaslo() {
        String zakodowaneHaslo = "";
        for (char symbol : wylosowaneHaslo) {
            if (symbol == ' ' || symbol == '-' || podaneLitery.contains(symbol + "")) {
                zakodowaneHaslo += symbol;
            } else {
                zakodowaneHaslo += "_";
            }
        }
        return zakodowaneHaslo;
    }

    static void zagraj() {
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
        } while ((iloscNietrafionychProb < 9) && (!czyHasloOdgadniete()) && (!uzytkownikZgadlHaslo));
    }

    static void wyswietlHasloDoZgadywania() {
        for (char litera : hasloDoOperacji) {
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
        for (int i = 0; i < wylosowaneHaslo.length; i++) {
            if (wylosowaneHaslo[i] == podanaLitera) {
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
        for (int i = 0; i < wylosowaneHaslo.length; i++) {
            if (wylosowaneHaslo[i] == podanaLitera) {
                hasloDoOperacji[i] = podanaLitera;
            }
        }
    }

    static boolean zgadnijHaslo() {
        System.out.println("Zgadnij hasło lub wciśnij \"Enter\"");
        String probaHasla = scanner.nextLine();
        if (probaHasla.equals(new String(wylosowaneHaslo))) {
            System.out.println("Koniec gry! Brawo, hasło odgadnięte.");
            wyswietlHaslo();
            return true;
        }
        return false;
    }

    static void zareagujNaNietrafienieUzytkownika() {
        iloscNietrafionychProb++;
        narysujWisielca();
        wyswietlTabliceWisielca();
    }

    static boolean czyHasloOdgadniete() {
        for (int i = 0; i < hasloDoOperacji.length; i++) {
            if (hasloDoOperacji[i] == '_') {
                return false;
            }
        }
        return true;
    }

    static void wyswietlHaslo() {
        for (char litera : wylosowaneHaslo) {
            System.out.print(Character.toUpperCase(litera));
        }
        System.out.println();
        System.out.println();
    }

    static void narysujWisielca() {
        switch (iloscNietrafionychProb) {
            case 1 -> {
                for (int wiersz = 2; wiersz < tablicaWisielca.length - 1; wiersz++) {
                    tablicaWisielca[wiersz][30] = '|';
                }
            }
            case 2 -> {
                Arrays.fill(tablicaWisielca[2], 16, 29, '_');
                for (int wiersz = 3; wiersz < 6; wiersz++) {
                    tablicaWisielca[wiersz][16] = '|';
                }
            }
            case 3 -> {
                for (int kolumna = 14; kolumna < 19; kolumna++) {
                    tablicaWisielca[6][kolumna] = '-';
                }
                tablicaWisielca[7][14] = '|';
                tablicaWisielca[7][18] = '|';
                for (int kolumna = 14; kolumna < 19; kolumna++) {
                    tablicaWisielca[8][kolumna] = '-';
                }
            }
            case 4 -> {
                for (int wiersz = 9; wiersz < 12; wiersz++) {
                    tablicaWisielca[wiersz][16] = '|';
                }
            }
            case 5 -> {
                tablicaWisielca[12][15] = '╱';
                tablicaWisielca[12][17] = '╲';
            }
            case 6 -> {
                tablicaWisielca[13][14] = '╱';
                tablicaWisielca[13][18] = '╲';
            }
            case 7 -> tablicaWisielca[10][15] = '╲';
            case 8 -> tablicaWisielca[10][17] = '╱';
            case 9 -> {
                tablicaWisielca[10][15] = '╱';
                tablicaWisielca[10][17] = '╲';
            }
            default -> System.out.println();
        }
    }

    static void wyswietlTabliceWisielca() {
        for (int wiersz = 0; wiersz < tablicaWisielca.length; wiersz++) {
            for (int kolumna = 0; kolumna < tablicaWisielca[wiersz].length; kolumna++) {
                System.out.print(tablicaWisielca[wiersz][kolumna]);
            }
            System.out.println();
        }
    }
}