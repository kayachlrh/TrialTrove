package nana.TrialTrove;

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
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class ProductConcurrencyTest {
    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Test
    void 동시_신청_테스트() throws InterruptedException {

        Long productId = 1L;
        int threadCount = 50;

        // ⭐ 테스트 시작 전 상태 초기화
        ProductEntity product = productRepository.findById(productId).orElseThrow();
        product.setApplicants(4);   // 시작값
        productRepository.save(product);

        System.out.println("초기 applicants = " + product.getApplicants());

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // ⭐ 성공 / 실패 카운트
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {

            Long memberId = (long) (i + 1);

            executorService.submit(() -> {
                try {

                    ApplicationDTO dto = new ApplicationDTO();
                    dto.setMemberId(memberId);
                    dto.setProductId(productId);
                    dto.setPhone("010-0000-0000");

                    productService.apply(dto);

                    successCount.getAndIncrement();

                } catch (Exception e) {

                    failCount.incrementAndGet();
                    System.out.println("신청 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        ProductEntity result = productRepository.findById(productId).orElseThrow();

        System.out.println("총 요청 수 = " + threadCount);
        System.out.println("성공 수 = " + successCount.get());
        System.out.println("실패 수 = " + failCount.get());
        System.out.println("최종 applicants = " + result.getApplicants());
        System.out.println("정원 = " + result.getMaxApplicants());
    }
}
