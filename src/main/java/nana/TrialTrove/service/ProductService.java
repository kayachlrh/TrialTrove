package nana.TrialTrove.service;

import nana.TrialTrove.domain.*;
import nana.TrialTrove.repository.CategoryRepository;
import nana.TrialTrove.repository.MemberRepository;
import nana.TrialTrove.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, MemberRepository memberRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
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

        return productPage.map(product -> new ProductDTO(
                product.getId(),
                product.getProductName(),
                product.getImage(),
                product.getSellerName(),
                product.getLocation(),
                product.getDeadlineDate(),
                product.getMaxApplicants(),
                product.getDescription(),
                product.getActivityType(),
                product.getCategory().getName()
        ));
    }


    // 체험 디테일
    public ProductDTO getProductById(Long id) {
        Optional<ProductEntity> productEntityOpt = productRepository.findById(id);
        if (productEntityOpt.isPresent()) {
            ProductEntity productEntity = productEntityOpt.get();
            return new ProductDTO(
                    productEntity.getId(),
                    productEntity.getProductName(),
                    productEntity.getImage(),
                    productEntity.getSellerName(),
                    productEntity.getLocation(),
                    productEntity.getDeadlineDate(),
                    productEntity.getMaxApplicants(),
                    productEntity.getDescription(),
                    productEntity.getActivityType(),
                    productEntity.getCategory().getName()
            );
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
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getProductName(),
                        product.getImage(),
                        product.getSellerName(),
                        product.getLocation(),
                        product.getDeadlineDate(),
                        product.getMaxApplicants(),
                        product.getDescription(),
                        product.getActivityType(),
                        product.getCategory().getName()
                ))
                .collect(Collectors.toList());
    }

}
