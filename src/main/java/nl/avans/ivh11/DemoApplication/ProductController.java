package nl.avans.ivh11.DemoApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
// @RequestMapping(value = "/")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private ArrayList<Product> products = new ArrayList<>();

    // Views constants
    private final String VIEW_LIST_PRODUCTS = "views/product/list";

    @Autowired
    private final ProductRepository productRepository;

    // Constructor with Dependency Injection
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping(value = "/product")
    public String listProducts(
            @RequestParam(value="category", required=false, defaultValue="all") String category,
            @RequestParam(value="size", required=false, defaultValue="10") String size,
            Model model) {

        logger.debug("listProducts called.");
        Iterable<Product> products = productRepository.findAll();

        model.addAttribute("category", category);
        model.addAttribute("size", size);
        model.addAttribute("products", products);
        return VIEW_LIST_PRODUCTS;
    }

    @RequestMapping(value="/product/new", method = RequestMethod.GET)
    public String showCreateProductForm(final Product product, final ModelMap model) {
        logger.debug("showCreateProductForm");
        return "views/product/create";
    }

    @RequestMapping(value="/product/new", method = RequestMethod.POST)
    public String validateAndSaveProduct(
            @Valid Product product,
            final BindingResult bindingResult,
            final ModelMap model) {
        logger.debug("validateAndSaveProduct - adding product " + product.getName());
        if (bindingResult.hasErrors()) {
            logger.debug("validateAndSaveProduct - not added, bindingResult.hasErrors");
            return "views/member/create";
        }

        product = this.productRepository.save(product);
        products.add(product);

        model.addAttribute("info", "Product was added.");
        model.addAttribute("products", products);
        return VIEW_LIST_PRODUCTS;
    }

}
