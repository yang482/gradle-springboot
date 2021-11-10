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
    
    
    @GetMapping("/login")
    public String loginPage() {
        return "page/login";
    }
    
    @PostMapping("/login")
    public String login(Model model) {
        
        
        model.addAttribute("isLogin", "로그인 컨트롤러 수행 완료");
        
        return "page/login";
    }
}
