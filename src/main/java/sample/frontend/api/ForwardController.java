package sample.frontend.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ForwardController {

    @RequestMapping({"/balances", "/authentication", "/authentication/**",
            "/consent", "/transactions/**"})
    public ModelAndView forward() {
        return new ModelAndView("forward:index.html");
    }

}
