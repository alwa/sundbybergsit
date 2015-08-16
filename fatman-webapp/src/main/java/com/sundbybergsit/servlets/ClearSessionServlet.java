package com.sundbybergsit.servlets;

import com.sundbybergsit.authentication.Authentication;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "clearsession", urlPatterns = "/clearsession")
public class ClearSessionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession session = req.getSession();
		session.removeAttribute("accessToken");
		session.removeAttribute("refreshToken");

        Authentication loginBean = (Authentication) req.getSession().getAttribute("loginBean");
        loginBean.logout();

		// With the authToken cleared out, return to the index
		resp.sendRedirect("/");
	}
}