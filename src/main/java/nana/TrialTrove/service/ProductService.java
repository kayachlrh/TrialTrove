package nana.TrialTrove.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import nana.TrialTrove.domain.*;
import nana.TrialTrove.repository.ApplicationRepository;
import nana.TrialTrove.repository.CategoryRepository;
import nana.TrialTrove.repository.MemberRepository;
import nana.TrialTrove.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final ApplicationRepository applicationRepository;


    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, MemberRepository memberRepository, ApplicationRepository applicationRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
        this.applicationRepository = applicationRepository;
    }


    // 모든 상품 조회
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<ProductEntity> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<ProductEntity> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategory_Name(category, pageable);
    }

    public Page<ProductEntity> searchProductsByName(String productName, Pageable pageable) {
        return productRepository.findByProductName(productName, pageable);
    }




    // 관리자 대시보드: 모든 신청 정보와 관련된 상품 정보를 가져옴
    public Page<AdminDashboardDTO> getAllApplicationsWithProductInfo(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("applicationDate").descending());
        Page<ApplicationEntity> applicationPage = applicationRepository.findAll(pageable);

        List<AdminDashboardDTO> dashboardDTOs = applicationPage.getContent().stream().map(application -> {
            ProductEntity product = application.getProduct();
            MemberEntity member = application.getMember();
            String userId = member.getUserId();

            return new AdminDashboardDTO(
                    product.getProductName(),
                    application.getApplicationDate(),
                    product.getDeadlineDate(),
                    application.getStatus(),
                    product.getId(),
                    application.getId(),
                    userId
            );
        }).collect(Collectors.toList());

        return new PageImpl<>(dashboardDTOs, pageable, applicationPage.getTotalElements());
    }


    @Transactional
    public void enrollProduct(ProductDTO productDTO, MemberEntity member) {


        // DTO를 엔티티로 변환
        ProductEntity product = new ProductEntity();

        System.out.println("Category ID: " + productDTO.getCategoryId());

        CategoryEntity categoryEntity = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + productDTO.getCategoryId()));
        product.setCategory(categoryEntity);

        product.setProductName(productDTO.getProductName());
        product.setSellerName(productDTO.getSellerName());
        product.setLocation(productDTO.getLocation());
        product.setDeadlineDate(productDTO.getDeadlineDate());
        product.setMaxApplicants(productDTO.getMaxApplicants());
        product.setImage(productDTO.getImage());
        product.setOwnerId(member);
        product.setDescription(productDTO.getDescription());
        product.setActivityType(productDTO.getActivityType());

        // 엔티티 저장
        productRepository.save(product);
    }

    // 카테고리
    public List<CategoryDTO> getAllCategoriesWithProductCount() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = new ArrayList<>();

        for (CategoryEntity category : categories) {
            Long productCount = categoryRepository.countProductsByCategoryId(category.getId());
            categoryDTOs.add(new CategoryDTO(category.getName(), productCount));
        }

        return categoryDTOs;
    }

    // 체험 목록
    public Page<ProductDTO> getProducts(int page, int size, String category, String search) {
        Page<ProductEntity> productPage;
        PageRequest pageRequest = PageRequest.of(page, size);

        if (search != null && !search.isEmpty()) {
            productPage = productRepository.findByProductName(search, pageRequest);
        } else if (category != null && !category.isEmpty()) {
            productPage = productRepository.findByCategory_Name(category, pageRequest);
        } else {
            productPage = productRepository.findAll(pageRequest);
        }

        return productPage.map(product -> ProductDTO.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .image(product.getImage())
                .sellerName(product.getSellerName())
                .location(product.getLocation())
                .deadlineDate(product.getDeadlineDate())
                .applicants(product.getApplicants())
                .maxApplicants(product.getMaxApplicants())
                .description(product.getDescription())
                .activityType(product.getActivityType())
                .categoryName(product.getCategory().getName())
                .build());
    }


    // 체험 디테일
    public ProductDTO getProductById(Long id) {
        Optional<ProductEntity> productEntityOpt = productRepository.findById(id);
        if (productEntityOpt.isPresent()) {
            ProductEntity productEntity = productEntityOpt.get();
            return ProductDTO.builder()
                    .id(productEntity.getId())
                    .productName(productEntity.getProductName())
                    .image(productEntity.getImage())
                    .sellerName(productEntity.getSellerName())
                    .location(productEntity.getLocation())
                    .deadlineDate(productEntity.getDeadlineDate())
                    .applicants(productEntity.getApplicants())
                    .maxApplicants(productEntity.getMaxApplicants())
                    .description(productEntity.getDescription())
                    .activityType(productEntity.getActivityType())
                    .categoryName(productEntity.getCategory().getName())
                    .build();
        } else {
            throw new RuntimeException("Product not found with id: " + id); // 예외처리
        }
    }

    // 검색
    public List<ProductDTO> searchProducts(String keyword, String category, String location) {
        // 모든 조건이 비어 있는 경우 빈 리스트 반환
        if ((keyword == null || keyword.isEmpty()) &&
                (category == null || category.isEmpty()) &&
                (location == null || location.isEmpty())) {
            return new ArrayList<>();
        }

        // 조건이 모두 비어 있지 않은 경우 검색
        List<ProductEntity> products = productRepository.findByKeywordCategoryLocation(keyword, category, location);

        // entities를 DTO로 변환
        return products.stream()
                .map(product -> ProductDTO.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .image(product.getImage())
                        .sellerName(product.getSellerName())
                        .location(product.getLocation())
                        .deadlineDate(product.getDeadlineDate())
                        .applicants(product.getApplicants())
                        .maxApplicants(product.getMaxApplicants())
                        .description(product.getDescription())
                        .activityType(product.getActivityType())
                        .categoryName(product.getCategory().getName())
                        .build())
                .collect(Collectors.toList());
    }

    //체험 디테일 수정
    @Transactional
    public void updateProduct(ProductDTO productDTO) {
        // Convert DTO to Entity if necessary
        Optional<ProductEntity> optionalProduct = productRepository.findById(productDTO.getId());
        if (optionalProduct.isPresent()) {
            ProductEntity product = optionalProduct.get();
            product.setProductName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setMaxApplicants(productDTO.getMaxApplicants());
            productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Product not found with id: " + productDTO.getId());
        }
    }

    //체험 삭제
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    //체험 관심목록
    @Transactional
    public void addFavorite(Long productId, String username) {
        MemberEntity member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (member.getFavorites().contains(product)) {
            throw new RuntimeException("Product already added to favorites");
        }

        member.getFavorites().add(product);
        memberRepository.save(member);
    }


    public Page<FavoriteDTO> getFavorites(String username, Pageable pageable) {
        MemberEntity member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        List<ProductEntity> favorites = member.getFavorites();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), favorites.size());

        List<FavoriteDTO> favoriteDTOs = favorites.subList(start, end).stream().map(product -> {
            FavoriteDTO dto = new FavoriteDTO();
            dto.setProductId(product.getId());
            dto.setProductName(product.getProductName());
            dto.setCategoryName(product.getCategory().getName());
            dto.setLocation(product.getLocation());
            dto.setImage(product.getImage());
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(favoriteDTOs, pageable, favorites.size());
    }


    // 관심 체험 삭제
    @Transactional
    public void deleteFavorite(Long productId, String username) {
        MemberEntity member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        member.getFavorites().remove(product);
        memberRepository.save(member);
    }

    // 관심 체험 신청
    @Transactional
    public ApplicationEntity apply(ApplicationDTO applicationDTO) {
        // DTO를 엔티티로 변환
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setMember(memberRepository.findById(applicationDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + applicationDTO.getMemberId())));

        ProductEntity productEntity = productRepository.findById(applicationDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + applicationDTO.getProductId()));


        applicationEntity.setProduct(productEntity);
        applicationEntity.setPhone(applicationDTO.getPhone());
        applicationEntity.setApplicationDate(LocalDateTime.now());
        applicationEntity.setStatus("접수");

        // 상품 테이블의 신청자 수 업데이트
        int applicants = productEntity.getApplicants();
        productEntity.setApplicants(applicants + 1); // 현재 신청자 수에 1 추가
        productRepository.save(productEntity);

        // 신청 정보 저장
        return applicationRepository.save(applicationEntity);
    }

    // 체험 신청 중복 확인
    public boolean checkIfAlreadyApplied(Long memberId, Long productId) {
        // 회원 ID와 상품 ID를 이용하여 이미 신청한 기록이 있는지 확인
        Optional<MemberEntity> member = memberRepository.findById(memberId);
        Optional<ProductEntity> product = productRepository.findById(productId);

        if (member.isPresent() && product.isPresent()) {
            Optional<ApplicationEntity> application = applicationRepository.findByProductAndMember(
                    product.get(),
                    member.get()
            );
            return application.isPresent();
        } else {
            return false; // 회원이나 상품이 존재하지 않는 경우 처리 방법을 설정할 수 있음
        }
    }

    // 신청자 관리
    public void updateStatus(Long id, String newStatus) {
        ApplicationEntity application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(newStatus);
        applicationRepository.save(application);
    }

    // 신청 삭제
    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }

    // 신청 현황
    public Page<ApplicationProductDTO> getMemberApplications(Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("applicationDate").descending());
        Page<ApplicationEntity> applicationPage = applicationRepository.findByMemberId(memberId, pageable);

        List<ApplicationProductDTO> dtoList = applicationPage.getContent().stream().map(application -> {
            ProductEntity product = application.getProduct();


            ApplicationProductDTO dto = new ApplicationProductDTO();
            dto.setId(application.getId());
            dto.setImage(product.getImage());
            dto.setProductName(product.getProductName());
            dto.setSellerName(product.getSellerName());
            dto.setLocation(product.getLocation());
            dto.setApplicationDate(application.getApplicationDate().toLocalDate());

            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, applicationPage.getTotalElements());
    }
}


