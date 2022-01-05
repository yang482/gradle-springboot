package kr.co.bizu.base.util;

import kr.co.bizu.base.util.HttpUrlConnectionUtil;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class HttpUrlConnectionUtilTest {

    @Test
    void getTest() {
    
        String parameter = "param1=bbbb&param2=" + HttpUrlConnectionUtil.encodeUrl("양승현");
        String responseStr = HttpUrlConnectionUtil.get("http://localhost:8080/test", null, parameter, null);
    
        System.out.println(responseStr);
    }
    
    @Test
    void postTest() {
        
        String parameter = "param1=bbbb&param2=양승현";
        String responseStr = HttpUrlConnectionUtil.post("http://localhost:8080/test", null, parameter, null);
        
        System.out.println(responseStr);
    }
    
    @Test
    void postJsonTest() {
    
        /*
        JSON 으로 보낼때는 파라미터는 JSON 문자열이어야 하며
        헤더의 Content-Type 은 application/json 으로 지정 해주어야 함
        */
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");
        
        String parameter = "{\"param1\":\"bbbb\", \"param2\":\"양승현json\"}";
        String responseStr = HttpUrlConnectionUtil.post("http://localhost:8080/test", headerMap, parameter, null);
        
        System.out.println(responseStr);
    }
    
    @Test
    void postXmlTest() {
        
        /*
        JSON 으로 보낼때는 파라미터는 JSON 문자열이어야 하며
        헤더의 Content-Type 은 application/xml 으로 지정 해주어야 함
        */
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/xml");
        
        String parameter = "<params><param1>bbbb</param1></params>";
        String responseStr = HttpUrlConnectionUtil.post("http://localhost:8080/test", headerMap, parameter, null);
        
        System.out.println(responseStr);
    }
}