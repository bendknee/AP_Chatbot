package com.bot.Getter;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.profile.UserProfileResponse;
import retrofit2.Response;

import java.io.IOException;

public class Getter {
    String AccessToken;

    public Getter(String accessToken){
        this.AccessToken = accessToken;
    }

    public String getId(Source source){
        String id=null;
        String type=null;
        if (source instanceof GroupSource) {
            id = ((GroupSource) source).getGroupId();
            type="group";
        } else if (source instanceof RoomSource) {
            id = ((RoomSource) source).getRoomId();
            type="room";
        } else{
            id = source.getUserId();
            type="personal";
        }
        return id;
    }

    public String getType(Source source){
        String type = null;
        if (source instanceof GroupSource)
            type="group";
        else if(source instanceof RoomSource)
            type="room";
        else
            type="personal";
        return type;
    }

    public String getGroupMemberName(String type, String senderId, String userId){
        String userName = "";
        try{
            Response<UserProfileResponse> response =
                    LineMessagingServiceBuilder
                            .create(AccessToken)
                            .build()
                            .getMemberProfile(type, senderId, userId)
                            .execute();
            if (response.isSuccessful()){
                UserProfileResponse profileResponse = response.body();
                userName = profileResponse.getDisplayName();
            } else{
                System.out.println(response.code() + " " + response.message());
            }
        } catch (Exception ex){
            System.out.println("Gagal get Group Member Name : " + ex.toString());
        }
        return userName;
    }

    public String getName(String userId){
        String name = "";
        try {
            Response<UserProfileResponse> response =
                    LineMessagingServiceBuilder
                            .create(AccessToken)
                            .build()
                            .getProfile(userId)
                            .execute();
            if (response.isSuccessful()){
                UserProfileResponse profileResponse = response.body();
                name = profileResponse.getDisplayName();
            } else{
                System.out.println(response.code() + " " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }
}