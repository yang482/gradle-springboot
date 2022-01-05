package kr.co.bizu.base.sample.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {
    
    @RequestMapping("/test")
    @ResponseBody
    public String test(@RequestParam MultiValueMap<String, Object> multiValueMap) {
    
        System.out.println(multiValueMap);
        
        return "OK";
    }
    
    @RequestMapping(value = "/test", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String testJson(@RequestBody String jsonParam) {
        
        System.out.println(jsonParam);
        
        return "OK";
    }
    
    @RequestMapping(value = "/test", consumes = {MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public String testXml(@RequestBody String xmlParam) {
        
        System.out.println(xmlParam);
        
        return "OK";
    }
    
}
