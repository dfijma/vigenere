package net.fijma.vigenere.lib;


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

    // frequency table if 'crypted' were decryped with 'k'
    private static Map<Character, Double> freqFromRot(String crypted, char k) {
        return calculateFrequencies(vigenere(crypted, String.valueOf(k), false));
    }

    // what key would lead to the least freq squareDifference when used to decrypt 'crypted'?
    static char minimalDistance(String crypted) {
        int res = Integer.MAX_VALUE;
        Double min = Double.MAX_VALUE;
        for (int i=0; i<alphabet.length(); ++i) {
            Double d = squareDifference(freqs, freqFromRot(crypted, alphabet.charAt(i)));
            if (d < min) {
                min = d;
                res = i;
            }
        }
        return alphabet.charAt(res);
    }

    private static int val(char c) {
        return (char)(c - 'A');
    }

    private static char shift(char c, char key, boolean encrypt) {
        int f = encrypt ? 1 : -1;
        int d = (val(c) + (f * val(key)) + alphabet.length()) % alphabet.length();
        return alphabet.charAt(d);
    }

    static String stripe(String xs, int i, int n) {
        StringBuilder sb = new StringBuilder();
        int p = i;
        while (p<xs.length()) {
            sb.append(xs.charAt(p));
            p += n;
        }
        return sb.toString();
    }

    public static String vigenere(String xs, String key, boolean encrypt) {
        StringBuilder sb = new StringBuilder();
        xs = xs.toUpperCase();
        for (int i=0; i<xs.length(); ++i) {
            sb.append(shift(xs.charAt(i), key.charAt(i % key.length()), encrypt));
        }
        return sb.toString();
    }

    private static String analyseStripes(String xs, int stripes) {
        StringBuilder sb = new StringBuilder();
        for (int stripe = 0; stripe < stripes; stripe++) {
            String ss = stripe(xs, stripe, stripes);
            sb.append(minimalDistance(ss));
        }
        return sb.toString();
    }

    public static void subStrings(String xs) {
        boolean keep = true;

        Set<Integer> gcds = new HashSet<>();

        for (int length = 4; keep ; ++length) {
            HashMap<String, List<Integer>> occurrences = new HashMap<>();

            // all substring of length 'length'
            for (int i = 0; i < xs.length() - length + 1; i++) {
                String ss = xs.substring(i, i + length);
                List<Integer> ps = occurrences.get(ss);
                if (ps == null) {
                    ps = new ArrayList<>();
                    occurrences.put(ss, ps);
                }
                ps.add(i);
            }

            keep = false;
            // all occurring more than twice:
            for (Map.Entry<String, List<Integer>> e : occurrences.entrySet()) {
                if (e.getValue().size() > 2) {
                    keep = true; // continue with next length
                    int gggg = Util.gcd(e.getValue().get(0), e.getValue().get(1));
                    for (int l=2; l<e.getValue().size(); l++) {
                        gggg = Util.gcd(gggg, e.getValue().get(l));
                    }
                    if (gggg > 1) {
                        gcds.add(gggg);
                    }
                }
            }

        }

        List<Integer> sorted = new ArrayList<>(gcds);
        List<String> keys = new ArrayList<>();
        Collections.sort(sorted);
        for (Integer e : sorted) {
            String key = analyseStripes(xs, e);

            if (!Util.isRepeatOfAny(keys, key)) {
                System.out.println(e + ":" + key);
                keys.add(key);
            }
        }
    }


}
