package support;

import org.yaml.snakeyaml.Yaml;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestDataManager {
    private static final ThreadLocal<TestConfig> configThreadLocal = new ThreadLocal<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS");
    private static final ThreadLocal<String> timestampThreadLocal = ThreadLocal.withInitial(() -> LocalDateTime.now().format(formatter));
    private static final ConcurrentHashMap<String, Object> testData = new ConcurrentHashMap<>();

    public static String getTimestamp() {
        return timestampThreadLocal.get();
    }

    public static TestConfig getTestConfig() {
        if (configThreadLocal.get() == null) {
            try {
                String path = System.getProperty("user.dir") + "/src/test/resources/data/test_config.yml";
                InputStream stream = new FileInputStream(path);
                TestConfig testConfig = new Yaml().loadAs(stream, TestConfig.class);
                configThreadLocal.set(testConfig);
            } catch (FileNotFoundException e) {
                throw new Error(e);
            }
        }
        return configThreadLocal.get();
    }

    public static Map<String, String> getPositionFromFile(String entityName, String fileName) {
        Map<String, Object> restData = getMapFromYamlFile(fileName);
        Map<String, String> position = (Map<String, String>) restData.get(entityName);
        String title = position.get("title");
        title = title + "+" + getTimestamp();
        position.put("title", title);
        System.out.println(position);
        return position;
    }

    public static Map<String, String> getCandidatesFromFile(String firstName, String lastName) {
        Map<String, Object> restData = getMapFromYamlFile(lastName);
        Map<String, String> candidate = (Map<String, String>) restData.get(firstName);
        String email = candidate.get("email");
        email = email + "+" + getTimestamp();
        candidate.put("email", email);
        return candidate;
    }

    public static Map<String, String> getRecruiterCredentialsFromFile() {
        Map<String, Object> restData = getMapFromYamlFile("rest_data");
        Map<String, String> credentials = (Map<String, String>) restData.get("recruiter");
        return credentials;
    }

    public static Map<String, String> getJuniorCandidateCredentialsFromFile() {
        Map<String, Object> restData = getMapFromYamlFile("rest_data");
        Map<String, String> position = (Map<String, String>) restData.get("junior_candidate");
        String[] emails = position.get("email").split("@");
        position.put("email", emails[0] + RandomData.getRandomString() + "@" + emails[1]);

        return position;

    }

    public static Map<String, Object> getMapFromYamlFile(String fileName) {
        String path = System.getProperty("user.dir") + "/src/test/resources/data/" + fileName + ".yml";
        try (InputStream stream = new FileInputStream(path)) {
            Yaml yaml = new Yaml();
            return yaml.load(stream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + path, e);
        } catch (IOException e) {
            throw new Error("Error reading the file", e);
        }
    }
}
