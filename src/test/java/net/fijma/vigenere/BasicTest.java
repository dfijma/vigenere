package net.fijma.vigenere;

import org.junit.Assert;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BasicTest {

    @org.junit.Test
    public void testTables() {
        // test setup of alphabet and  frequency tables
        Set<Character> letters = new HashSet<>();

        for (int i = 0; i < Vigenere.alphabet.length(); i++) {
            letters.add(Vigenere.alphabet.charAt(i));
        }

        Assert.assertEquals("26 letters expected", 26, letters.size());

        double total = 0.0;

        for (Map.Entry<Character, Double> e : Vigenere.freqs.entrySet()) {
            boolean wasPresent = letters.remove(e.getKey());
            Assert.assertTrue("letter duplicated", wasPresent);
            total += e.getValue();
        }

        Assert.assertTrue(Math.abs(total - 100.0) < 1.0);

        String example = "DITDITQQQQ";
        total = 0.0;
        Map<Character, Double> res = Vigenere.calculateFrequencies(example);
        for (Map.Entry<Character, Double> e : res.entrySet()) {
            total += e.getValue();
        }
        Assert.assertTrue(Math.abs(total - 100.0) < 1.0);
    }

    @org.junit.Test
    public void testCipher() {
        // test basic implementation of cipher
        Assert.assertEquals("DITISGEHEIM", Vigenere.cipher("DITISGEHEIM", "A", true));
        Assert.assertEquals("DITISGEHEIM", Vigenere.cipher("DITISGEHEIM", "A", false));

        Assert.assertEquals("B", Vigenere.cipher("A", "B", true));
        Assert.assertEquals("A", Vigenere.cipher("B", "B", false));

        String plain = "DUCOFIJMA";
        String key = "ABC";
        String shouldEncryptTo = "DVEOGKJNC";
        String actualEncrypted = Vigenere.cipher(plain, key, true);
        Assert.assertEquals(shouldEncryptTo, actualEncrypted);
        String decrypted = Vigenere.cipher(shouldEncryptTo, key, false);
        Assert.assertEquals(plain, decrypted);
    }

}
