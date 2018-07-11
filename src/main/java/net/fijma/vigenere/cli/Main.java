package net.fijma.vigenere.cli;

import net.fijma.vigenere.Vigenere;

import java.util.Scanner;

public class Main {

    private static void usage() {
        System.err.println("usage: encrypt <key> | decrypt <key> | analyse");
        System.exit(1);
    }

    private static String input() {
        // read and clean full input: only letters, in uppercase
        StringBuilder sb = new StringBuilder();
        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            String line = input.nextLine();
            for (int i=0; i<line.length(); ++i) {
                char c = Character.toUpperCase(line.charAt(i));
                if (c >= 'A' && c<='Z') {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    private static String key(String raw) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<raw.length(); i++) {
            char c = Character.toUpperCase(raw.charAt(i));
            if (c<'A' || c>'Z') {
                usage();
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            usage();
        }

        switch (args[0]) {
            case "decrypt":
            case "encrypt":
                if (args.length < 2) {
                    usage();
                }
                System.out.println(Vigenere.cipher(input(), key(args[1]), "encrypt".equals(args[0])));
                break;
            case "analyse":
                Vigenere.subStrings(input());
                break;
            default:
                usage();
        }
    }
}
