package ma.zydev.mytableguide.web;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    @GetMapping
    public String index(Model model) {
        model.addAttribute("restaurantsCount",0);
        model.addAttribute("restaurantsCount",0);



        return "admin/index";
    }
}
