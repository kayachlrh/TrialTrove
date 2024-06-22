package nana.TrialTrove.controller;


import nana.TrialTrove.domain.AdminDashboardDTO;
import nana.TrialTrove.domain.ApplicationDTO;
import nana.TrialTrove.domain.ProductDTO;
import nana.TrialTrove.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;

    @Autowired
    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    // 관리자 대시보드
    @GetMapping("/dashboard")
    public String showAdminDashboard(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size,
                                     Model model) {

        Page<AdminDashboardDTO> dashboardPage = productService.getAllApplicationsWithProductInfo(page, size);

        // 모델에 추가
        model.addAttribute("dashboardPage", dashboardPage);

        return "admin/dashboard"; // 관리자 대시보드 뷰
    }

    // 신청자 관리
    @PostMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, String> payload) {
        Long id = Long.valueOf(payload.get("id"));
        String newStatus = payload.get("status");

        productService.updateStatus(id, newStatus);

        return ResponseEntity.ok("Status updated successfully");
    }

    // 신청 삭제
    @PostMapping("/deleteApplication")
    public ResponseEntity<String> deleteApplication(@RequestBody Map<String, Long> payload) {
        Long id = payload.get("id");

        productService.deleteApplication(id);

        return ResponseEntity.ok("Application deleted successfully");
    }
}
