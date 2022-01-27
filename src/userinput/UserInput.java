package userinput;

import java.util.Scanner;

public class UserInput {

    public String readInputString() {
        Scanner scanner = new Scanner(System.in);
        if (isInputString(scanner.nextLine())) {
            return scanner.nextLine().toLowerCase();
        } else {
            return readInputString();
        }
    }

    public  int readInputInt() {
        Scanner scanner = new Scanner(System.in);
        if (isInputInt(scanner)) {
            return scanner.nextInt();
        } else {
            System.out.println("fehler");
            return readInputInt();
        }
    }

    private  boolean isInputString(String s) {

        if (s == null || s.equals("")) {
            System.out.println("Fehler bei eingabe.");
            return false;
        }
        try {
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Fehler isInteger -> NumberFormatException");
        }
        return false;
    }

    private  boolean isInputInt(Scanner scanner) {
        return scanner.hasNextInt();

    }

}