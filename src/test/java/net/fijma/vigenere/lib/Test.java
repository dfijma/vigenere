package net.fijma.vigenere.lib;

import org.junit.Assert;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test {

    @org.junit.Test
    public void testTable() {

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

        Assert.assertEquals("DITISGEHEIM", Vigenere.vigenere("DITISGEHEIM", "A", true));
        Assert.assertEquals("DITISGEHEIM", Vigenere.vigenere("DITISGEHEIM", "A", false));

        Assert.assertEquals("B", Vigenere.vigenere("A", "B", true));
        Assert.assertEquals("A", Vigenere.vigenere("B", "B", false));

        Assert.assertEquals("PUFUESQTQUY", Vigenere.vigenere("DITISGEHEIM", "M", true));
        Assert.assertEquals("DITISGEHEIM", Vigenere.vigenere("PUFUESQTQUY", "M", false));

        String plain = "WELKELETTERSWORDENINHETNEDERLANDSHETMEESTGEBRUIKTDEONDERSTAANDELIJSTISGEBASEERDOPONDERZOEKUITVANDESTICHTINGVOORPUBLIEKSVOORLICHTINGOVERWETENSCHAPENTECHNIEKPWTVOORHAARONDERZOEKNAARLETTERFREQUENTIESBASEERDEDESTICHTINGZICHOPEENCORPUSVANANDERHALFMILJOENWOORDENREDACTIONELEKRANTENTEKSTAFKOMSTIGUITDEHAARLEMSECOURANTGETELDEINDOVEREENPERIODEVANONGEVEERVIERMAANDENDEGEGEVENSZIJNTEVINDENINTERSPRAKESPRAAKALSBETEKENISVOLGELUIDINTHEMATISCHEHOOFDSTUKKENVANMARCELPRBROECKETOENMALIGMEDEWERKERVANDEPWT";
        String crypt = "JRYXRYRGGREFJBEQRAVAURGARQREYNAQFURGZRRFGTROEHVXGQRBAQREFGNNAQRYVWFGVFTRONFRREQBCBAQREMBRXHVGINAQRFGVPUGVATIBBECHOYVRXFIBBEYVPUGVATBIREJRGRAFPUNCRAGRPUAVRXCJGIBBEUNNEBAQREMBRXANNEYRGGRESERDHRAGVRFONFRREQRQRFGVPUGVATMVPUBCRRAPBECHFINANAQREUNYSZVYWBRAJBBEQRAERQNPGVBARYRXENAGRAGRXFGNSXBZFGVTHVGQRUNNEYRZFRPBHENAGTRGRYQRVAQBIRERRACREVBQRINABATRIRREIVREZNNAQRAQRTRTRIRAFMVWAGRIVAQRAVAGREFCENXRFCENNXNYFORGRXRAVFIBYTRYHVQVAGURZNGVFPURUBBSQFGHXXRAINAZNEPRYCEOEBRPXRGBRAZNYVTZRQRJREXREINAQRCJG";
        Assert.assertEquals('A', Vigenere.minimalDistance(plain));
        Assert.assertEquals('N', Vigenere.minimalDistance(crypt));

        String plain2 = "DUCOFIJMA";
        String key2 = "ABC";
        String shouldEncrypt2 = "DVEOGKJNC";
        String encrypted2 = Vigenere.vigenere(plain2, key2, true);
        Assert.assertEquals(shouldEncrypt2, encrypted2);
        String decrypted = Vigenere.vigenere(shouldEncrypt2, key2, false);
        Assert.assertEquals(plain2, decrypted);


        String stripe = "DUCOFIJMA";
        Assert.assertEquals("DOJ", Vigenere.stripe(stripe, 0, 3));
        Assert.assertEquals("UFM", Vigenere.stripe(stripe, 1, 3));
        Assert.assertEquals("CIA", Vigenere.stripe(stripe, 2, 3));
    }

}
