package country.bot.top10countrysong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class Billboard10CountryTest {
    Billboard10Country billboard10Country;
    String url = "https://www.billboard.com/charts/country-songs";

    @Before
    public void setUp() {
        billboard10Country = new Billboard10Country(url);
    }

    @Test
    public void urlErrorTest() {
        new Billboard10Country("https://haha.id/");
    }

    @Test
    public void getBillboardUrl() {
        assertEquals(url, billboard10Country.getBillboardUrl());
    }

    @Test
    public void getTopTenCountryList() {
        List<Song> list = billboard10Country.getTopTenCountryList();
        assertEquals(10, list.size());
    }

    @Test
    public void printTopTenList() {
        String expectedOutput = billboard10Country.printTopTentList();

        System.out.println(expectedOutput);
        assertTrue(expectedOutput.contains("Bebe"));
    }
}
