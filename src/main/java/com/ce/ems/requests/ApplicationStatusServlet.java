package com.ce.ems.requests;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.ce.ems.base.core.Application;

@WebServlet(loadOnStartup = 1, urlPatterns = { "/app/utils/status" })
public class ApplicationStatusServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
		Application.start();
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	static {

	}
}
