package nana.TrialTrove.repository;

import nana.TrialTrove.domain.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Page<ProductEntity> findByCategory_Name(String categoryName, Pageable pageable);
    Page<ProductEntity> findByProductName(String productName, Pageable pageable);
}
