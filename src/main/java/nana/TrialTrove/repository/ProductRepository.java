package nana.TrialTrove.repository;

import nana.TrialTrove.domain.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    // 카테고리 이름을 포함하는 검색
    Page<ProductEntity> findByCategory_Name(String categoryName, Pageable pageable);

    // 제품 이름을 포함하는 검색
    Page<ProductEntity> findByProductName(String productName, Pageable pageable);

    // 모든 제품을 페이징하여 반환
    Page<ProductEntity> findAll(Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE " +
            "(:keyword IS NULL OR p.productName LIKE %:keyword%) AND " +
            "(:category IS NULL OR p.category.name = :category) AND " +
            "(:location IS NULL OR p.location = :location)")
    List<ProductEntity> findByKeywordCategoryLocation(@Param("keyword") String keyword,
                                                      @Param("category") String category,
                                                      @Param("location") String location);

    @Query("SELECT p FROM ProductEntity p JOIN ApplicationEntity a ON p.id = a.product.id WHERE a.member.id = :memberId")
    List<ProductEntity> findByMemberId(@Param("memberId") Long memberId);
}
