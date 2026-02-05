package nana.TrialTrove;

import nana.TrialTrove.domain.CategoryEntity;
import nana.TrialTrove.domain.ProductEntity;
import nana.TrialTrove.repository.CategoryRepository;
import nana.TrialTrove.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableJpaAuditing
public class TrialTroveApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrialTroveApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(ProductRepository productRepository, CategoryRepository categoryRepository) {
		return args -> {
			CategoryEntity defaultCategory = categoryRepository.findById(1L).orElseThrow();

			for (int i = 1; i <= 500; i++) {
				ProductEntity product = new ProductEntity();
				product.setProductName("상품 " + i);
				product.setCategory(defaultCategory);
				product.setLocation("서울");
				product.setSellerName("판매자 " + i);
				productRepository.save(product);
			}

			System.out.println("더미 데이터 500건 삽입 완료!");
		};
	}
}
