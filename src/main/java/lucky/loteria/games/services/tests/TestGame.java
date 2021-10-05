package lucky.loteria.games.services.tests;

import java.util.stream.Stream;

public class TestGame {

    public static void main(String[] args) {
        RandomInvoker randomInvoker = new RandomInvoker();
        int numberOfSamples = 1_000_000;
        int probability = 10;
        int howManyTimesInvoked =
                Stream.generate(() -> randomInvoker.withProbability(() -> 1, () -> 0, probability))
                        .limit(numberOfSamples)
                        .mapToInt(e -> e)
                        .sum();

        System.out.println(howManyTimesInvoked);

        int monteCarloProbability = (howManyTimesInvoked * 100) / numberOfSamples;
        System.out.println(monteCarloProbability);
    }
}
