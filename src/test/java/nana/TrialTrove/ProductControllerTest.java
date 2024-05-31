//package nana.TrialTrove;
//
//import nana.TrialTrove.controller.ProductController;
//import nana.TrialTrove.domain.ProductDTO;
//import nana.TrialTrove.repository.MemberRepository;
//import nana.TrialTrove.service.ProductService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//
//import java.security.Principal;
//import java.time.LocalDate;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
//
//@WebMvcTest(ProductController.class)
//public class ProductControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProductService productService;
//
//    @MockBean
//    private MemberRepository memberRepository;
//
//    @Test
//    @WithMockUser(username = "user", roles = {"USER"})
//    public void testShowEnrollPage() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/product/enroll"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("product/enroll"));
//    }
//
//    @Test
//    @WithMockUser(username = "user", roles = {"USER"})
//    public void testEnrollProduct() throws Exception {
//        Principal mockPrincipal = Mockito.mock(Principal.class);
//        Mockito.when(mockPrincipal.getName()).thenReturn("user");
//
//        ProductDTO productDTO = new ProductDTO();
//        productDTO.setProductName("Test Product");
//        productDTO.setImage("test.jpg");
//        productDTO.setSellerName("Test Seller");
//        productDTO.setLocation("Test Location");
//        productDTO.setDeadlineDate(LocalDate.now());
//        productDTO.setApplicants(0);
//        productDTO.setMaxApplicants(10);
//        productDTO.setCategoryId(1L);
//
//        Mockito.doNothing().when(productService).enrollProduct(Mockito.any(ProductDTO.class), Mockito.anyString());
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/product/enroll")
//                        .param("productName", productDTO.getProductName())
//                        .param("image", productDTO.getImage())
//                        .param("sellerName", productDTO.getSellerName())
//                        .param("location", productDTO.getLocation())
//                        .param("deadlineDate", productDTO.getDeadlineDate().toString())
//                        .param("applicants", String.valueOf(productDTO.getApplicants()))
//                        .param("maxApplicants", String.valueOf(productDTO.getMaxApplicants()))
//                        .param("categoryId", String.valueOf(productDTO.getCategoryId()))
//                        .principal(mockPrincipal))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
//
//        Mockito.verify(productService, Mockito.times(1)).enrollProduct(Mockito.any(ProductDTO.class), Mockito.anyString());
//    }
//
//    @Test
//    @WithMockUser(username = "user", roles = {"USER"})
//    public void testShowDetailPage() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/product/trialDetail"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("product/trialDetail"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("products"));
//    }
//
//    @Test
//    @WithMockUser(username = "user", roles = {"USER"})
//    public void testShowCategoryPage() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/product/trialList"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("product/trialList"));
//    }
//}
