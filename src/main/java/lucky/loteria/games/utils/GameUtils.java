package lucky.loteria.games.utils;

public class GameUtils {

    public static String convertUS(String userName) {
        StringBuilder newUN = new StringBuilder();
        try {
            int n = userName.length();
            for(int i = 0; i < n; i++)
            {
                char c = userName.charAt(i);
                if(i==0 || i ==1 || i == n-1) {
                    newUN.append(c);
                }else {
                    newUN.append("*");
                }
            }
        } catch (Exception e) {
            return "******";
        }
        return newUN.toString();
    }
}