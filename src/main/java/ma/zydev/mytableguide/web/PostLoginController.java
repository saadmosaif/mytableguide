package ma.zydev.mytableguide.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostLoginController {

    @GetMapping("/post-login")
    public String postLogin(Authentication auth) {
        if (auth == null) return "redirect:/login";
        boolean isAdmin = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN"));
        boolean isOwner = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_OWNER"));

        if (isAdmin) return "redirect:/admin";
        if (isOwner) return "redirect:/owner";
        return "redirect:/";
    }
}
