package nana.TrialTrove.controller;

import lombok.extern.slf4j.Slf4j;
import nana.TrialTrove.domain.CategoryDTO;
import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.domain.ProductDTO;
import nana.TrialTrove.domain.ProductEntity;
import nana.TrialTrove.repository.MemberRepository;
import nana.TrialTrove.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/product/*")
@Controller
@Slf4j
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    private MemberRepository memberRepository;

    @Value("${uploadDir:uploads/}")
    private String uploadDir;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/enroll")
    public String showEnrollPage() {
        return "product/enroll";
    }

    @PostMapping("/enroll")
    public String enrollProduct(@ModelAttribute ProductDTO productDTO, Principal principal, @RequestParam("imageFile") MultipartFile imageFile) {

        // 이미지 파일 처리
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename();
            productDTO.setImage(imageName);

            // 파일 저장 로직
            String uploadDir = "/Users/nana/Documents/work/uploads";
            try {
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }
                imageFile.transferTo(new File(uploadDir + imageName));
            } catch (IOException e) {
                e.printStackTrace();
                // 오류 처리 로직
            }
        }

        // 현재 로그인한 사용자의 아이디 가져오기
        String userId = principal.getName();

        // 사용자 정보 가져오기
        MemberEntity member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        System.out.println("ProductDTO: " + productDTO);

        // DTO를 엔티티로 변환하여 상품 등록
        productService.enrollProduct(productDTO, member);
        logger.info("Product enrolled successfully, redirecting to home page.");

        return "redirect:/";
    }


    @GetMapping("/trialDetail")
    public String showDetailPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "product/trialDetail";
    }

    @GetMapping("/trialList")
    public String trialListPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "category", required = false) String category,
                                @RequestParam(value = "search", required = false) String search,
                                Model model) {
        int pageSize = 6; // 한 페이지당 6개 제품 표시
        Page<ProductEntity> productPage;

        if (search != null && !search.isEmpty()) {
            // 검색어가 있는 경우
            productPage = productService.searchProductsByName(search, PageRequest.of(page, pageSize));
        } else if (category != null && !category.isEmpty()) {
            // 카테고리가 있는 경우
            productPage = productService.getProductsByCategory(category, PageRequest.of(page, pageSize));
        } else {
            // 검색어와 카테고리 모두 없는 경우
            productPage = productService.getProducts(PageRequest.of(page, pageSize));
        }

        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(product -> new ProductDTO(
                        product.getProductName(),
                        product.getImage(),
                        product.getLocation(),
                        product.getMaxApplicants(),
                        product.getActivityType(),
                        product.getCategory().getName()
                ))
                .collect(Collectors.toList());

        model.addAttribute("products", productDTOs);
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "product/trialList";
    }

    @GetMapping("/categories")
    @ResponseBody
    public List<CategoryDTO> getCategoriesForTrialList() {
        return productService.getAllCategoriesWithProductCount();
    }
}
