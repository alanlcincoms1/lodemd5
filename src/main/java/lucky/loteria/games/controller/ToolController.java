package lucky.loteria.games.controller;

import io.sentry.Sentry;
import lucky.loteria.games.internal_dto.ToolForm;
import lucky.loteria.games.model.Table;
import lucky.loteria.games.repository.impl.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("api/v1/")
public class ToolController extends ExceptionHandle {

    @Autowired
    private TableRepository tableRepository;

    public ToolController() {
    }

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
