package bet.lucky.game.utils;

public class GameUtils {
    public static int countStringinString(String str, String findStr) {
        int lastIndex = 0;
        int count = 0;
        while (lastIndex != -1) {
            lastIndex = str.indexOf(findStr, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        return count;
    }

    public static String convertUS(String userName) {
        StringBuilder newUN = new StringBuilder();
        try {
            int n = userName.length();
            for (int i = 0; i < n; i++) {
                char c = userName.charAt(i);
                if (i == 0 || i == 1 || i == n - 1) {
                    newUN.append(c);
                } else {
                    newUN.append("*");
                }
            }
        } catch (Exception e) {
            return "******";
        }
        return newUN.toString();
    }
}