package kr.co.bizu.base.sample.mapper;

import kr.co.bizu.base.sample.dto.Sample;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SampleMapper {

    public Sample selectSample();
}
