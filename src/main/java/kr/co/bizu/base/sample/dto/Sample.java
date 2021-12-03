package kr.co.bizu.base.sample.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

import org.apache.ibatis.type.Alias;

@Data
@ToString
@Alias("sample")
public class Sample {
    int seq;
    String sampleDesc;
    Date createdAt;
}
