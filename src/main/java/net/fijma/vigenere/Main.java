package net.fijma.vigenere;

import net.fijma.vigenere.lib.Vigenere;

import java.util.Scanner;

public class Main {

    static void fatal(String xs) {
        System.err.println(xs);
        System.exit(1);
    }

    public static void main(String[] args) {

        StringBuffer sb = new StringBuffer();
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

        if (args.length < 1) {
            fatal("subcommand: encrypt, decrypt, analyse");
        }

        switch (args[0]) {
            case "decrypt":
            case "encrypt":
                if (args.length < 2) {
                    fatal("usage: " + args[0]+ " <key>");
                }
                String crypted = Vigenere.vigenere(sb.toString(), args[1], "encrypt".equals(args[0]));
                System.out.println(crypted);
                break;
            case "analyse":
                Vigenere.subStrings(sb.toString());
                break;
            default:
                fatal("subcommand: encrypt, decrypt, frequency or analyse");
        }
    }
}
