package lucky.loteria.games.utils;

import java.security.SecureRandom;
import java.util.ArrayList;

public class RandomUtils {
    public static SecureRandom r;

    public static int randomNumber(int min, int max) {
        try {
            ArrayList<String> listAlgo = new ArrayList<>();
            listAlgo.add("NativePRNG");
            listAlgo.add("NativePRNGNonBlocking");
            listAlgo.add("SHA1PRNG");
            listAlgo.add("DEFAULT");
            int random = RandomUtils.randomNumberNormal(0, listAlgo.size() - 1);
            if ("DEFAULT".equals(listAlgo.get(random))) {
                r = new SecureRandom();
            } else {
                r = SecureRandom.getInstance(listAlgo.get(random));
            }

        } catch (Exception e) {
            e.printStackTrace();
            r = new SecureRandom();
        }
        return r.nextInt((max - min) + 1) + min;
    }

    public static int randomNumberNormal(int min, int max) {
        SecureRandom randomIndex = new SecureRandom();
        return randomIndex.nextInt((max - min) + 1) + min;
    }
}