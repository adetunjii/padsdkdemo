package pad_java_sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CustomFilter implements Filter {

    public CustomFilter() {

    }

    private static final Logger logger = Logger.getLogger("CustomFilter.class");
    private final ObjectMapper mapper = new ObjectMapper();

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest)request;

        String clientId = httpServletRequest.getHeader("client-id");
        String clientSecret = httpServletRequest.getHeader("client_secret");
        String token = httpServletRequest.getHeader("Authorization");

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Map<String, Object> errorDetails = new HashMap<>();

        if((clientId == null || clientId.isEmpty()) || (clientSecret == null || clientSecret.isEmpty() || (token == null || token.isEmpty()))) {

            errorDetails.put("error", "Incomplete credentials");

        }else{

            token = token.replace("Bearer ", "");

            System.setProperty("clientId",clientId );
            System.setProperty("clientSecret", clientSecret);
            System.setProperty("token", token);

            if(UserService.isUserValid()){

                chain.doFilter(httpServletRequest, httpServletResponse);
                return;

            }else{

                errorDetails.put("error", "Invalid credentials");

            }

        }

        httpServletResponse.setStatus(HttpStatus.SC_FORBIDDEN);
        httpServletResponse.setContentType("application/json");

        mapper.writeValue(httpServletResponse.getWriter(), errorDetails);

        httpServletResponse.flushBuffer();

    }

    public void init(FilterConfig config) throws ServletException {

    }

    public void destroy() {
        System.clearProperty("clientId");
        System.clearProperty("clientSecret");
        System.clearProperty("token");
    }

}