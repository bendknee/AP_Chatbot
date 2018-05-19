package advprog.example.bot.controller;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScrapperTest {

    @Test
    public void testScrapping() {
        String weekly = "(1) スター・ウォーズ/最後�?�ジェダイ MovieNEX(�?回版) - マーク・�?ミル - 2018-04-25\n(2) ヴァイオレット・エヴァーガーデン�A - アニメーション - 2018-05-02\n(3) オリエント急行殺人事件 2枚組ブルーレイ&DVD - ケ�?ス・ブラナー - 2018-05-02\n(4) 斉木楠雄�?�Ψ難 豪�?�版ブルーレイ&DVDセット�?�?回生産�?定】 - 山崎賢人 - 2018-05-02\n(5) ラブライブ!サンシャイン!! 2nd Season 5�?特装�?定版】 - アニメーション - 2018-04-24\n(6) ミックス。 豪�?�版Blu-ray - 新垣�?衣,瑛太 - 2018-05-02\n(7) GREEN MIND AT BUDOKAN - 秦 基�?� - 2018-05-02\n(8) THE IDOLM@STER SideM GREETING TOUR 2017 〜BEYOND THE DREAM〜 LIVE Blu-ray - アイドルマスターSideM - 2018-04-25\n(9) SHOGO HAMADA ON THE ROAD 2015-2016“Journey of a Songwriter�? - 浜田�?�?� - 2018-04-25\n(10) ラブライブ!サンシャイン!! Aqours 2nd LoveLive! HAPPY PARTY TRAIN TOUR Blu-ray Memorial BOX - Aqours - 2018-04-25\n";
        String dateWeekly = "2018-05-14";
        String weeklyTest = Scrapper.scrapping("weekly", dateWeekly);
        assertEquals(weekly, weeklyTest);

        String daily = "Input tanggal salah atau tidak ditemukan!!!\n\n\nFormat input\n/oricon bluray <weekly|daily> YYYY-MM-DD";
        String dateDaily = "2018-55-13";
        String dailyTest = Scrapper.scrapping("daily", dateDaily);
        assertEquals(daily, dailyTest);
    }
}