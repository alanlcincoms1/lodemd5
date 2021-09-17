package lucky.loteria.games.services;

import com.google.gson.Gson;
import com.squareup.okhttp.Response;
import io.sentry.Sentry;
import lombok.Data;
import lucky.loteria.games.model.*;
import lucky.loteria.games.services.game_core.GameFactory;
import lucky.loteria.games.utils.ExternalRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SocketService {
    @Value( "${public_socket}" )
    private String publicSocket;

    @Value( "${private_socket_to_user}" )
    private String privateSocketToUser;

    @Autowired
    private Gson gson;

    @Autowired
    private GameFactory gameFactory;


    public void sendBroadcast(String chanel, String event, Object object) {
        SocketCreateGameData socketData = new SocketCreateGameData();
        socketData.setChanel(chanel);
        socketData.setEvent(event);
        socketData.setData(object);

        sendData(socketData, publicSocket);
    }

    public void sendJackpotBroadcast(String chanel, String event, Table table) {

        SocketCreateGameData socketData = new SocketCreateGameData();
        socketData.setChanel(chanel);
        socketData.setEvent(event);
        socketData.setData(table);

        sendData(socketData, publicSocket);
    }

    public void sendToUser(String userId, String event, Map<String, Object> data) {
        Map<String, Object> param = new HashMap<>();
        param.put(event, data);

        SocketDataToUser socketData = new SocketDataToUser();
        socketData.setMember_id(userId);
        socketData.setData(param);

        for(int i = 1; i <= 5 ; i++) {
            try{
//                break;
                Response response = (Response) ExternalRequestUtils.makeRequest(privateSocketToUser, "POST",gson.toJson(socketData), null);
                if (response != null && response.code() == 200) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Sentry.capture(e);
            }
        }
    }

    private void sendData(SocketCreateGameData socketData, String privateSocket) {
        for(int i = 1; i <= 5 ; i++) {
            try{
                Response response = (Response) ExternalRequestUtils.makeRequest(privateSocket, "POST",gson.toJson(socketData), null);
                if (response != null && response.code() == 200) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Sentry.capture(e);
            }
        }
    }
}

@Data
class SocketCreateGameData{
    private String chanel;
    private String event;
    private Object data;
}

@Data
class SocketDataToUser{
    private String member_id;
    private Map<String,Object> data;
}
