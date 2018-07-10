package net.fijma.vigenere.lib;

import java.util.List;

public class Util {

    // is ys a repeat of xs?
    static boolean isRepeatOf(String xs, String ys) {
        if (ys.length() < xs.length()) return false;
        if (ys.length() % xs.length() != 0) return false;
        StringBuilder sb = new StringBuilder();
        for (int i =0; i<ys.length() / xs.length(); i++) {
            sb.append(xs);
        }
        return sb.toString().equals(ys);
    }

    // is ys a repeat of any string in xss?
    static boolean isRepeatOfAny(List<String> xss, String ys) {
        for (String xs : xss) {
            if (isRepeatOf(xs, ys)) return true;
        }
        return false;
    }

    // Euclides GCD
    static int gcd(int a, int b) {
        if (a == 0) {
            return b;
        }

        while (b != 0) {
            if (a > b) {
                a = a - b;
            } else {
                b = b - a;
            }
        }

        return a;
    }

}
