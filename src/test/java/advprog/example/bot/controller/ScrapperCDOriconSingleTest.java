package advprog.example.bot.controller;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScrapperCDOriconSingleTest {

    @Test
    public void scrappingTest() {
        String daily = "(1) Wake Me Up - TWICE - 2018-05-16\\n(2) BE IN SIGHT - 刀剣男士 formation of つはもの - 2018-05-16\\n(3) 手遅れcaution - =LOVE - 2018-05-16\\n(4) げんきいっぱい(鬼POP激キャッチー最強ハイパーウルトラミュージック) - ヤバイTシャツ屋さん - 2018-05-16\\n(5) シンクロニシティ - 乃木坂46 - 2018-04-25\\n(6) この道を/会いに行く/坂道を上って/小さな風景 - 小田和正 - 2018-05-02\\n(7) 天竜流し - 福田こうへい - 2018-04-25\\n(8) さらせ冬の嵐 - 山内惠介 - 2018-03-28\\n(9) 恋はシュミシュミ - 郷ひろみ - 2018-05-16\\n(10) divine criminal - fripSide - 2018-05-16\\n";
        String dateDaily = "2018-05-17";
        String dailyTest = ScrapperCDOriconSingle.scrapping("daily", dateDaily);
        assertEquals(daily, dailyTest);

        String weekly = "(1) 進化理論 - BOYS AND MEN - 2018-05-09\\n(2) 泣けないぜ…共感詐欺/Uraha=Lover/君だけじゃないさ...friends(2018アコースティックVer.) - アンジュルム - 2018-05-09\\n(3) シンクロニシティ - 乃木坂46 - 2018-04-25\\n(4) 早送りカレンダー - HKT48 - 2018-05-02\\n(5) この道を/会いに行く/坂道を上って/小さな風景 - 小田和正 - 2018-05-02\\n(6) 無敵のビーナス - ばってん少女隊 - 2018-05-09\\n(7) Eclipse - 蒼井翔太 - 2018-05-09\\n(8) Changing point - i☆Ris - 2018-05-09\\n(9) WE/GO - さとり少年団 - 2018-05-09\\n(10) 誓い - 雨宮天 - 2018-05-09\\n";
        String dateWeekly = "2018-05-21";
        String weeklyTest = ScrapperCDOriconSingle.scrapping("weekly", dateWeekly);
        assertEquals(weekly, weeklyTest);

        String monthly = "(1) シンクロニシティ - 乃木坂46 - 2018-04-25\n\n(2) 早送りカレンダー - HKT48 - 2018-05-02\n(3) Ask Yourself - KAT-TUN - 2018-04-18\n(4) 春はどこから来るのか? - NGT48 - 2018-04-11\n(5) 君のAchoo! - ラストアイドル(シュークリームロケッツ) - 2018-04-18\n(6) SEXY SEXY/泣いていいよ/Vivid Midnight - Juice=Juice - 2018-04-18\n(7) ガラスを割れ! - 欅坂46 - 2018-03-07\n(8) ONE TIMES ONE - コブクロ - 2018-04-11\n(9) ODD FUTURE - UVERworld - 2018-05-02\n(10) Shanana ここにおいで - B2takes! - 2018-04-11\n";
        String dateMonthly = "2018-04";
        String monthlyTest = ScrapperCDOriconSingle.scrapping("weekly", dateMonthly);
        assertEquals(monthly, monthlyTest);

        String yearly = "(1) 願いごとの持ち腐れ - AKB48 - 2017-05-31\n(2) #好きなんだ - AKB48 - 2017-08-30\n(3) 11月のアンクレット - AKB48 - 2017-11-22\n(4) シュートサイン - AKB48 - 2017-03-15\n(5) 逃げ水 - 乃木坂46 - 2017-08-09\n(6) インフルエンサー - 乃木坂46 - 2017-03-22\n(7) いつかできるから今日できる - 乃木坂46 - 2017-10-11\n(8) 不協和音 - 欅坂46 - 2017-04-05\n(9) 風に吹かれても - 欅坂46 - 2017-10-25\n(10) Doors 〜勇気の軌跡〜 - 嵐 - 2017-11-08\n";
        String dateYearly = "2018-04";
        String yearlyTest = ScrapperCDOriconSingle.scrapping("weekly", dateYearly);
        assertEquals(yearly, yearlyTest);
    }

}