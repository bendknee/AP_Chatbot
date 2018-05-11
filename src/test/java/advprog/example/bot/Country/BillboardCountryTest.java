package advprog.example.bot.Country;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import advprog.example.bot.country.BillboardCountry;
import advprog.example.bot.country.Song;
import org.junit.Before;
import org.junit.Test;

public class BillboardCountryTest {
    BillboardCountry billboardCountry;
    String url = "https://www.billboard.com/charts/country-songs";

    @Before
    public void setUp() {
        billboardCountry = new BillboardCountry(url);
    }

    @Test
    public void urlErrorTest() {
        new BillboardCountry("https://haha.id/");
    }

    @Test
    public void getBillboardUrl() {
        assertEquals(url, billboardCountry.getBillboardUrl());
    }

    @Test
    public void getTopTenCountryList() {
        List<Song> list = billboardCountry.getTopTenCountryList();
        assertEquals(10, list.size());
    }

    @Test
    public void printTopTenList() {
        String expectedOutput = billboardCountry.printTopTentList();

        System.out.println(expectedOutput);
        assertTrue(expectedOutput.contains("Bebe"));
    }
}
