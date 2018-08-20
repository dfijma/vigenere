package net.fijma.vigenere.cli;

import net.fijma.vigenere.Vigenere;
import org.apache.commons.cli.*;

import java.util.Scanner;

public class Main {

    private static void usage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("vigenere", options);
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

    private static String key(Options options, String raw) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<raw.length(); i++) {
            char c = Character.toUpperCase(raw.charAt(i));
            if (c<'A' || c>'Z') {
                usage(options);
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        Options options = new Options();
        options.addOption("d", false, "decrypt");
        options.addOption("e", false, "encrypt");
        options.addOption("a", false, "analyse");
        options.addOption(Option.builder("k").optionalArg(true).hasArg().argName("key").desc("key").build());

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            String key = null;
            if (cmd.hasOption("k")) {
                key = cmd.getOptionValue("k");
            }
            if (cmd.hasOption("d")) {
                if (key == null) usage(options);
                System.err.println("decrypting using key: " + key);
                System.out.println(Vigenere.cipher(input(), key(options, key), false));
            } else if (cmd.hasOption("e")) {
                if (key == null) usage(options);
                System.err.println("encrypting using key: " + key);
                System.out.println(Vigenere.cipher(input(), key(options, key), true));
            } else if (cmd.hasOption("a")) {
                System.err.println("analysing:");
                Vigenere.analyse(input());
            } else {
                usage(options);
            }

        } catch (ParseException e) {
            System.err.println(e.getMessage());
            usage(options);
        }
    }
}
