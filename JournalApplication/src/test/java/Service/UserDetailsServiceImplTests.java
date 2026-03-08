package Service;

import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.repository.UserEntryRepo;
import com.Unemployment.JournalApplication.service.UserDetailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ActiveProfiles("dev")
public class UserDetailsServiceImplTests {

    @InjectMocks
    private UserDetailServiceImpl userDetailsService;

    @Mock
    private UserEntryRepo userRepository;

    @BeforeEach
    void setUp() {
        // Initializes the @Mock and @InjectMocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsernameTest() {
        // Arrange: Mock the behavior of the database
        when(userRepository.findByUserName(ArgumentMatchers.anyString()))
                .thenReturn(Users.builder()
                        .userName("ram")
                        .password("inrinrick")
                        .roles(new ArrayList<>())
                        .build());

        // Act: Call the service method
        UserDetails user = userDetailsService.loadUserByUsername("ram");

        // Assert: Verify the result
        Assertions.assertNotNull(user);
        Assertions.assertEquals("ram", user.getUsername());
    }
}