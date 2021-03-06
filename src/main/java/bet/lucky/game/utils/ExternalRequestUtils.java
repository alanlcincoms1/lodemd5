package bet.lucky.game.utils;

import com.google.gson.Gson;
import com.squareup.okhttp.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExternalRequestUtils {

    public static Object makeRequest(String urlStr, String action, String parameters, Type className) {
        log.info("requestServiceData: {}, urlService: {}, actionService: {}", parameters, urlStr, action);
        OkHttpClient client = new OkHttpClient();

        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        client.setWriteTimeout(30, TimeUnit.SECONDS);


        Request.Builder builder = new Request.Builder().url(urlStr);
        Request request = null;

        if ("GET".equals(action)) {
            request = builder.get().build();
        }
        if ("POST".equals(action)) {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, parameters);
            request = builder.post(body).build();

        }
        Response response = null;
        Gson g = new Gson();
        try {
            response = client.newCall(request).execute();
            if (response.code() != 200) {
                String result = response.body().string();
                log.info("responseService: {}", result);
                response.body().close();
                log.info("Call wallet error: {}", result);
                return null;
            }
            if (className == null) {
                String result = response.body().string();
                log.info("responseService: {}", result);
                response.body().close();
                return response;
            }

            String result = response.body().string();
            log.info("responseService: {}", result);
            response.body().close();
            return g.fromJson(result, className);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return null;
    }

    public static Object request(String urlStr, String action, String parameters, Type className) {
        log.info("requestServiceData: {}, urlService: {}, actionService: {}", parameters, urlStr, action);

        OkHttpClient client = new OkHttpClient();

        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        client.setWriteTimeout(30, TimeUnit.SECONDS);


        Request.Builder builder = new Request.Builder().url(urlStr);
        Request request = null;

        if ("GET".equals(action)) {
            request = builder.get().build();
        }
        if ("POST".equals(action)) {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, parameters);
            request = builder.post(body).build();

        }
        Response response = null;
        Gson g = new Gson();
        try {
            response = client.newCall(request).execute();

            String result = response.body().string();
            log.info("responseService: {}", result);
            if (response.code() == 500) {
                log.error(result);
                log.error(response.body().toString());
            }
            if (response.code() >= 300) {
                return null;
            }
            response.body().close();

            return g.fromJson(result, className);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return null;
    }

//    public static void main(String[] args) throws IOException {
//
//        TransferBalanceRequest transferBalanceRequest = new TransferBalanceRequest();
//        transferBalanceRequest.setToken("d83f63dc6c47b2f997b738ba6d5c73f5");
//        transferBalanceRequest.setAmount(10000);
//        transferBalanceRequest.setTransaction_id(Math.random() + "");
//
//        BetDataRequest betDataRequest = new BetDataRequest();
//        betDataRequest.setGame_id("111");
//        betDataRequest.setGame_name("111");
//        betDataRequest.setGame_round_id("111");
//        betDataRequest.setGame_ticket_id("111");
//        betDataRequest.setGame_ticket_status("BET");
//
//        betDataRequest.setGame_your_bet("Your bet");
//
//        betDataRequest.setGame_stake(11111.0);
//        betDataRequest.setGame_winlost(1000.0);
//
//        transferBalanceRequest.setData(betDataRequest);
//        List<TransferBalanceRequest> param = new ArrayList<TransferBalanceRequest>();
//        param.add(transferBalanceRequest);
//        String params = new Gson().toJson(param);
//
//
//        UserBalanceUpdateResponseDto userBalanceUpdateResponseDto = (UserBalanceUpdateResponseDto) ExternalRequestUtils.makeRequest(
//                "https://api-staging.sgame.tv/gamems/v1/agency/transfer",
//                "POST",
//                params,
//                UserBalanceUpdateResponseDto.class
//        );
//        System.out.println(userBalanceUpdateResponseDto.getData().get(0).toString());
//    }

}