package support;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class RandomData {
    public static String getRandomString() {
        return randomAlphabetic(5).toLowerCase();
    }
}
