package Service;

import com.Unemployment.JournalApplication.JournalApplication; // Imported to link the context
import com.Unemployment.JournalApplication.service.EmailService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = JournalApplication.class) // Explicitly points to your main config
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    // Remove @Disabled if you want to run this test immediately
    @Test
    void testSendMail() {
        emailService.sendEmail("carnation_duchess348@slmail.me",
                "Testing Java mail sender",
                "Hi, aap kaise hain ?");
    }
}