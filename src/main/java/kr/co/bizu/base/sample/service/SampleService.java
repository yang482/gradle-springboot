package kr.co.bizu.base.sample.service;

import kr.co.bizu.base.sample.dto.Sample;
import kr.co.bizu.base.sample.mapper.SampleMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sample.SampleService")
public class SampleService {
    
    @Autowired
    SampleMapper sampleMapper;
    
    public Sample selectSample() {
        return sampleMapper.selectSample();
    }
}
