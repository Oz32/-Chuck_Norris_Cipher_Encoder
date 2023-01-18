package chucknorris;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static String[] availableCommands = new String[]{"encode", "decode", "exit"};
    static String welcoming = "Please input operation (encode/decode/exit):";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(welcoming);
            String command = scanner.nextLine();
            String scanned = "";
            if (command.equalsIgnoreCase(availableCommands[0])) {
                System.out.println("Input string:");
                scanned = scanner.nextLine();
                System.out.println("Encoded string:");
                System.out.println(chuckNorrisUnaryCodeEncoder(scanned) + "\n");
            } else if (command.equalsIgnoreCase(availableCommands[1])) {
                System.out.println("Input encoded string:");
                scanned = scanner.nextLine();
                if (Errors(scanned) || chuckNorrisUnaryCodeDecoder(scanned).toString().equals("error")) {
                    System.out.println("Encoded string is not valid." + "\n");
                    continue;
                }
                System.out.println("Decoded string:");
                System.out.println(chuckNorrisUnaryCodeDecoder(scanned) + "\n");
            } else if (command.equalsIgnoreCase(availableCommands[2])) {
                System.out.println("Bye!");
                break;
            } else {
                System.out.println("There is no " + "'" + command + "'" + " operation" + "\n");
            }
        }
    }

    public static StringBuilder convertInputToBinary(String scanned) {
        StringBuilder inputBinary = new StringBuilder();
        for (int i = 0; i < scanned.length(); i++) {
            int a = scanned.charAt(i);
            String b = String.format("%7s", Integer.toBinaryString(a)).replace(" ", "0");
            inputBinary.append(b);
        }
        return inputBinary;
    }

    public static StringBuilder chuckNorrisUnaryCodeEncoder(String scanned) {
        String binary = convertInputToBinary(scanned).toString();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < binary.length(); i++) {
            int a = Character.getNumericValue(binary.charAt(i));
            result.append(a == 1 ? " 0 0" : " 00 0");
            while (i != binary.length() - 1) {
                if (Character.getNumericValue(binary.charAt(i + 1)) == Character.getNumericValue(binary.charAt(i))) {
                    result.append("0");
                    i++;
                } else break;
            }
        }
        result.deleteCharAt(0);
        return result;
    }

    public static StringBuilder chuckNorrisUnaryCodeDecoder(String scanned) {
        String[] symbols = scanned.split(" ");
        StringBuilder binaryString = new StringBuilder();

        for (int i = 0; i < symbols.length; i++) {
            if (i % 2 == 0 || i == 0) {
                if (symbols[i].equals("0")) {
                    for (int j = 0; j < symbols[i + 1].length(); j++) {
                        binaryString.append("1");
                    }
                } else if (symbols[i].equals("00")) {
                    for (int j = 0; j < symbols[i + 1].length(); j++) {
                        binaryString.append("0");
                    }
                }
            }
        }
        return (convertBinaryToResult(binaryString));
    }

    public static StringBuilder convertBinaryToResult(StringBuilder binaryString) {
        StringBuilder result = new StringBuilder();
        if (binaryString.length() % 7 != 0) {
            return result.append("error");
        }
        String[] binaryValues = new String[binaryString.length() / 7];
        int startIndex = 0, endIndex = 7;
        for (int i = 0; i < binaryValues.length; i++) {
            binaryValues[i] = binaryString.substring(startIndex, endIndex);
            if (i < binaryString.length() - 1) {
                startIndex += 7;
                endIndex += 7;
            }
        }
        int[] binaryIntegerValues = new int[binaryValues.length];
        for (int i = 0; i < binaryIntegerValues.length; i++) {
            binaryIntegerValues[i] = Integer.parseInt(binaryValues[i]);
        }
        int[] decimalValues = new int[binaryIntegerValues.length];
        for (int i = 0; i < decimalValues.length; i++) {
            decimalValues[i] = binaryToDecimal(binaryIntegerValues[i]);
        }
        char[] actualChars = new char[decimalValues.length];
        for (int i = 0; i < actualChars.length; i++) {
            actualChars[i] = (char) decimalValues[i];
            result.append(actualChars[i]);
        }
        return result;
    }

    public static int binaryToDecimal(int input) {
        int number = input;
        int result = 0;
        int base = 1;
        int temp = number;

        while (temp > 0) {
            int lastDigit = temp % 10;
            temp = temp / 10;
            result += lastDigit * base;
            base = base * 2;
        }
        return result;
    }

    public static boolean Errors(String scanned) {
        String[] symbols = scanned.split(" ");
        boolean error = false;
        for (int i = 0; i < scanned.length(); i++) {
            if (scanned.charAt(i) != '0' && scanned.charAt(i) != ' ') {
                error = true;
                break;
            }
        }
        if (!symbols[0].equals("0") && !symbols[0].equals("00")) {
            error = true;
        }
        if (symbols.length % 2 != 0) {
            error = true;
        }
        return error;
    }
}