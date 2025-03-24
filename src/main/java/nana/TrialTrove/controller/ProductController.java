package nana.TrialTrove.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nana.TrialTrove.domain.*;
import nana.TrialTrove.repository.MemberRepository;
import nana.TrialTrove.service.MemberService;
import nana.TrialTrove.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.FieldError;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/product/*")
@Controller
@Slf4j
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Value("${uploadDir:uploads/}")
    private String uploadDir;

    @Autowired
    public ProductController(ProductService productService, MemberService memberService) {
        this.productService = productService;
        this.memberService = memberService;
    }

    @GetMapping("/enroll")
    public String showEnrollPage() {
        return "product/enroll";
    }

    // 체험 등록
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


    // 체험 디테일
    @GetMapping("/trialDetail/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        ProductDTO product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product/trialDetail";
    }

    // 체험 목록
    @GetMapping("/trialList")
    public String trialListPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "category", required = false) String category,
                                @RequestParam(value = "search", required = false) String search,
                                Model model) {
        int pageSize = 6; // 한 페이지당 6개 제품 표시
        Page<ProductDTO> productPage = productService.getProducts(page, pageSize, category, search);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "product/trialList";
    }

    // 카테고리
    @GetMapping("/categories")
    @ResponseBody
    public List<CategoryDTO> getCategoriesForTrialList() {
        return productService.getAllCategoriesWithProductCount();
    }


    // 검색
    @GetMapping("/search")
    public String searchProducts(@RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "category", required = false) String category,
                                 @RequestParam(value = "location", required = false) String location,
                                 Model model) {
        // 검색 조건이 모두 비어 있는 경우 404 페이지로 리다이렉트
        if ((keyword == null || keyword.isEmpty()) &&
                (category == null || category.isEmpty()) &&
                (location == null || location.isEmpty())) {
            return "redirect:/error/404";
        }

        List<ProductDTO> products = productService.searchProducts(keyword, category, location);

        if (products.isEmpty()) {
            // 검색 결과가 없는 경우 404 오류 페이지로 리다이렉트
            return "redirect:/error/404";
        }

        if (keyword != null && !keyword.isEmpty()) {
            // 검색어가 있을 때
            if (products.size() == 1) {
                // 검색 결과가 한 개인 경우 해당 상품의 디테일 페이지로 리다이렉트
                return "redirect:/product/trialDetail/" + products.get(0).getId();
            } else {
                // 여러 개의 결과가 있는 경우 목록 페이지로 리다이렉트
                model.addAttribute("products", products);
                return "product/trialList";
            }
        } else {
            // 검색어가 없을 때 (카테고리 또는 위치만으로 검색)
            model.addAttribute("products", products);
            return "product/trialList";
        }
    }

    // 체험 디테일 수정
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Long id,
            @RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("maxApplicants") int maxApplicants) {
        try {
            ProductDTO productDTO = ProductDTO.builder()
                    .id(id)
                    .productName(productName)
                    .description(description)
                    .maxApplicants(maxApplicants)
                    .build();  // 빌더 패턴으로 객체 생성

            productService.updateProduct(productDTO);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the product");
        }
    }

    // 체험 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"success\": false}");
        }
    }

    // 체험 관심목록

    @GetMapping("/favorite")
    public String getFavorites(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size,
                               Model model) {
        // 현재 로그인한 사용자 가져오기
        MemberDTO currentUser = memberService.getCurrentUser();
        if (currentUser == null) {
            // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        String username = currentUser.getUserId();
        Pageable pageable = PageRequest.of(page, size);

        // 페이지네이션된 관심 목록 가져오기
        Page<FavoriteDTO> favoritePage = productService.getFavorites(username, pageable);

        // 모델에 추가
        model.addAttribute("favoritePage", favoritePage);
        model.addAttribute("currentUser", currentUser);
        return "product/favorite";
    }


    @PostMapping("/favorite/{productId}")
    public ResponseEntity<Map<String, String>> addFavorite(@PathVariable("productId") Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        productService.addFavorite(productId, username);
        return ResponseEntity.ok().body(Map.of("message", "Product added to favorites"));
    }


    // 관심 체험 삭제
    @DeleteMapping("/favorite/{productId}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable("productId") Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        productService.deleteFavorite(productId, username);
        return ResponseEntity.ok().build();
    }


    // 체험 신청
    @GetMapping("/apply/{productId}")
    public String showApplyPage(@PathVariable("productId") Long productId, Model model) {

        // 사용자 이름을 사용하여 데이터베이스에서 사용자 정보를 조회
        MemberDTO currentUser = memberService.getCurrentUser();
        if (currentUser == null) {
            // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        ProductDTO productDTO = productService.getProductById(productId);


        // 모델에 사용자 정보와 productId 추가
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("product", productDTO);
        model.addAttribute("applicationDTO", new ApplicationDTO());

        return "product/apply"; // 해당 뷰로 이동
    }


    @PostMapping("/apply/{productId}")
    public String processApplication(@PathVariable("productId") Long productId, @ModelAttribute("applicationDTO") @Valid ApplicationDTO applicationDTO, BindingResult bindingResult, Model model) {
        // 현재 로그인한 사용자 정보 가져오기
        MemberDTO currentUser = memberService.getCurrentUser();

        if (currentUser == null) {
            // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "Error", error.getDefaultMessage());
            }
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("product", productService.getProductById(productId));
            return "product/apply"; // 신청 페이지
        }

        // 신청 정보 저장
        applicationDTO.setMemberId(currentUser.getId());
        applicationDTO.setProductId(productId);
        productService.apply(applicationDTO);

        ProductDTO updatedProduct = productService.getProductById(productId);
        model.addAttribute("product", updatedProduct);

        // 성공 페이지로 리다이렉트
        return "success/successMsg"; // 성공 페이지 경로
    }

    // 체험 신청 중복 확인
    @GetMapping("/check-application/{productId}")
    public ResponseEntity<String> checkApplication(@PathVariable("productId") Long productId, Authentication authentication) {
        // 현재 로그인한 사용자 정보 가져오기
        User principal = (User) authentication.getPrincipal();
        String username = principal.getUsername();

        // 실제 MemberEntity를 가져오기 위해 Optional<MemberEntity>을 사용하여 findByUserId 호출
        Optional<MemberEntity> optionalMember = memberRepository.findByUserId(username);

        if (optionalMember.isPresent()) {
            MemberEntity member = optionalMember.get();

            // 신청 여부 확인
            boolean alreadyApplied = productService.checkIfAlreadyApplied(member.getId(), productId);

            if (alreadyApplied) {
                return ResponseEntity.ok("Already applied");
            } else {
                return ResponseEntity.ok("Not applied");
            }
        } else {
            // 사용자를 찾지 못한 경우에 대한 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
    }

}



