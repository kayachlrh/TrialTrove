package nana.TrialTrove.controller;

import nana.TrialTrove.domain.CategoryDTO;
import nana.TrialTrove.domain.ProductDTO;
import nana.TrialTrove.domain.ProductEntity;
import nana.TrialTrove.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }
    @RequestMapping("/")
    public String main(Model model) {
        model.addAttribute("activePage", "home");
        List<ProductDTO> productDTOs = productService.getProducts(0, 6, null, null).getContent();
        model.addAttribute("products", productDTOs);
        return "main/index";
    }

}

