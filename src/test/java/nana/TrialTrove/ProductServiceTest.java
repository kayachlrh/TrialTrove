package nana.TrialTrove;

import jakarta.transaction.Transactional;
import nana.TrialTrove.domain.ApplicationDTO;
import nana.TrialTrove.domain.ProductEntity;
import nana.TrialTrove.repository.ProductRepository;
import nana.TrialTrove.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Transactional
public class ProductServiceTest {
    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Test
    void searchPerformanceTest(){
        String keyword = "상품 100";

        // 워밍업
        for(int i = 0; i < 10; i++){
            productService.searchProducts(keyword, null, null);
        }

        // 성능 측정
        int repeat = 100;
        long totalTime = 0;

        for(int i = 0; i < repeat; i++){
            long start = System.currentTimeMillis();
            productService.searchProducts(keyword, null, null);
            long end = System.currentTimeMillis();
            totalTime += (end -start);
        }

        long avgTimeMs = totalTime / repeat;

        // 결과
        System.out.println("=================================");
        System.out.println("검색 키워드 : " + keyword);
        System.out.println("반복 횟수 : " + repeat);
        System.out.println("평균 검색 시간(ms) : " + avgTimeMs);
        System.out.println("=================================");
    }

}
