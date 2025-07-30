package sweii.kochchef.demo.security;

import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonPasswordChecker {

    private List<String> commonPasswords = new ArrayList<>();

    private List<String> loadCommonPasswords() {
        commonPasswords.clear();
        try {
            ClassPathResource resource = new ClassPathResource("commonPasswords/10-million-password-list-top-1000.txt");
            Path path = resource.getFile().toPath();

            // try-with-resources because of stream close automatically
            try (Stream<String> lines = Files.lines(path)) {
                commonPasswords = lines.collect(Collectors.toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commonPasswords;
    }

    public boolean isCommonPassword(String password) {
        return loadCommonPasswords().contains(password);
    }
}
