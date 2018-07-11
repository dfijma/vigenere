package net.fijma.vigenere;


import java.net.URISyntaxException;
import java.util.*;

public class Vigenere {

    // the alphabet we use
    public static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // frequency table for Dutch
    // see: https://onzetaal.nl/taaladvies/letterfrequentie-in-het-nederlands
    public static Map<Character, Double> freqs;

    static {
        freqs = new HashMap<>();
        freqs.put('E', 18.91);
        freqs.put('N', 10.03);
        freqs.put('A', 7.49);
        freqs.put('T', 6.79);
        freqs.put('I', 6.50);
        freqs.put('R', 6.41);
        freqs.put('O', 6.06);
        freqs.put('D', 5.93);
        freqs.put('S', 3.73);
        freqs.put('L', 3.57);
        freqs.put('G', 3.40);
        freqs.put('V', 2.85);
        freqs.put('H', 2.38);
        freqs.put('K', 2.25);
        freqs.put('M', 2.21);
        freqs.put('U', 1.99);
        freqs.put('B', 1.58);
        freqs.put('P', 1.57);
        freqs.put('W', 1.52);
        freqs.put('J', 1.46);
        freqs.put('Z', 1.39);
        freqs.put('C', 1.24);
        freqs.put('F', 0.81);
        freqs.put('X', 0.040);
        freqs.put('Y', 0.035);
        freqs.put('Q', 0.009);
    }

    // calculate frequencies of letters in text
    static Map<Character, Double> calculateFrequencies(String text) {
        Map<Character, Double> res = new HashMap<>();
        for (int i=0; i<alphabet.length(); i++) {
            res.put(alphabet.charAt(i), 0.0);
        }
        // count them
        int count = 0;
        for (int i=0; i<text.length(); i++) {
            Character c = text.charAt(i);
            if (c<'A' || c>'Z') throw new RuntimeException("invalid char");
            count += 1;
            res.put(c, res.get(c) + 1.0);
        }

        // normalize to 100%
        for (Map.Entry<Character, Double> e : res.entrySet()) {
            res.put(e.getKey(), 100 * (e.getValue() / count));
        }

        return res;
    }

    // square difference of two frequency tables
    private static double squareDifference(Map<Character, Double> x, Map<Character, Double> y) {
        double res = 0.0;
        for (int i=0; i<alphabet.length(); i++) {
            char c = alphabet.charAt(i);
            res += Math.pow(x.get(c) - y.get(c), 2);
        }
        return res;
    }

    // implementation of cipher itself
    public static String cipher(String xs, String key, boolean encrypt) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<xs.length(); ++i) {
            sb.append(shift(xs.charAt(i), key.charAt(i % key.length()), encrypt));
        }
        return sb.toString();
    }

    private static int val(char c) {
        return (char)(c - 'A');
    }

    private static char shift(char c, char key, boolean encrypt) {
        int f = encrypt ? 1 : -1;
        int d = (val(c) + (f * val(key)) + alphabet.length()) % alphabet.length();
        return alphabet.charAt(d);
    }

    // what single letter key would lead to the least squareDifference with frequency table when used to decrypt 'rotEncrypted'?
    static char frequencyAnalysis(String rotEncrypted) {
        int res = Integer.MAX_VALUE;
        Double min = Double.MAX_VALUE;
        for (int i=0; i<alphabet.length(); ++i) {
            Double d = squareDifference(freqs, calculateFrequencies(cipher(rotEncrypted, String.valueOf(alphabet.charAt(i)), false)));
            if (d < min) {
                min = d;
                res = i;
            }
        }
        return alphabet.charAt(res);
    }

    // stripe encrypted text based on (assumed) key length
    static String stripe(String xs, int stripe, int keyLength) {
        StringBuilder sb = new StringBuilder();
        while (stripe < xs.length()) {
            sb.append(xs.charAt(stripe));
            stripe += keyLength;
        }
        return sb.toString();
    }

    // perform frequency analysis on all stripes of input 'xs' based on (assumed) key length
    private static String analyseStripes(String xs, int keyLength) {
        StringBuilder sb = new StringBuilder();
        for (int stripe = 0; stripe < keyLength; stripe++) {
            String ss = stripe(xs, stripe, keyLength);
            sb.append(frequencyAnalysis(ss));
        }
        return sb.toString();
    }

    public static void analyse(String xs) {
        boolean cont = true;

        Set<Integer> gcds = new HashSet<>();

        // while we found any repeating words of given length, continue
        for (int length = 10; cont ; ++length) {

            // all substrings of length 'length' with their positions
            HashMap<String, List<Integer>> substrings = new HashMap<>();
            for (int i = 0; i < xs.length() - length + 1; i++) {
                String ss = xs.substring(i, i + length);
                List<Integer> ps = substrings.computeIfAbsent(ss, k -> new ArrayList<>());
                ps.add(i);
            }

            // remove substrings occurring less than three times
            substrings.entrySet().removeIf(e -> e.getValue().size() < 3);

            cont = false; // reset to true if we find something, so that we will continue with length+1

            System.out.println("length: " + length);
            for (Map.Entry<String, List<Integer>> e : substrings.entrySet()) {
                cont = true; // continue with next length
                List<Integer> positions = e.getValue(); // at least 3 position => at least 2 distances

                int gcdDiff = positions.get(1) - positions.get(0);
                System.out.print(gcdDiff + " ");
                for (int l=2; l<e.getValue().size(); l++) {
                    gcdDiff = Util.gcd(gcdDiff, positions.get(l) - positions.get(l-1));
                    System.out.print((positions.get(l) - positions.get(l-1)) + " ");
                }
                System.out.println(" = ggd " + gcdDiff);
                gcds.add(gcdDiff);
            }
        }

        List<Integer> sorted = new ArrayList<>(gcds);
        Collections.sort(sorted);

        for (int i=0; i<sorted.size(); ++i) {
            System.out.println(sorted.get(i) + " ");
        }
        System.out.println();

        int gcd = sorted.get(0);
        for (int i = 1; i<sorted.size(); ++i) {
            gcd = Util.gcd(gcd, sorted.get(i));
        }

        System.out.println("general: " + gcd);

        List<String> keys = new ArrayList<>();
        for (Integer e : sorted) {
            String key = analyseStripes(xs, e);

            //if (!Util.isRepeatOfAny(keys, key)) {
                System.out.println(e + ":" + key);
                keys.add(key);
            //}//
        }
    }


}
