package lucky.loteria.games.controller;

import com.google.gson.Gson;
import lucky.loteria.games.model.Table;
import lucky.loteria.games.repository.impl.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/v1/")
public class TableController extends ExceptionHandle {

	@Autowired
	private TableRepository tableRepository;
	@Autowired
	private Gson gson;

	public TableController() {}

	
	@GetMapping(value = "tables")
	@ResponseBody
	public ResponseEntity<Object> running(){
		List<Table> tables = tableRepository.findTablesByStatusEqualsOrderByIndexOrderAsc(Table.TableStatus.ACTIVE.getValue());
		return new ResponseEntity<>(gson.toJson(tables), HttpStatus.OK);
	}

}
