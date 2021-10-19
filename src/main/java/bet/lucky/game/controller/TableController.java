package bet.lucky.game.controller;

import bet.lucky.game.model.Tables;
import bet.lucky.game.repository.impl.TableRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/")
public class TableController {

    private final TableRepository tableRepository;
    private final Gson gson;

    @GetMapping(value = "tables")
    @ResponseBody
    public ResponseEntity<Object> running() {
        List<Tables> tables = tableRepository.findTablesByStatusEquals(Tables.TableStatus.ACTIVE.getValue());
        return new ResponseEntity<>(gson.toJson(tables), HttpStatus.OK);
    }

}
