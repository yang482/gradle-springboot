package kr.co.bizu.base.sample.web;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/sample")
public class SampleWebController {
    
    @GetMapping({"", "/"})
    public String sampleMain(Model model) {
        
        model.addAttribute("var1", "Variable for script from Controller");
        model.addAttribute("var2", "Variable for html from Controller");
        model.addAttribute("var3", "<b>Variable with html tag</b>");
        
        return "sample/sampleMain";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(Model model) {
        
        
        model.addAttribute("isLogin", "Login complete");
        
        return "login";
    }
}
