package com.ce.ems.requests;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.ce.ems.base.core.Application;

@WebServlet(loadOnStartup = 1, urlPatterns = { "/initservlet" })
public class ApplicationInitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		Application.start();
		super.init();
	}

	@Override 
	public void destroy() {
		Application.stop();
		super.destroy();
	}

	static {

	}
}
