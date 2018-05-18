package advprog.example.bot.anime;


public class Main {
    public static void main(String[] args) {
        ListAnimeBot lab = new ListAnimeBot();

        /*Map<String, Integer> map = lab.getMapAnime();
        System.out.println(map.size());

        for (String name: map.keySet()){

            String key =name.toString();
            String value = map.get(name).toString();
            System.out.println(key + " " + value);


        }
        */
        /*
        System.out.println(lab.getListAnime());
        */

        AnimeBot ab = new AnimeBot("Akkun to Kanojo");
    }

}
