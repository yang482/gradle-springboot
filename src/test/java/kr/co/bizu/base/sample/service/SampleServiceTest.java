package kr.co.bizu.base.sample.service;

import kr.co.bizu.base.sample.mapper.SampleMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SampleServiceTest {
    
    @Autowired
    SampleMapper sampleMapper;
    
    @Test
    void selectSample() {
        System.out.println(sampleMapper.selectSample());
    }
}