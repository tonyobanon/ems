package com.ce.ems.requests;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.ce.ems.base.core.Application;
import com.ce.ems.base.core.ClassIdentityType;
import com.ce.ems.base.core.ClasspathScanner;
import com.ce.ems.base.core.Exceptions;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;

@WebServlet(urlPatterns = "/baseservlet", loadOnStartup = 5)
public class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		try {
			new ClasspathScanner<>("Entity", Entity.class, ClassIdentityType.ANNOTATION).scanClasses().forEach(e -> {
				ObjectifyService.register(e);
			});
			Application.start();
		} catch (Exception e) {
			Exceptions.throwRuntime(e);
		}
	}

	@Override
	public void destroy() {
		Application.stop();
		super.destroy();
	}

}
