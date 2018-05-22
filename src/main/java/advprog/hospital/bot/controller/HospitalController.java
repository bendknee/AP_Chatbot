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

        herminaDepok.put("picture", "https://static.konsula.com/images/practice/0001001000/0001000266/rumah-" +
                "sakit-hermina-depok.800x600.jpeg");
        herminaDepok.put("location", "-6.399851, 106.825205");
        herminaDepok.put("address", "Jl. Raya Siliwangi No. 50 Pancoran Mas Depok");
        herminaDepok.put("name", "Rumah Sakit Hermina Depok");
        herminaDepok.put("description", "Rumah sakit Umum");

        tuguIbu.put("picture", "https://res.cloudinary.com/vanjunior/image/upload/c_fill,f_auto,h_220,q_auto," +
                "w_250/v1/hospital/RS-Tugu-Ibu.jpg");
        tuguIbu.put("location", "-6.356472, 106.859890");
        tuguIbu.put("address", "Jl. Raya Bogor Km 29 Cimanggis - Depok");
        tuguIbu.put("name", "Rumah Sakit Tugu Ibu");
        tuguIbu.put("description", "Rumah sakit Umum");

        meilia.put("picture", "https://static.konsula.com/images/practice/0001002000/0001001119/rumah-sakit" +
                "-meilia.800x600.jpg");
        meilia.put("location", "-6.376019, 106.902071");
        meilia.put("address", "Jl. Alternatif Cibubur KM. 1, Cimanggis, Kota Depok");
        meilia.put("name", "Rumah Sakit Meilia");
        meilia.put("description", "Rumah sakit Umum");

        grhaPermata.put("picture", "https://static.konsula.com/images/practice/0001001000/0001000679/rumah-" +
                "sakit-grha-permata-ibu.800x600.jpg");
        grhaPermata.put("location", "-6.370648, 106.813218");
        grhaPermata.put("address", "Jl. KH. M. Usman No 168 Kukusan Depok");
        grhaPermata.put("name", "Rumah Sakit Grha Permata Ibu");
        grhaPermata.put("description", "Rumah sakit Umum");

        bhaktiYudha.put("picture", "http://www.rsbhaktiyudha.co.id/foto_statis/70selayangpandang.jpg");
        bhaktiYudha.put("location", "-6.397400, 106.806116");
        bhaktiYudha.put("address", "Jl. Raya Sawangan No. 2A Depok");
        bhaktiYudha.put("name", "Rumah Sakit Bhakti Yudha");
        bhaktiYudha.put("description", "Rumah sakit Umum");

        hasanahAfiah.put("picture", "https://static.konsula.com/images/practice/0001001000/0001000889/rumah-" +
                "sakit-hasanah-graha-afiah.800x600.jpg");
        hasanahAfiah.put("location", "-6.408340, 106.840459");
        hasanahAfiah.put("address", "Jl. Raden Saleh No.42 (Studio Alam TVRI) Depok");
        hasanahAfiah.put("name", "Rumah Sakit Hasanah Graha Afiah");
        hasanahAfiah.put("description", "Rumah sakit Umum");

        sentraMedika.put("picture", "https://static.konsula.com/images/practice/0001001000/0001000678/rumah-" +
                "sakit-sentra-medika-cisalak.800x600.jpg");
        sentraMedika.put("location", "-6.391011, 106.865672");
        sentraMedika.put("address", "Jl. Raya Bogor KM. 33 Cisalak, Sukmajaya, Depok");
        sentraMedika.put("name", "Rumah Sakit Sentra Medika");
        sentraMedika.put("description", "Rumah sakit Umum");

        mitraKeluarga.put("picture", "https://static.konsula.com/images/practice/0001001000/0001000873/rumah-" +
                "sakit-mitra-keluarga-depok.800x600.jpg");
        mitraKeluarga.put("location", "-6.394259, 106.823679");
        mitraKeluarga.put("address", "Jl. Margonda Raya, Pancoran Mas, Depok, Jawa Barat");
        mitraKeluarga.put("name", "Rumah Sakit Mitra Keluarga Depok");
        mitraKeluarga.put("description", "Rumah sakit Umum");

        harapanDepok.put("picture", "http://1.bp.blogspot.com/-oZFaq9REP34/ViZXDoT0EOI/AAAAAAAAAug/g2PVWXX1H" +
                "OY/s1600/Alamat%2BRumah%2BSakit%2BHarapan%2BDepok.jpg");
        harapanDepok.put("location", "-6.402518, 106.819878");
        harapanDepok.put("address", "Jl. Pemuda No.10 Depok");
        harapanDepok.put("name", "Rumah Sakit Harapan Depok");
        harapanDepok.put("description", "Rumah sakit Umum");

        puriCinere.put("picture", "https://www.garnesia.com/images/vendor/v4550.jpg");
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
                replyText = "Method dapat dijalankan tanpa mengeluarkan error";
            } else if (contentText.length() == 5 && contentText.substring(0, 5).equals("/info")) {
                int jumlahHospital = HOSPITAL_DATA.size();
                replyText = "Terdapat " + jumlahHospital + " rumah sakit sekitar Depok dalam database";
                reply(replyToken, new TextMessage(replyText));
            } else if (contentText.length() > 4 && contentText.substring(0, 4).equals("/get")) {
                int indexNum = Integer.parseInt(contentText.substring(5, 6));
                displayData(replyToken, indexNum);
                replyText = "Method dapat dijalankan tanpa mengeluarkan error";
            }
        } else if (state == STATE_ADD_LOCATION) {
            replyText = "Silahkan masukkan lokasi";
            reply(replyToken, new TextMessage(replyText));
        }

        return replyText;
    }

    @EventMapping
    public String handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {

        String replyText = "Mohon ulangi permintaan Anda";
        if (state == STATE_ADD_LOCATION) {
            state = STATE_GENERAL;
            LocationMessageContent locationMessageContent = event.getMessage();
            String replyToken = event.getReplyToken();
            getDistance(replyToken, locationMessageContent.getLatitude(), locationMessageContent.getLongitude());
            replyText = "Mohon tunggu, permintaan Anda sedang kami proses";
        };

        return replyText;
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
            String stringified = Integer.toString(randomNum.get(i));
            HashMap<String, String> hospital =  HOSPITAL_DATA.get(stringified);

            MessageAction action = new MessageAction(hospital.get("name").substring(12), "/get " + randomNum.get(i));
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
            String stringified = Integer.toString(i);
            HashMap<String, String> hospital = HOSPITAL_DATA.get(stringified);
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
        String stringified = Integer.toString(numIndex);
        HashMap<String, String> hospital = HOSPITAL_DATA.get(stringified);

        ImageMessage imageReply = new ImageMessage(hospital.get("picture"), hospital.get("picture"));
        TextMessage textReply = new TextMessage(hospital.get("name") + "\n" + hospital.get("description"));
        LocationMessage locationReply = new LocationMessage(
                hospital.get("name"), hospital.get("address"),
                Double.parseDouble(hospital.get("location").substring(0,9)),
                Double.parseDouble(hospital.get("location").substring(11))
        );

        reply(replyToken, Arrays.asList(imageReply, textReply, locationReply));
    }

    private void displayData(String replyToken, int numIndex, double jarak) {
        String stringified = Integer.toString(numIndex);
        HashMap<String, String> hospital = HOSPITAL_DATA.get(stringified);

        ImageMessage imageReply = new ImageMessage(hospital.get("picture"), hospital.get("picture"));
        TextMessage textReply = new TextMessage(hospital.get("name") + "\n" + hospital.get("description") +
                "\nJarak ke rumah sakit " + jarak + " meter");
        LocationMessage locationReply = new LocationMessage(
                hospital.get("name"), hospital.get("address"),
                Double.parseDouble(hospital.get("location").substring(0,9)),
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
