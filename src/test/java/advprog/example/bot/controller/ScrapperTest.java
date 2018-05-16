package advprog.example.bot.controller;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
class ScrapperTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Test
    void testScrapping() {
        String weekly = "(1) スター・ウォーズ/最後のジェダイ MovieNEX(初回版) - マーク・ハミル - 2018-04-25\\n(2) ヴァイオレット・エヴァーガーデン�A - アニメーション - 2018-05-02\\n(3) オリエント急行殺人事件 2枚組ブルーレイ&DVD - ケネス・ブラナー - 2018-05-02\\n(4) 斉木楠雄のΨ難 豪華版ブルーレイ&DVDセット【初回生産限定】 - 山崎賢人 - 2018-05-02\\n(5) ラブライブ!サンシャイン!! 2nd Season 5【特装限定版】 - アニメーション - 2018-04-24\\n(6) ミックス。 豪華版Blu-ray - 新垣結衣,瑛太 - 2018-05-02\\n(7) GREEN MIND AT BUDOKAN - 秦 基博 - 2018-05-02\\n(8) THE IDOLM@STER SideM GREETING TOUR 2017 〜BEYOND THE DREAM〜 LIVE Blu-ray - アイドルマスターSideM - 2018-04-25\\n(9) SHOGO HAMADA ON THE ROAD 2015-2016“Journey of a Songwriter” - 浜田省吾 - 2018-04-25\\n(10) ラブライブ!サンシャイン!! Aqours 2nd LoveLive! HAPPY PARTY TRAIN TOUR Blu-ray Memorial BOX - Aqours - 2018-04-25";
        String dateWeekly = "2018-05-14";
        String weeklyTest = Scrapper.scrapping("weekly", dateWeekly);
        assertEquals(weekly, weeklyTest);

        String daily = "";
        String dateDaily = "2018-05-13";
        String dailyTest = Scrapper.scrapping("daily", dateWeekly);
        System.out.println(dailyTest);
        assertEquals(daily, dailyTest);
    }
}