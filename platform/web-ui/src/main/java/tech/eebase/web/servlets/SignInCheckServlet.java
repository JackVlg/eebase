package tech.eebase.web.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.eebase.web.model.SignInResponseBody;

public class SignInCheckServlet implements Servlet {

    private static final Logger LOG = LoggerFactory.getLogger(SignInCheckServlet.class);

    private static final Pattern DATA_PATTERN = Pattern.compile("([^=]*?)=([^\\&]*)\\&?");
    
    private static final ObjectMapper mapper = new ObjectMapper(); 
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        LOG.debug("Inited");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        
        if (!"POST".equals(request.getMethod())) {
            sendResponse(response, 400, new SignInResponseBody("Wrong method"));
            return;
        }
        
        int contentLength = request.getContentLength();
        if (contentLength < 5 || contentLength > 1000) {
            sendResponse(response, 400, new SignInResponseBody("Wrong content length"));
            return;
        }
        
        String l = null;
        String p = null;
        
        String body = readFirstLine(request);
        if (body != null) {
            Matcher matcher = DATA_PATTERN.matcher(body);
            while (matcher.find()) {
                String key = matcher.group(1);
                String value = matcher.group(2);
                
                if ("l".equals(key)) l = value;
                if ("p".equals(key)) p = value;
            }
            
            if (l != null && p != null) {
                try {
                    LOG.debug("Trying to auth with login {}", l);
                    Thread.sleep(1000);
                    request.login(l, p);
                    sendResponse(response, 200, new SignInResponseBody("Signed in"));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    sendResponse(response, 401, new SignInResponseBody("Auth error"));
                }
            } else {
                sendResponse(response, 400, new SignInResponseBody("No login data"));
            }
        } else {
            sendResponse(response, 400, new SignInResponseBody("Empty body"));
        }
    }

    private void sendResponse(HttpServletResponse response, int statusCode, SignInResponseBody responseBody) throws IOException {
        response.setStatus(statusCode);
        mapper.writeValue(response.getOutputStream(), responseBody);
        response.getOutputStream().flush();
    }
    
    private String readFirstLine(HttpServletRequest request) throws IOException {
        try (var isr = new InputStreamReader(request.getInputStream());
             var br = new BufferedReader(isr)) {
            return br.readLine();
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        // Nothing to do
    }

}
