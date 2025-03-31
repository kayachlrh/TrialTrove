package nana.TrialTrove.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import nana.TrialTrove.domain.ProductEntity;
import nana.TrialTrove.domain.QProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;




import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, QuerydslPredicateExecutor<ProductEntity> {

    // 카테고리 이름을 포함하는 검색
    Page<ProductEntity> findByCategory_Name(String categoryName, Pageable pageable);

    // 제품 이름을 포함하는 검색
    Page<ProductEntity> findByProductName(String productName, Pageable pageable);

    // 모든 제품을 페이징하여 반환
    Page<ProductEntity> findAll(Pageable pageable);

    default List<ProductEntity> searchByConditions(String keyword, String category, String location, JPAQueryFactory queryFactory) {
        QProductEntity product = QProductEntity.productEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.and(product.productName.containsIgnoreCase(keyword));
        }
        if (category != null && !category.isEmpty()) {
            builder.and(product.category.name.eq(category));
        }
        if (location != null && !location.isEmpty()) {
            builder.and(product.location.eq(location));
        }

        return queryFactory
                .selectFrom(product)
                .where(builder)
                .fetch();
    }


    @Query("SELECT p FROM ProductEntity p JOIN ApplicationEntity a ON p.id = a.product.id WHERE a.member.id = :memberId")
    List<ProductEntity> findByMemberId(@Param("memberId") Long memberId);
}
