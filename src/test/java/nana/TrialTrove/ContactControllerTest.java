package nana.TrialTrove;

import nana.TrialTrove.controller.ContactController;
import nana.TrialTrove.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactController.class)
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService; // ContactService를 Mock으로 대체하여 주입

    @Test
    public void givenInvalidContact_whenCreateContact_thenReturnsValidationError() throws Exception {
        String invalidContactJson = "{ \"title\": \"\", \"writer\": \"\", \"content\": \"\", \"password\": \"\" }";

        mockMvc.perform(post("/contact")
                        .contentType("application/json")
                        .content(invalidContactJson))
                .andExpect(status().isBadRequest());
    }

}