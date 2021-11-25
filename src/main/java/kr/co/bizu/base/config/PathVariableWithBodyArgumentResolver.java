package kr.co.bizu.base.config;

import kr.co.bizu.base.annotation.PathVariableWithBody;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PathVariableWithBodyArgumentResolver implements HandlerMethodArgumentResolver {
    
    ObjectMapper objectMapper;
    
    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PathVariableWithBody.class);
    }
    
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    
        Map<String, String> params = getBodyParams(webRequest);
    
        params.putAll(getUriTemplateParams(webRequest));
        
        return objectMapper.convertValue(params, parameter.getParameterType());
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, String> getUriTemplateParams(NativeWebRequest webRequest) {
    
        Map<String, String> uriTemplateMap = (Map<String, String>) webRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
                
        if (!CollectionUtils.isEmpty(uriTemplateMap)) {
            return new LinkedHashMap<>(uriTemplateMap);
        }
        else {
            return Collections.emptyMap();
        }
    }
    
    private Map<String, String> getBodyParams(NativeWebRequest webRequest) throws Exception{
    
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
    
        if( servletRequest != null ) {
    
            ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);
            //String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    
            InputStream bodyStream = inputMessage.getBody();
            String body = new String(inputMessage.getBody().readAllBytes());
            
            if(StringUtils.hasText(body)) {
                return objectMapper.readValue(body, new TypeReference<Map<String, String>>() {});
            }
        }
        
        return Collections.emptyMap();
    }
}
