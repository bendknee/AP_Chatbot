package advprog.hospital.bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.*;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

@LineMessageHandler
public class HospitalController {

    @Autowired
    private LineMessagingClient lineMessagingClient;

    private static final Logger LOGGER = Logger.getLogger(HospitalController.class.getName());
    private static final Map<String, HashMap<String, String>> HOSPITAL_DATA = getData();

    private static final int STATE_GENERAL = 0;
    private static final int STATE_ADD_LOCATION = 1;

    private static int state = STATE_GENERAL;


    private static Map<String, HashMap<String, String>> getData() {
        HashMap<String, HashMap<String, String>> hospitalData = new HashMap<String, HashMap<String, String>>();

        HashMap<String, String> herminaDepok = new HashMap<String, String>();
        HashMap<String, String> tuguIbu = new HashMap<String, String>();
        HashMap<String, String> meilia = new HashMap<String, String>();
        HashMap<String, String> grhaPermata = new HashMap<String, String>();
        HashMap<String, String> bhaktiYudha = new  HashMap<String, String>();
        HashMap<String, String> hasanahAfiah = new HashMap<String, String>();
        HashMap<String, String> sentraMedika = new HashMap<String, String>();
        HashMap<String, String> mitraKeluarga = new HashMap<String, String>();
        HashMap<String, String> harapanDepok = new HashMap<String, String>();
        HashMap<String, String> puriCinere = new HashMap<String, String>();

        hospitalData.put("1", herminaDepok); hospitalData.put("2", tuguIbu);
        hospitalData.put("3", meilia); hospitalData.put("4", grhaPermata);
        hospitalData.put("5", bhaktiYudha); hospitalData.put("6", hasanahAfiah);
        hospitalData.put("7", sentraMedika); hospitalData.put("8", mitraKeluarga);
        hospitalData.put("9", harapanDepok); hospitalData.put("0", puriCinere);

        herminaDepok.put("picture", "https://lh4.googleusercontent.com/JMRDkWUi5e_PqhExsxDibKXVt8UCubeL8axp9z" +
                "fZo_rxq0mGmxiHptqK1CI4hpjg-r152TegxH8jHd-edjiW=w1366-h639");
        herminaDepok.put("location", "-6.399851, 106.825205");
        herminaDepok.put("address", "Jl. Raya Siliwangi No. 50 Pancoran Mas Depok");
        herminaDepok.put("name", "Rumah Sakit Hermina Depok");
        herminaDepok.put("description", "Rumah sakit Umum");

        tuguIbu.put("picture", "https://lh3.googleusercontent.com/m0aFp3T9Iom2f6Ko0rblB85evk_GL622UOsuLUYuE1x" +
                "qwt8cS2Qngqu0BCiZGUG5R4B3Rpkc46tkl-bvQDSn=w1366-h639");
        tuguIbu.put("location", "-6.356472, 106.859890");
        tuguIbu.put("address", "Jl. Raya Bogor Km 29 Cimanggis - Depok");
        tuguIbu.put("name", "Rumah Sakit Tugu Ibu");
        tuguIbu.put("description", "Rumah sakit Umum");

        meilia.put("picture", "https://lh3.googleusercontent.com/ej8rxtxAVAvmbuFnoBABzuR_RzLWqQUlbBkRrB5st_0m" +
                "ErGKffGgGw2xTQzEOEfrSFmn8uDc99_IkOzdwf5z=w1366-h639");
        meilia.put("location", "-6.376019, 106.902071");
        meilia.put("address", "Jl. Alternatif Cibubur KM. 1, Kel. Harjamukti, Cimanggis, Kota Depok");
        meilia.put("name", "Rumah Sakit Meilia");
        meilia.put("description", "Rumah sakit Umum");

        grhaPermata.put("picture", "https://lh6.googleusercontent.com/vm5pp62UiZV14wj8YXNSFBnRuuulsdxgFdUUQn1" +
                "Ga-c5u8DevzmqEop6Xapz3HkklZ5ybcoQ7hGy_XTuirew=w1366-h639");
        grhaPermata.put("location", "-6.370648, 106.813218");
        grhaPermata.put("address", "Jl. KH. M. Usman No 168 Kukusan Depok");
        grhaPermata.put("name", "Rumah Sakit Grha Permata Ibu");
        grhaPermata.put("description", "Rumah sakit Umum");

        bhaktiYudha.put("picture", "https://lh4.googleusercontent.com/iAZVDMspAW6uJbB4VybQQfb_kbWeOvhO5qF4mUB" +
                "IgKn4ATT3pEnC-4EGYPpAne4SVFbDh0P9lua-YQVRNJRG=w1366-h639");
        bhaktiYudha.put("location", "-6.397400, 106.806116");
        bhaktiYudha.put("address", "Jl. Raya Sawangan No. 2A Depok");
        bhaktiYudha.put("name", "Rumah Sakit Bhakti Yudha");
        bhaktiYudha.put("description", "Rumah sakit Umum");

        hasanahAfiah.put("picture", "https://lh3.googleusercontent.com/UC4gOhtUxCcPolzDLOLlknQHozHEYNCkUoa4WC" +
                "t1qbP6SR6pMMWFdw8IfISJ4YPTRVVqzPFDwtkSeW7EV4JQ=w1366-h639");
        hasanahAfiah.put("location", "-6.408340, 106.840459");
        hasanahAfiah.put("address", "Jl. Raden Saleh No.42 (Studio Alam TVRI) Depok");
        hasanahAfiah.put("name", "Rumah Sakit Hasanah Graha Afiah");
        hasanahAfiah.put("description", "Rumah sakit Umum");

        sentraMedika.put("picture", "https://lh5.googleusercontent.com/4QxYtPlVDbOcgPa1N6B0q1GP1c3WTnvwnY09UE" +
                "mID61CC_g0_OJWKwXy9I89LVlohsH70oZt5rcpYA=w1366-h639");
        sentraMedika.put("location", "-6.391011, 106.865672");
        sentraMedika.put("address", "Jl. Raya Bogor KM. 33 Cisalak, Sukmajaya, Depok");
        sentraMedika.put("name", "Rumah Sakit Sentra Medika");
        sentraMedika.put("description", "Rumah sakit Umum");

        mitraKeluarga.put("picture", "https://lh6.googleusercontent.com/_Eq0bKST7iIw-acPtIR4lLRT_rqi-57YfzErC" +
                "G-NjUZhhq6MZFoVvd1z-U5OV-OxjbDMrW0g5qOBMA=w1366-h639");
        mitraKeluarga.put("location", "-6.394259, 106.823679");
        mitraKeluarga.put("address", "Jl. Margonda Raya, Pancoran Mas, Depok, Jawa Barat");
        mitraKeluarga.put("name", "Rumah Sakit Mitra Keluarga Depok");
        mitraKeluarga.put("description", "Rumah sakit Umum");

        harapanDepok.put("picture", "https://lh4.googleusercontent.com/b7SH30UHHSRUw8eoXIfCtpkRczWzWP37RFvHsK" +
                "hJltUG3E1WE-mGO-YbJEYWk9KfJFoglt_-Oo3CIw=w1366-h639");
        harapanDepok.put("location", "-6.402518, 106.819878");
        harapanDepok.put("address", "Jl. Pemuda No.10 Depok");
        harapanDepok.put("name", "Rumah Sakit Harapan Depok");
        harapanDepok.put("description", "Rumah sakit Umum");

        puriCinere.put("picture", "https://lh5.googleusercontent.com/rzy-sPEddg9wI160E1F1ucplz0kFqBij2ngWUCv4" +
                "CE6EMU0ZzgDXg0aPacMkwroc2xPcb-KiI-43jQ=w1366-h639");
        puriCinere.put("location", "-6.321839, 106.782051");
        puriCinere.put("address", "Jl. Maribaya No.1 Puri Cinere,Depok");
        puriCinere.put("name", "Rumah Sakit Puri Cinere");
        puriCinere.put("description", "Rumah sakit Umum");

        return Collections.unmodifiableMap(hospitalData);
    }

    @EventMapping
    public String handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String replyToken = event.getReplyToken();
        String contentText = content.getText();

        String replyText = "Mohon ulangi permintaan Anda";
        if (state == STATE_GENERAL) {
            if (contentText.length() == 9 && contentText.substring(0, 9).equals("/hospital")) {
                state = STATE_ADD_LOCATION;
                replyText = "Terima kasih, permintaan anda akan kami proses";
                reply(replyToken, new TextMessage(replyText));
            } else if (contentText.length() == 16 && contentText.substring(0, 16).equals("/random_hospital")) {
                TemplateMessage carouselReply =
                        new TemplateMessage("Hospital List", getCarouselTemplateMessage());
                reply(replyToken, carouselReply);
            } else if (contentText.length() == 5 && contentText.substring(0, 5).equals("/info")) {
                int jumlahHospital = HOSPITAL_DATA.size();
                replyText = "Terdapat " + jumlahHospital + " rumah sakit sekitar Depok dalam database";
                reply(replyToken, new TextMessage(replyText));
            } else if (contentText.length() > 4 && contentText.substring(0, 4).equals("/get")) {
                int indexNum = Integer.parseInt(contentText.substring(4, 5));
                displayData(replyToken, indexNum);
            }
        } else if (state == STATE_ADD_LOCATION) {
            replyText = "Silahkan masukkan lokasi";
            reply(replyToken, new TextMessage(replyText));
        }

        return replyText;
    }

    @EventMapping
    public TextMessage handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {

        String replyText = "Mohon ulangi permintaan Anda";
        if (state == STATE_ADD_LOCATION) {
            state = STATE_GENERAL;
            LocationMessageContent locationMessageContent = event.getMessage();
            String replyToken = event.getReplyToken();
            getDistance(replyToken, locationMessageContent.getLatitude(), locationMessageContent.getLongitude());
            replyText = "Mohon tunggu, permintaan Anda sedang kami proses";
        };

        return new TextMessage(replyText);
    }


    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    @NotNull
    private CarouselTemplate getCarouselTemplateMessage() {
        ArrayList<CarouselColumn> columns = new ArrayList<CarouselColumn>();
        ArrayList<Integer> randomNum = generateRandom();

        for (int i = 0; i < 3; i++) {
            HashMap<String, String> hospital =  HOSPITAL_DATA.get(randomNum.get(i));

            MessageAction action = new MessageAction("/get " + randomNum.get(i), hospital.get("name"));
            CarouselColumn column = new CarouselColumn(
                    hospital.get("picture"), hospital.get("name"), hospital.get("address"),
                    Arrays.asList(action)
            );
            columns.add(column);
        }

        return new CarouselTemplate(columns);
    }

    private void getDistance(String replyToken, double latitude, double longitude) {
        int index = 0;
        double jarak = 0;

        for (int i = 0; i < 10; i++) {
            HashMap<String, String> hospital = HOSPITAL_DATA.get(i);
            double jarakTemp = pythagorean(latitude, longitude,
                    Double.parseDouble(hospital.get("location").substring(0,9)),
                    Double.parseDouble(hospital.get("location").substring(11))
            );

            if (i == 0) {
                index = i; jarak = jarakTemp;
            } else if (jarak > jarakTemp) {
                index = i; jarak = jarakTemp;
            }
        }

        displayData(replyToken, index, jarak);
    }

    private void displayData(String replyToken, int numIndex) {
        HashMap<String, String> hospital = HOSPITAL_DATA.get(numIndex);

        ImageMessage imageReply = new ImageMessage(hospital.get("image"), hospital.get("image"));
        TextMessage textReply = new TextMessage(hospital.get("name") + "\n" + hospital.get("description"));
        LocationMessage locationReply = new LocationMessage(
                "", hospital.get("address"),
                Double.parseDouble(hospital.get("location").substring(0,9)),
                Double.parseDouble(hospital.get("location").substring(11))
        );

        reply(replyToken, Arrays.asList(imageReply, textReply, locationReply));
    }

    private void displayData(String replyToken, int numIndex, double jarak) {
        HashMap<String, String> hospital = HOSPITAL_DATA.get(numIndex);

        ImageMessage imageReply = new ImageMessage(hospital.get("image"), hospital.get("image"));
        TextMessage textReply = new TextMessage(hospital.get("name") + "\n" + hospital.get("description") +
                "\nJarak ke rumah sakit " + jarak + " meter");
        LocationMessage locationReply = new LocationMessage(
                "", hospital.get("address"),
                Double.parseDouble(hospital.get("location").substring(0,10)),
                Double.parseDouble(hospital.get("location").substring(11))
        );

        reply(replyToken, Arrays.asList(imageReply, textReply, locationReply));
    }

    private void reply(String replyToken, Message message) {
        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, Arrays.asList(message)));
    }

    private void reply(String replyToken, List<Message> message) {
        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, message));
    }

    private ArrayList<Integer> generateRandom() {
        ArrayList<Integer> randomNumber = new ArrayList<Integer>();
        ArrayList<Integer> randomNum = generateRandomHelper(randomNumber);

        return randomNum;
    }

    private ArrayList<Integer> generateRandomHelper(ArrayList<Integer> randomNum) {
        Random rand = new Random();
        boolean exists = false;
        int random = rand.nextInt(10);

        for (int num : randomNum) {
            if (num == random) {
                exists = true;
            }
        }

        if (!exists) {
            randomNum.add(random);
            if (randomNum.size() < 3) {
                randomNum = generateRandomHelper(randomNum);
            }
        } else {
            randomNum = generateRandomHelper(randomNum);
        }

        return randomNum;
    }


    private double pythagorean(double a, double b, double c, double d) {
        double aa = (a-c)*Math.pow(10, 6);
        double bb = (b-d)*Math.pow(10, 6);
        double result = Math.sqrt(Math.pow(aa, 2)+ Math.pow(bb, 2));

        return result;
    }
}
