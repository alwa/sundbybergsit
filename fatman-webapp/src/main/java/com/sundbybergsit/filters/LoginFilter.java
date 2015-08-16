package com.sundbybergsit.filters;

import com.sundbybergsit.fatman.jsf.FatmanLoginBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Z
 * Date: 2013-03-22
 * Time: 19:12
 * To change this template use File | Settings | File Templates.
 */
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        FatmanLoginBean loginBean = (FatmanLoginBean) ((HttpServletRequest) request).getSession().getAttribute("loginBean");
        if (loginBean != null && loginBean.isLoggedIn()) {
            // User is logged in, redirect to desired page.
            chain.doFilter(request, response);
        } else if (loginBean != null && !loginBean.isLoggedIn()) {

            // Get the existing session.
            HttpSession session = ((HttpServletRequest) request).getSession(false);

            // Invalidate the existing session.

            // Note :
            // We may need to invalidate the existing session to ensure that all previous session data(s) for the user is removed from the context.

            // Example : When user login to the application after Session Times out,we may not
            // need his previous session data and we need to create a new session for the user.

            if (session != null) {
                session.invalidate();
            }
        } else {
            String requestURI = ((HttpServletRequest) request).getRequestURI();
            if (requestURI.contains("start") ||
                    requestURI.contains("createdata") ||
                    requestURI.contains("historik") ||
                    requestURI.contains("settings")) {
                // No logged-in user found, so redirect to login page.
                ((HttpServletResponse) response).sendRedirect("index.jsp");
            } else {
                // OK to go to login page even when not logged in
                chain.doFilter(request, response);

            }
        }
    }

    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
