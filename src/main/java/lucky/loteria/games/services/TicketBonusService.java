package lucky.loteria.games.services;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lucky.loteria.games.model.thirdparty.TicketBonus;
import lucky.loteria.games.utils.ExternalRequestUtils;
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
