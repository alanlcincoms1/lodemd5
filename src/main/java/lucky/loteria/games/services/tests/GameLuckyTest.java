package lucky.loteria.games.services.tests;

import org.springframework.stereotype.Component;

@Component
public class GameLuckyTest {
//    @Autowired
//    TableRepository tableRepository;
//
//    public static void main(String[] args) {
//        for (int count = 0; count < 1; count++) {
//            int total = 60_000;
//            int i = 1;
//
//            GameLucky gameLucky = new GameLucky(null, null, null, null);
//            User user = new User();
//            user.setUid("abc_test");
//            Table table = new Table();
//            table.setInitJackpotAmount(0.0);
//            ConfigurationRedis configurationRedis = new ConfigurationRedis();
//            configurationRedis.setEvent("F,I,V,E,8,EMPTY");
//            configurationRedis.setCollection("{\"8\":{\"total\":1,\"distribution\":10102,\"prize\":15800,\"index\":\"3\",\"numberOfAlphabet\":2},\"F\":{\"total\":6,\"distribution\":10000,\"prize\":0,\"index\":\"2,5,8,10,13,16\",\"numberOfAlphabet\":1},\"I\":{\"total\":2,\"distribution\":10050,\"prize\":100,\"index\":\"4,12\",\"numberOfAlphabet\":1},\"V\":{\"total\":2,\"distribution\":10100,\"prize\":200,\"index\":\"1,9\",\"numberOfAlphabet\":1},\"E\":{\"total\":2,\"distribution\":10101,\"prize\":0,\"index\":\"7,15\",\"numberOfAlphabet\":1},\"EMPTY\":{\"total\":0,\"distribution\":15102,\"prize\":0,\"index\":\"6,11,14\",\"numberOfAlphabet\":0}}");
//            configurationRedis.setTotalDistribution(15102);
//            configurationRedis.setPrize(50_000_000.0);
//            Double totalUserWinAll = 0.0;
//
//
//            HashMap<String, Integer> results = new HashMap<>();
//            HashMap<String, Double> money = new HashMap<>();
//            while (i <= total) {
//                Bet bet = createBet(table);
//                DataResults dataResults = gameLucky.createRandomResult(table, bet, configurationRedis);
//                results.merge(dataResults.getKey(), 1, Integer::sum);
//                totalUserWinAll += dataResults.getResult().getPrize() * 1000;
//                i++;
//            }
//            List<String> keys = List.of("F", "I", "V", "E", "8");
//            int numberOf8 = (int) (results.getOrDefault("8", 0) / 2);
//            int collected = 0;
//            for (String key : keys) {
//                if (results.getOrDefault(key, 0) >= numberOf8) {
//                    collected = numberOf8;
//                } else {
//                    collected = results.getOrDefault(key, 0);
//                    break;
//                }
//            }
//            if (collected > 0) {
//                totalUserWinAll += collected * 50_000_000;
//            }
//            System.out.println("Tổng số round : " + (total));
//
//            System.out.println("Tổng số tiền trả thưởng : " + totalUserWinAll);
//
//            System.out.println("Số lần sưu tập thành công : " + collected);
//
//            System.out.println("---------");
//
//            for (Map.Entry<String, Integer> entry : results.entrySet()) {
//                String key = entry.getKey();
//                Integer value = entry.getValue();
//
//                System.out.println(key + "(" + value + ")" + ": " + (((double) value) / total) * 100);
//            }
//        }
//
//
//    }
//
//    private static Bet createBet(Table table) {
//        Bet bet = new Bet();
//        bet.setStatus(Bet.BetStatus.BET.name());
//        return bet;
//    }
}
