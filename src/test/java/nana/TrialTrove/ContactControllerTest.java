//package nana.TrialTrove;
//
//import nana.TrialTrove.domain.ContactDTO;
//import nana.TrialTrove.domain.ContactEntity;
//import nana.TrialTrove.service.ContactService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class ContactControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ContactService contactService;
//
//    @Test
//    public void testCreateContact() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/board/contact")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("title", "Title Test")
//                        .param("content", "Content Test")
//                        .param("writer", "김네임")
//                        .param("password", "1234"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//    }
//
//    @Test
//    public void testShowContactDetail() throws Exception {
//        // Assuming there is a contact entity with ID 1
//        mockMvc.perform(MockMvcRequestBuilders.get("/board/detail/20"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    public void testCheckPassword() throws Exception {
//        // Assuming there is a contact entity with ID 1
//        mockMvc.perform(MockMvcRequestBuilders.post("/board/checkPassword/20")
//                        .param("password", "1234"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//    }
//
//    @Test
//    public void testShowContactList() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/board/list"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    public void testShowFaqPage() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/board/faq"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    public void testShowModifyForm() throws Exception {
//        // Assuming there is a contact entity with ID 1
//        mockMvc.perform(MockMvcRequestBuilders.get("/board/modify/20"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    public void testUpdateContact() throws Exception {
//        // Assuming there is a contact entity with ID 1
//        ContactDTO updatedContact = new ContactDTO();
//        updatedContact.setTitle("Updated Title");
//        updatedContact.setContent("updated.Content");
//        updatedContact.setWriter("Updated Writer");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/board/modify/20")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("Title", updatedContact.getTitle())
//                        .param("Content", updatedContact.getContent())
//                        .param("Writer", updatedContact.getWriter()))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//    }
//
//    @Test
//    public void testDeleteContact() throws Exception {
//        // Assuming there is a contact entity with ID 1
//        mockMvc.perform(MockMvcRequestBuilders.post("/board/delete/20"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//    }
//}