package com.bot.botapakah;

import com.bot.bikun.Halte;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.Calendar;

//import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
//@JsonIgnoreProperties(ignoreUnknown = true)
@SpringBootApplication
@LineMessageHandler
public class BotApakahApplication extends SpringBootServletInitializer {
    Calendar today = Calendar.getInstance();
    private static String APIkey = "AIzaSyA3b4onLv4IQn-5AyPwmtGqFwg8L5SbAW0";
    private static String currentStage = "";
    private String path = "./src/main/java/com/bot/botapakah/DataBikun.json";
    private BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
    private ObjectMapper objectMapper = new ObjectMapper();
    private Halte[] haltes = objectMapper.readValue(bufferedReader, Halte[].class);


    @Autowired
    private LineMessagingClient lineMessagingClient;

    public BotApakahApplication() throws IOException {
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BotApakahApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(BotApakahApplication.class, args);
    }

    @EventMapping
    public List<Message> handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        today = Calendar.getInstance();
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        if (((contentText.equals("/bikun") && event.getSource() instanceof UserSource) )){
            currentStage = "bikun";
            return requestLocationMessage();
        } else if ((contentText.equals("/bikun_stop")
                && event.getSource() instanceof UserSource)) {
            currentStage = "bikun_stop";
            List<Message> messageList = new ArrayList<>();
            CarouselTemplate carouselTemplate = new CarouselTemplate(
                    Arrays.asList(
                            new CarouselColumn("https://image.ibb.co/hYHEO8/DSC_1000.jpg","Halte FH", "Fakultas Hukum",
                                    Collections.singletonList(new PostbackAction("Pilih", "0"))),
                            new CarouselColumn("https://image.ibb.co/jBJFVo/DSC_1003.jpg","Halte RIK", "Rumpun Ilmu Kesehatan",
                                    Collections.singletonList(new PostbackAction("Pilih", "1"))),
                            new CarouselColumn("https://image.ibb.co/nCn1mT/DSC_1005.jpg","Halte FIK", "Fakultas Ilmu Keperatan",
                                    Collections.singletonList(new PostbackAction("Pilih", "2"))),
                            new CarouselColumn("https://image.ibb.co/gTest8/DSC_1006.jpg","Halte FMIPA", "Rumpun Ilmu Pengetahuan Alam",
                                    Collections.singletonList(new PostbackAction("Pilih", "3"))),
                            new CarouselColumn("https://image.ibb.co/iZBSRT/DSC_1009.jpg","Halte Vokasi", "Program Pendidikan Vokasi",
                                    Collections.singletonList(new PostbackAction("Pilih", "4"))),
                            new CarouselColumn("https://image.ibb.co/gt0Afo/DSC_1010.jpg","Halte FT", "Fakultas Teknik",
                                    Collections.singletonList(new PostbackAction("Pilih", "5"))),
                            new CarouselColumn("https://image.ibb.co/eEEE6T/DSC_1011.jpg","Halte FEB", "Fakultas Ekonomi dan Bisnis",
                                    Collections.singletonList(new PostbackAction("Pilih", "6"))),
                            new CarouselColumn("https://image.ibb.co/d5Zst8/DSC_1012.jpg","Halte FIB", "Fakultas Ilmu Bahasa",
                                    Collections.singletonList(new PostbackAction("Pilih", "7"))),
                            new CarouselColumn("https://image.ibb.co/b3tkD8/DSC_1013.jpg","Halte FISIP", "Fakultas Ilmu Sosial dan Ilmu Politik",
                                    Collections.singletonList(new PostbackAction("Pilih", "8"))),
                             new CarouselColumn("https://image.ibb.co/gp9gmT/DSC_1014.jpg","Halte Psiko", "Fakultas Psikologi",
                                    Collections.singletonList(new PostbackAction("Pilih", "9")))
                    )
            );
            TextMessage textMessage = new TextMessage("Berikut halte yang ada di Universitas Indonesia. Pilih halte dengan menekan opsi 'Pilih'");
            TemplateMessage templateMessage = new TemplateMessage("Pilih Halte", carouselTemplate);
            messageList.add(textMessage);
            messageList.add(templateMessage);
            return messageList;
        } else {
            return Collections.singletonList(new TextMessage("Perintah tidak ditemukan!"));
        }
    }

    private List<Message> requestLocationMessage() {
        List<Message> messageList = new ArrayList<>();
        TextMessage textMessage = new TextMessage("Silakan kirim lokasi kamu " +
                "dengan menekan opsi 'Kirim Lokasi'");
        CarouselTemplate carouselTemplate = new CarouselTemplate(
                Arrays.asList(
                        new CarouselColumn("https://www.google.com/maps/about/images/" +
                                "home/home-benefits-1-1.jpg?" +
                                "mmfb=670787c0b70b970d52c3101316182a15",
                                "Kirim Lokasi", "Kirim lokasimu sekarang!",
                                Collections.singletonList(new URIAction("Kirim Lokasi",
                                        "line://nv/location")))
                )
        );
        TemplateMessage templateMessage =
                new TemplateMessage("Kirim lokasi kamu", carouselTemplate);

        messageList.add(textMessage);
        messageList.add(templateMessage);
        return messageList;
    }

    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> LocEvent) throws IOException{
        today = Calendar.getInstance();
        LocationMessageContent userloc = LocEvent.getMessage();
        double latitude = userloc.getLatitude();
        double longitude = userloc.getLongitude();
        String jawaban  = "your latitude : " + latitude + "\nyour longitude: " + longitude;
        String replyToken = LocEvent.getReplyToken();
        hitungJarakKeHalte(latitude, longitude);

        //balasChat(replyToken, jawaban);

        Arrays.sort(haltes);
        Halte bikun = haltes[0];
        Double timeFinal = getTime(today.get(Calendar.MINUTE));
        jawaban = "Halte terdekat dari kamu adalah : "+ bikun.getName()+ " dengan jarak : "+ bikun.getDistance() + " m";
        String jawaban2 = jawaban +
                "\n"+"Bikun selanjutnya akan datang dalam " + String.valueOf(timeFinal)+" menit";
        balasChatDenganRandomJawaban(replyToken, jawaban2);

    }

    private void hitungJarakKeHalte(double latitude, double longitude) throws IOException {
        for (Halte halte: haltes) {
            double hLatitude = halte.getLatitude();
            double hLongitude = halte.getLongitude();

            String apiUrl =
                    "https://maps.googleapis.com/maps/api/distancematrix/json?units=metrics";
            String origin =
                    String.format("&origins=%s,%s", latitude, longitude);
            String destination =
                    String.format("&destinations=%s,%s", hLatitude, hLongitude);
            String apiKey =
                    "&key=" + APIkey;

            String url = String.format("%s%s%s%s", apiUrl, origin, destination, apiKey);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Cache-Control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            assert response.body() != null;
            JSONObject jsonObject = new JSONObject(response.body().string());
            //LOGGER.info(jsonObject.toString());
            int distanceFromOrigin = (int) jsonObject.getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray("elements")
                    .getJSONObject(0)
                    .getJSONObject("distance")
                    .get("value");
            //int distanceFromOrigin = 11;

            halte.setDistance(distanceFromOrigin);
        }
    }





    @EventMapping
    public List<Message> handlePostbackEvent(PostbackEvent event) {
        today = Calendar.getInstance();
        int chosenNumber = Integer.parseInt(event.getPostbackContent().getData());
        List<Message> messageList = new ArrayList<>();
        if (chosenNumber == 0){
            double currentLatitude = -6.36456411;
            double currentLongitude = 106.83230214;
            LocationMessage halteLocation = new LocationMessage("Halte FH", "Universitas Indonesia",currentLatitude,currentLongitude);
            messageList.add(halteLocation);
        }
        else if (chosenNumber == 1){
            double currentLatitude = -6.37174054;
            double currentLongitude = 106.82947064;
            LocationMessage halteLocation = new LocationMessage("Halte RIK", "Universitas Indonesia",currentLatitude,currentLongitude);
            messageList.add(halteLocation);
        }
        else if (chosenNumber == 2){
            double currentLatitude = -6.37112987;
            double currentLongitude = 106.82679275;
            LocationMessage halteLocation = new LocationMessage("Halte FIK", "Universitas Indonesia",currentLatitude,currentLongitude);
            messageList.add(halteLocation);
        }
        else if(chosenNumber == 3){
            double currentLatitude = -6.37007718;
            double currentLongitude = 106.82594085;
            LocationMessage halteLocation = new LocationMessage("Halte FMIPA", "Universitas Indonesia",currentLatitude,currentLongitude);
            messageList.add(halteLocation);
        }
        else if (chosenNumber == 4){
            double currentLatitude = -6.36589743;
            double currentLongitude = 106.82190681;
            LocationMessage halteLocation = new LocationMessage("Halte Vokasi", "Universitas Indonesia",currentLatitude,currentLongitude);
            messageList.add(halteLocation);
        }
        else if (chosenNumber == 5){
            double currentLatitude = -6.36094795;
            double currentLongitude = 106.82334065;
            LocationMessage halteLocation = new LocationMessage("Halte FT", "Universitas Indonesia",currentLatitude,currentLongitude);
            messageList.add(halteLocation);
        }
        else if(chosenNumber == 6){
            double currentLatitude = -6.35938051;
            double currentLongitude = 106.82608724;
            LocationMessage halteLocation = new LocationMessage("Halte FEB", "Universitas Indonesia",currentLatitude,currentLongitude);
            messageList.add(halteLocation);
        }
        else if(chosenNumber == 7){
            double currentLatitude = -6.36122518;
            double currentLongitude = 106.82944536;
            LocationMessage halteLocation = new LocationMessage("Halte FIB", "Universitas Indonesia",currentLatitude,currentLongitude);
            messageList.add(halteLocation);
        }
        else if(chosenNumber == 8){
            double currentLatitude = -6.36170697;
            double currentLongitude = 106.82997489;
            LocationMessage halteLocation = new LocationMessage("Halte FISIP", "Universitas Indonesia",currentLatitude,currentLongitude);
            messageList.add(halteLocation);
        }
        else if(chosenNumber == 9){
            double currentLatitude = -6.36260265;
            double currentLongitude = 106.83084393;
            LocationMessage halteLocation = new LocationMessage("Halte FPsi", "Universitas Indonesia",currentLatitude,currentLongitude);
            messageList.add(halteLocation);
        }
        Double timeFinal = getTime(today.get(Calendar.MINUTE));
        TextMessage hospitalDetail = new TextMessage("Bikun selanjutnya akan datang dalam " + String.valueOf(timeFinal)+" menit");
        messageList.add(hospitalDetail);
        currentStage = "";
        return messageList;
    }


    private Double getTime(int jawaban){
        double hasiljam = 0;
        double hasilmenit = 0;
        if(jawaban<= 15){
            hasilmenit = 15-jawaban;
        }
        else if(jawaban > 15 && jawaban <=30){
            hasilmenit = 30-jawaban;
        }
        else if(jawaban > 30 && jawaban <= 45){
            hasilmenit = 45 -jawaban;
        }
        else if(jawaban > 45 && jawaban <= 60){
            hasilmenit = 60 - jawaban;
        }
        return hasilmenit;
    }
    private void balasChatDenganRandomJawaban(String replyToken, String jawaban){
        TextMessage jawabanDalamBentukTextMessage = new TextMessage(jawaban);
        try {
            lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, jawabanDalamBentukTextMessage))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Ada error saat ingin membalas chat");
        }
    }

}
