package kr.co.bizu.base.config;

import kr.co.bizu.base.annotation.PathVariableWithBody;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PathVariableWithBodyArgumentResolver extends AbstractMessageConverterMethodArgumentResolver implements HandlerMethodArgumentResolver {
    
    ObjectMapper objectMapper;
    
    public PathVariableWithBodyArgumentResolver(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }
    
    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PathVariableWithBody.class);
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    
        parameter = parameter.nestedIfOptional();
        Object arg = readWithMessageConverters(webRequest, parameter, Map.class);
    
        LinkedHashMap params = new LinkedHashMap();
        params.putAll((Map)arg);
        params.putAll(getUriTemplateParams(webRequest));
        
        return objectMapper.convertValue(params, parameter.getParameterType());
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Map getUriTemplateParams(NativeWebRequest webRequest) {
    
        Map<String, String> uriTemplateMap = (Map<String, String>) webRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
                
        if (!CollectionUtils.isEmpty(uriTemplateMap)) {
            return uriTemplateMap;
        }
        else {
            return Collections.emptyMap();
        }
    }
}
