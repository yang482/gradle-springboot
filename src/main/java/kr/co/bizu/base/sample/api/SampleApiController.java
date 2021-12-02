package kr.co.bizu.base.sample.api;

import kr.co.bizu.base.annotation.PathVariableWithBody;
import kr.co.bizu.base.sample.dto.Sample;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleApiController {
    
    @RequestMapping("/{id}")
    public Sample getSample(@PathVariableWithBody Sample sample) {
        System.out.println(sample);
        //System.out.println(bodySample);
        return sample;
    }
}
