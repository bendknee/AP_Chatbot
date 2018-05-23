package advprog.weather.bot.controller;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.neovisionaries.i18n.CountryCode;

import java.util.*;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;


@LineMessageHandler
public class WeatherController {

    private static final Logger LOGGER = Logger.getLogger(WeatherController.class.getName());
    private Set<String> personalTrigger = new HashSet<>();
    private HashMap<String, String> tempConfig = new HashMap<>();
    private final String baseUrl = "https://api.openweathermap.org/data/2.5/weather?";
    private final String apiKey = "appid=187bd7d86855cb7ee05515d77863583d";


    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        String content = event.getMessage().getText();
        Source source = event.getSource();
        if (source instanceof GroupSource) {
            if (content.toLowerCase().contains("cuaca di")) {
                ArrayList<String> inputSplit = new ArrayList<>();
                inputSplit.addAll(Arrays.asList(content.toLowerCase().split(" ")));
                int indexKeyWord = inputSplit.indexOf("cuaca");
                String city = inputSplit.get(indexKeyWord + 2);
                String url = baseUrl + apiKey + "&q=" + city;
                ArrayList<String> requiredDatas = fetchDataApiRequest(url);
                return new TextMessage(textResponseFormatter(requiredDatas, null));
            }
        } else {
            if (content.equals("/weather")) {
                personalTrigger.add(source.getSenderId());
                tempConfig.put(source.getSenderId(), "celcius");
                return new TextMessage("Please submit a location straightaway "
                        + "with Line's 'Share location' feature below. ☟");
            } else if (content.equals("/configure_weather")) {
                if (content.split(" ").length == 1) {
                    return new TextMessage("Please set your temperature configuration "
                            + "by typing in this format :\n"
                            + "/configure_weather [celsius/kelvin/fahrenheit]");
                } else {
                    String unit = content.split(" ")[1].toLowerCase();
                    if (unit.equals("celsius") | unit.equals("kelvin")
                            | unit.equals("fahrenheit")) {
                        tempConfig.put(source.getSenderId(), unit);
                        return new TextMessage("Configuration changed to " + unit);
                    } else {
                        return new TextMessage(unit + " is not a valid degree unit.");
                    }
                }
            }
        }
        return null;
    }

    @EventMapping
    public TextMessage handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LOGGER.fine(String.format("LocationMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage().getTitle()));
        LocationMessageContent content = event.getMessage();
        Source source = event.getSource();

        if (!(source instanceof GroupSource) & personalTrigger.contains(source.getSenderId())) {
            personalTrigger.remove(source.getSenderId());
            String latitude = Double.toString(content.getLatitude());
            String longitude = Double.toString(content.getLongitude());
            String url = baseUrl + apiKey + "&lat=" + latitude + "&lon=" + longitude;
            ArrayList<String> requiredDatas = fetchDataApiRequest(url);
            return new TextMessage(textResponseFormatter(requiredDatas, source.getSenderId()));
        }
        return null;
    }

    private ArrayList<String> fetchDataApiRequest(String url) {
        ArrayList<String> data = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        try {
            String stringifiedJson = restTemplate.getForObject(url, String.class, header);
            JSONObject parsedJson = new JSONObject(stringifiedJson);
            data.add(parsedJson.getString("name"));         // City name
            String countryCode = parsedJson.getJSONObject("sys").getString("country");
            data.add(CountryCode.getByCode(countryCode).getName()); // Country name
            JSONObject weatherObject = parsedJson.getJSONArray("weather").getJSONObject(0);
            data.add(weatherObject.getString("main"));      // Main weather condition
            data.add(parsedJson.getJSONObject("wind").getString("speed"));   // Wind Speed (m/s)
            data.add(parsedJson.getJSONObject("main").getString("temp"));   // Temperature (K)
            data.add(parsedJson.getJSONObject("main").getString("humidity"));   // Humidity
            return data;
        } catch (Exception e) {
            data.clear();
            data.add("City not found. Try another city");
            return data;
        }
    }

    private String textResponseFormatter(ArrayList<String> datas, String userId) {
        if (datas.size() > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Weather at your position (");
            sb.append(datas.get(0)).append(", ");
            sb.append(datas.get(1)).append("):\n");
            sb.append(datas.get(2)).appendCodePoint(emojiSelector(datas.get(2)));
            sb.append("\nWind speed : ");
            sb.append(datas.get(3)).append(" meter/sec\n");
            sb.append("Temperature : ");
            if (userId == null) {
                sb.append(defaultConversion(datas.get(4))).append("° Celsius\n");
            } else {
                sb.append(degreeConfigurated(datas.get(4), userId));
            }
            sb.append("Humidity : ");
            sb.append(datas.get(5)).append("%");
            return sb.toString();
        } else {
            return datas.get(0);
        }
    }

    private String degreeConfigurated(String kelvin, String userId) {
        String unit = tempConfig.get(userId);
        if (unit.equals("celsius")) {
            double temperature = Double.parseDouble(kelvin);
            temperature -= 273.15;
            return String.format("%,.2f", temperature) + "° Celsius\n";
        } else if (unit.equals("kelvin")) {
            return kelvin + "° Kelvin\n";
        } else if (unit.equals("fahrenheit")) {
            double temperature = Double.parseDouble(kelvin);
            temperature = 9/5 * (temperature - 273.15) + 32;
            return String.format("%,.2f", temperature) + "° Fahrenheit\n";
        }
        return null;
    }

    private String defaultConversion(String kelvin) {
        double temperature = Double.parseDouble(kelvin);
        temperature -= 273.15;
        return String.format("%,.2f", temperature);
    }

    private int emojiSelector(String weather) {
        if (weather.contains("Clouds")) {
            return 0x1000AC;
        } else if (weather.contains("Clear")) {
            return 0x1000A9;
        } else if (weather.contains("Rain")) {
            return 0x1000AA;
        } else if (weather.contains("Thunderstorm")) {
            return 0x10003A;
        } else if (weather.contains("Snow")) {
            return 0x1000AB;
        } else if (weather.contains("Mist")) {
            return 0x10002A;
        }
        return 0x1000A8;
    }
}
