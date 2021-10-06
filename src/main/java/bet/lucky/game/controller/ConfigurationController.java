package bet.lucky.game.controller;

import bet.lucky.game.model.Configuration;
import bet.lucky.game.repository.impl.ConfigurationRedisRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import bet.lucky.game.services.ConfigurationService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/configs")
public class ConfigurationController {
    private final Gson gson;

    private final ConfigurationService configurationService;

    private final ConfigurationRedisRepository configurationRedisRepository;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getConfigs() {
        List<Configuration> configurations = configurationService.getAll();
        List<Configuration> result = new ArrayList<>();
        configurations.forEach(configuration -> {
            configurationRedisRepository.save(configuration.toRedisObject());
            Configuration c = new Configuration();
            BeanUtils.copyProperties(configuration, c);
            c.setTotalDistribution(0);
            c.setCollection(gson.toJson(configuration.getSimpleCollection()));
            result.add(c);
        });
        return new ResponseEntity<>(gson.toJson(result), HttpStatus.OK);
    }
}
