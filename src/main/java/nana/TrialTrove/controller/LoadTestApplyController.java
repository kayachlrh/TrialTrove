package nana.TrialTrove.controller;

import lombok.RequiredArgsConstructor;
import nana.TrialTrove.domain.ApplicationDTO;
import nana.TrialTrove.exception.CapacityExceededException;
import nana.TrialTrove.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/load-test")
@RequiredArgsConstructor
public class LoadTestApplyController {

    private final ProductService productService;

    @PostMapping("/apply/{productId}")
    public ResponseEntity<String> apply(
            @PathVariable Long productId,
            @RequestBody ApplicationDTO dto
        ) {
            try {
                dto.setProductId(productId);
                productService.apply(dto);
                return ResponseEntity.ok().build();
            } catch (CapacityExceededException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("CAPACITY_EXCEEDED");
            }
    }
}
