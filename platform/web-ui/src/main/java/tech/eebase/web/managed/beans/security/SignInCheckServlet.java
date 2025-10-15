package tech.eebase.web.managed.beans.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SignInCheckServlet implements Servlet {

    private static final Logger LOG = LoggerFactory.getLogger(SignInCheckServlet.class);

    private static final Pattern DATA_PATTERN = Pattern.compile("([^=]*?)=([^\\&]*)\\&?");
    
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
        String referrer = request.getHeader("referer");
        int contentLength = request.getContentLength();
        
        if (contentLength < 5 || contentLength > 1000) {
            sendResponse(response, 400, "{\"msg\" : \"Wrong content length\"}");
        } else {
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
                        sendResponse(response, 200, "{\"msg\" : \"Signed in\"}");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        LOG.warn("Auth error with login '{}'", l);
                        sendResponse(response, 401, "{\"msg\" : \"Auth error\"}");
                    }
                } else {
                    sendResponse(response, 400, "{\"msg\" : \"No login data\"}");
                }
            } else {
                sendResponse(response, 400, "{\"msg\" : \"Empty body\"}");
            }
        }
        
        LOG.debug("referrer={}, method={}, content-length={}", referrer, request.getMethod(), contentLength);
    }

    private void sendResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        try (PrintWriter pw = new PrintWriter(response.getOutputStream())) {
            pw.print(message);
            pw.flush();
        }
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
