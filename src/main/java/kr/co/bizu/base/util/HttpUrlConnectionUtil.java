package kr.co.bizu.base.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class HttpUrlConnectionUtil {
    
    public static final String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36";
    
    public static final String ENCODING = "UTF-8"; // 5초
    public static final int CONNECTION_TIMEOUT = 5000; // 5초
    public static final int READ_TIMEOUT = 5000; // 5초
    
    private static void init() {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }
    
    public static URL getUrl(String url){
        try{
            return new URL(url);
        } catch (MalformedURLException me) {
            me.printStackTrace();
        }
        return null;
    }
    
    @SuppressWarnings("rawtypes")
    private static String getMapValue(String name, Map option, boolean ignoreCase) {
        
        if( option == null || name == null || name.replaceAll("\\s", "").isEmpty()) {
            return "";
        }
        
        option.remove(null);
        if( ignoreCase ) {
            for( Object key : option.keySet() ) {
                if( name.equalsIgnoreCase(String.valueOf(key)) ) {
                    try {
                        // 값이 null 일지도 모르므로 try catch 처리
                        return String.valueOf(option.get(key));
                    } catch (Exception ignore) {}
                }
            }
        } else {
            try {
                // 값이 null 일지도 모르므로 try catch 처리
                return String.valueOf(option.get(name));
            } catch (Exception ignore) {}
        }
        
        
        return "";
    }
    
    public static String encodeUrl(String source) {
        return encodeUrl(source, ENCODING);
    }
    public static String encodeUrl(String source, String encoding) {
        try {
            return URLEncoder.encode(source, encoding);
        } catch (Exception e) {
            return source;
        }
    }
    /**
     * GET 방식 호출
     *
     * @param url 프로토콜을 포함한 호출 URL ex ) http://www.naver.com
     * @param header HTTP 헤더 설정
     * @param parameter 호출 시 파라미터
     * @param option 기타 커넥션 등에 대한 옵션 key : connectionTimeout, readTimeout
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String get(String url, Map header, String parameter, Map option) {
        if( url == null ) {
            return "";
        }
        
        init();
        
        URL callableUrl;
        URLConnection connection;
        BufferedReader br = null;
        
        StringBuilder responseBuilder = new StringBuilder();
        
        try {
            // 파라미터 설정
            if( parameter != null && !parameter.isEmpty() ) {
                // 혹시 KEY 가 null로 들어오는경우 제거
                StringBuilder urlBuilder = new StringBuilder();
                
                urlBuilder.append(url);
                if( url.contains("?") ) {
                    urlBuilder.append("&");
                } else {
                    urlBuilder.append("?");
                }
                
                urlBuilder.append(parameter);
                
                callableUrl = getUrl(urlBuilder.toString());
            } else {
                callableUrl = getUrl(url);
            }
            
            if( callableUrl == null ) {
                return "";
            }
            
            String protocol = callableUrl.getProtocol();
            if("http".equalsIgnoreCase(protocol) ) {
                HttpURLConnection temp = (HttpURLConnection) callableUrl.openConnection();
                temp.setRequestMethod("GET");
                connection = temp;
            } else if ("https".equalsIgnoreCase(protocol) ) {
                //SSL 인증서 무시 처리
                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session){
                        return true;
                    }
                };
                
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
                
                HttpsURLConnection temp = (HttpsURLConnection) callableUrl.openConnection();
                temp.setRequestMethod("GET");
                
                connection = temp;
            } else {
                return "";
            }
            
            // 헤더에 User-Agent 를 크롬으로 설정함.
            connection.setRequestProperty("User-Agent", USER_AGENT_CHROME);
            
            // 커넥션 타임 아웃 설정, 옵션 설정 실패 시 기본값 사용
            try {
                connection.setConnectTimeout(Integer.valueOf(getMapValue("connectionTimeout", option, true)));
            } catch (Exception e) {
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
            }
            
            // 리드 타임 아웃 설정, 옵션 설정 실패 시 기본값 사용
            try {
                connection.setReadTimeout(Integer.valueOf(getMapValue("readTimeout", option, true)));
            } catch (Exception e) {
                connection.setReadTimeout(READ_TIMEOUT);
            }
            
            // 기본값은 false GET 방식에서는 false 로 설정하여 사용
            connection.setDoOutput(false);
            
            //con.getResponseCode() 또는 connection.getInputStream() 시에 실제 URL 호출이 일어남
            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), ENCODING));
            
            String line;
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line).append(System.getProperty("line.separator"));
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if( br != null ) {
                    br.close();
                }
            } catch (IOException ignore) {}
        }
        
        return responseBuilder.toString();
    }
    
    /**
     * POST 방식 호출
     *
     * @param url 프로토콜을 포함한 호출 URL ex ) http://www.naver.com
     * @param header HTTP 헤더 설정
     * @param parameter 호출 시 파라미터
     * @param option 기타 커넥션 등에 대한 옵션 key : connectionTimeout, readTimeout
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String post(String url, Map header, String parameter, Map option) {
        
        if( url == null ) {
            return "";
        }
        
        init();
        
        URL callableUrl;
        URLConnection connection;
        BufferedReader br = null;
        OutputStreamWriter osw = null;
        
        StringBuilder responseBuilder = new StringBuilder();
        
        try {
            callableUrl = getUrl(url);
            
            if( callableUrl == null ) {
                return "";
            }
            
            String protocol = callableUrl.getProtocol();
            if("http".equalsIgnoreCase(protocol) ) {
                HttpURLConnection temp = (HttpURLConnection) callableUrl.openConnection();
                temp.setRequestMethod("POST");
                connection = temp;
            } else if ("https".equalsIgnoreCase(protocol) ) {
                
                //SSL 인증서 무시 처리
                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session){
                        return true;
                    }
                };
                
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
                
                HttpsURLConnection temp = (HttpsURLConnection) callableUrl.openConnection();
                temp.setRequestMethod("POST");
                connection = temp;
            } else {
                return "";
            }
            
            // 헤더에 User-Agent 를 크롬으로 설정함.
            connection.setRequestProperty("User-Agent", USER_AGENT_CHROME);
            
            // 커넥션 타임 아웃 설정, 옵션 설정 실패 시 기본값 사용
            try {
                connection.setConnectTimeout(Integer.valueOf(getMapValue("connectionTimeout", option, true)));
            } catch (Exception e) {
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
            }
            
            // 리드 타임 아웃 설정, 옵션 설정 실패 시 기본값 사용
            try {
                connection.setReadTimeout(Integer.valueOf(getMapValue("readTimeout", option, true)));
            } catch (Exception e) {
                connection.setReadTimeout(READ_TIMEOUT);
            }
            
            // POST 시 커넥션 설정
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            
            String contentType = getMapValue("Content-Type", header, true);
            if( contentType.isEmpty() ) {
                //x-www-form-urlencoded 일때는 파라미터 스트링을 get 방식 처럼 사용 할 수 있다.
                contentType = "application/x-www-form-urlencoded";
            }
    
            connection.setRequestProperty("Content-Type", contentType);
            
            if( parameter != null && !parameter.isEmpty() ) {
                osw = new OutputStreamWriter(connection.getOutputStream(), ENCODING);
                osw.write(parameter);
                osw.flush();
            }
            
            //con.getResponseCode() 또는 connection.getInputStream() 시에 실제 URL 호출이 일어남
            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), ENCODING));
            
            String line;
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line).append(System.getProperty("line.separator"));
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if( br != null ) {
                    br.close();
                }
            } catch (IOException ignore) {}
            try {
                if( osw != null ) {
                    osw.close();
                }
            } catch (IOException ignore) {}
        }
        
        return responseBuilder.toString();
    }
}