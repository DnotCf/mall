package com.mmall.filter;

import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionExpireFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpServletResponse response1 = (HttpServletResponse) response;
        String token = CookieUtil.getCookie(request1);
        if (StringUtils.isNotBlank(token)) {
            User user = JsonUtil.string2obj(RedisClientUtil.get(token), new TypeReference<User>() {
            });
            if (user != null) {
                RedisClientUtil.setExpir(token, 60 * 60 * 24);

            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
