package bet.lucky.game.services;

import bet.lucky.game.model.thirdparty.TicketBonus;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import bet.lucky.game.utils.ExternalRequestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class TicketBonusService {

    private final Gson gson;

    @Value(value = "${kafka-service.url}")
    private String kafKaServiceUrl;

    public TicketBonus getTicketBonus(String uid) {
        return (TicketBonus) ExternalRequestUtils.makeRequest(kafKaServiceUrl + uid, HttpMethod.GET.name(), null, TicketBonus.class);
    }

    public TicketBonus updateTicketBonus(String uid, int ticketUsed) {
        HashMap<String, Integer> dataRequest = new HashMap<>();
        dataRequest.put("ticketUsed", ticketUsed);
        return (TicketBonus) ExternalRequestUtils.makeRequest(kafKaServiceUrl + uid, HttpMethod.POST.name(), gson.toJson(dataRequest), TicketBonus.class);
    }
}
