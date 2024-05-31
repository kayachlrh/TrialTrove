package nana.TrialTrove.service;

import nana.TrialTrove.domain.*;
import nana.TrialTrove.repository.CategoryRepository;
import nana.TrialTrove.repository.MemberRepository;
import nana.TrialTrove.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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


}
