package Repository;

import com.Unemployment.JournalApplication.repository.UserEntryRepoImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryImplTests {

    @Autowired
    private UserEntryRepoImpl userRepository;

    @Disabled("tested")
    @Test
    void testSaveNewUser() {
        // Verifies that the custom MongoDB query returns a list (even if empty)
        // and doesn't throw a NullPointerException.
        Assertions.assertNotNull(userRepository.getUserForSA());
    }
}