package com.bot.botapakah;

import com.bot.botapakah.BotApakahApplication.StatusCode;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;


import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BotApakahApplicationTests {

    @Test
    public void contextLoads() {
    }
    public static MessageEvent<TextMessageContent> createDummyTextMessage(String text) {
        return new MessageEvent<>("replyToken", new UserSource("userId"),
                new TextMessageContent("id", text),
                Instant.parse("2018-01-01T00:00:00.000Z"));
    }

    public static MessageEvent<ImageMessageContent> createDummyImageMessage(){
        return new MessageEvent<>("replyToken", new UserSource("userId"),
                new ImageMessageContent("id"), Instant.parse("2018-01-01T00:00:00.000Z"));
    }

    @Test
    public void getStatusTest() {
        assertEquals(StatusCode.UNKNOWN_HOST, StatusCode.getStatus(1));
        assertEquals(StatusCode.SUCCESS, StatusCode.getStatus(200));
        assertEquals(StatusCode.BAD_REQUEST, StatusCode.getStatus(400));
        assertEquals(StatusCode.UNAUTHORIZED, StatusCode.getStatus(401));
        assertEquals(StatusCode.FORBIDDEN, StatusCode.getStatus(403));
        assertEquals(StatusCode.NOT_FOUND, StatusCode.getStatus(404));
        assertEquals(StatusCode.FILE_TOO_BIG, StatusCode.getStatus(413));
        assertEquals(StatusCode.UPLOAD_LIMITED, StatusCode.getStatus(429));
        assertEquals(StatusCode.INTERNAL_SERVER_ERROR, StatusCode.getStatus(500));
        assertEquals(StatusCode.SERVICE_UNAVAILABLE, StatusCode.getStatus(502));
        assertEquals(StatusCode.UNKNOWN_ERROR, StatusCode.getStatus(-1));
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("Couldn't find api.imgur.com, "
                        + "are you connected to the internet?",
                StatusCode.UNKNOWN_HOST.getDescription());
    }

    @Test
    public void toStringTest() {
        assertEquals("StatusCode - Name: UNKNOWN_HOST - "
                        + "HttpCode: 1 - Description: Couldn't find api.imgur.com, "
                        + "are you connected to the internet?",
                StatusCode.UNKNOWN_HOST.toString());
    }


}