//package edu.bbte.projectbluebook.datacatalog.versioning.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import edu.bbte.projectbluebook.datacatalog.versioning.model.TokenInfoResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Locale;
//
//@Component
//@PropertySource("classpath:application.properties")
//public class AuthFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        // Check authorization header
//        String authorization = httpServletRequest.getHeader("Authorization");
//        if (authorization == null || !authorization.startsWith("Bearer ")) {
//            sendResponse(403,"JWT Token is invalid", httpServletResponse);
//            return;
//        }
//
//        // Check token validity and retrieve relevant info
//        String authToken = authorization.substring(7);
//        TokenInfoResponse tokenInfoResponse = getTokenInfo(authToken);
//
//        if (!tokenInfoResponse.getActive()) {
//            sendResponse(403,"JWT Token is not active", httpServletResponse);
//            return;
//        }
//
//        // Check for endpoints
//
//        String method = httpServletRequest.getMethod().toUpperCase(new Locale("en", "us"));
//        TokenInfoResponse.RoleEnum role =  tokenInfoResponse.getRole();
//        // Delete asset - owner or admin
//        if ("DELETE".equals(method) && httpServletRequest.getRequestURI().startsWith("/assets")
//                && !httpServletRequest.getRequestURI().contains("/tags/")) {
//            if (role == TokenInfoResponse.RoleEnum.ADMIN) {
//                httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
//                httpServletRequest.setAttribute("role","admin");
//                chain.doFilter(httpServletRequest, httpServletResponse);
//            } else if (role == TokenInfoResponse.RoleEnum.USER) {
//                httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
//                httpServletRequest.setAttribute("role","user");
//                chain.doFilter(httpServletRequest, httpServletResponse);
//            } else {
//                sendResponse(401, "Unauthorized", httpServletResponse);
//            }
//            return;
//        }
//
//
//        // Add tag - owner or admin
//        if ("POST".equals(method)) {
//            if (role == TokenInfoResponse.RoleEnum.ADMIN && httpServletRequest.getRequestURI().equals("/assets")) {
//                httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
//                httpServletRequest.setAttribute("role","admin");
//                chain.doFilter(httpServletRequest, httpServletResponse);
//            } else if (role == TokenInfoResponse.RoleEnum.USER) {
//                httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
//                httpServletRequest.setAttribute("role","user");
//                chain.doFilter(httpServletRequest, httpServletResponse);
//            } else {
//                sendResponse(401, "Unauthorized", httpServletResponse);
//            }
//            return;
//        }
//
//        // Everything else
//        if (role == TokenInfoResponse.RoleEnum.ADMIN) {
//            httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
//            httpServletRequest.setAttribute("role","admin");
//            chain.doFilter(httpServletRequest, httpServletResponse);
//        } else if (role == TokenInfoResponse.RoleEnum.USER) {
//            httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
//            httpServletRequest.setAttribute("role", "user");
//            chain.doFilter(httpServletRequest, httpServletResponse);
//        }
//    }
//
//    private static TokenInfoResponse getTokenInfo(String token) throws IOException {
//
//        URL url = new URL("http://datacatalogregistryuserhandletest.azurewebsites.net/token_info");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-type", "application/json");
//        connection.setRequestProperty("Authorization", "Bearer " + token);
//        connection.setDoOutput(true);
//        connection.setDoInput(true);
//        OutputStream os = connection.getOutputStream();
//        os.write(token.getBytes());
//        os.flush();
//        os.close();
//        StringBuffer response = new StringBuffer();
//        int responseCode = connection.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String inputLine;
//
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//        }
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(response.toString(), TokenInfoResponse.class);
//    }
//
//    private void sendResponse(int statusCode, String message, HttpServletResponse httpServletResponse)
//            throws IOException {
//        httpServletResponse.setStatus(statusCode);
//        httpServletResponse.getWriter().write(message);
//    }
//}
//
