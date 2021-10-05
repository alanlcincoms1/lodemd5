package lucky.loteria.games.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lucky.loteria.games.external_dto.response.LuckyTopResponse;
import lucky.loteria.games.internal_dto.TopQuery;
import lucky.loteria.games.model.ILuckyTop;
import lucky.loteria.games.model.redis.UserRedis;
import lucky.loteria.games.repository.impl.UserCollectRepository;
import lucky.loteria.games.services.UserService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/collects")
public class UserCollectController {
    private static final int MAX_LIMIT = 60;
    public static final int MAX_PAGE = 20;

    private final Gson gson;

    private final UserService userService;

    private final UserCollectRepository userCollectRepository;

    @GetMapping(value = "/{token}/{tableId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCollect(@PathVariable String token, @PathVariable long tableId, HttpServletRequest httpServletRequest) {
        UserRedis user = userService.getUserByToken(token, httpServletRequest);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vui lòng đăng nhập");
        }
        return new ResponseEntity<>(gson.toJson(userCollectRepository.findByTableIdAndUid(tableId, user.getUid())), HttpStatus.OK);
    }

    @GetMapping(value = "/top/{tableId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTop(@PathVariable long tableId, TopQuery topQuery) {
        if (topQuery.getLimit() > MAX_LIMIT) topQuery.setLimit(MAX_LIMIT);
        if (topQuery.getPage() > MAX_PAGE) topQuery.setLimit(MAX_PAGE);
        Pageable sortedByPriceDescNameAsc = PageRequest.of(topQuery.getPage(),
                topQuery.getLimit(),
                Sort.by("totalAmountWin").descending());

        Page<ILuckyTop> luckyTopPage = userCollectRepository.findAllByTableId(tableId, sortedByPriceDescNameAsc);

        if (!luckyTopPage.hasContent()) return new ResponseEntity<>(gson.toJson(luckyTopPage), HttpStatus.OK);

        PageImpl result = new PageImpl<>(
                luckyTopPage.getContent().stream().map(l -> new LuckyTopResponse(l.getId(), l.getUsername(), l.getTotalAmountWin()))
                        .collect(Collectors.toList()),
                sortedByPriceDescNameAsc, luckyTopPage.getTotalElements());
        return new ResponseEntity<>(gson.toJson(result), HttpStatus.OK);
    }
}
