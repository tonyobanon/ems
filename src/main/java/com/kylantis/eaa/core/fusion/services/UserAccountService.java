package com.kylantis.eaa.core.fusion.services;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import com.ce.ems.base.core.SystemValidationException;
import com.ce.ems.models.BaseUserModel;
import com.ce.ems.utils.Utils;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.fusion.FusionHelper;
import com.kylantis.eaa.core.fusion.Sessions;
import com.kylantis.eaa.core.fusion.WebRoutes;
import com.kylantis.eaa.core.keys.CacheValues;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;

@EndpointClass(uri = "/users/accounts")
public class UserAccountService extends BaseService {

	@EndpointMethod(uri = "/phoneAuth", headerParams = { "phone", "pass", "rem" }, requestParams = { "returnUrl" },
			functionality = Functionality.EMAIL_LOGIN_USER)
	public void loginByPhone(RoutingContext ctx) {

		Long phone = Long.parseLong(ctx.request().getHeader("phone"));
		String pass = ctx.request().getHeader("pass");
		String rem = ctx.request().getHeader("rem");

		String returnUrl = ctx.request().getParam("returnUrl");

		try {
			Long userId = BaseUserModel.loginByPhone(phone, pass);
			String sessionToken = Utils.newRandom();

			loginUser(userId, sessionToken, ctx.request().remoteAddress().host(), rem.equals("true") ? CacheValues.SESSION_TOKEN_LONG_EXPIRY_IN_SECS
					: CacheValues.SESSION_TOKEN_SHORT_EXPIRY_IN_SECS);

			if (returnUrl.equals("null")) {
				returnUrl = WebRoutes.DEFAULT_CONSOLE_URI;
			}

			Cookie cookie = Cookie.cookie(FusionHelper.sessionTokenName(), sessionToken).setPath("/");
			if (rem.equals("true")) {
				cookie.setMaxAge(CacheValues.SESSION_TOKEN_LONG_EXPIRY_IN_SECS);
			}

			ctx.addCookie(cookie);

			ctx.response().setStatusCode(HttpServletResponse.SC_FOUND);
			ctx.response().putHeader("X-Location", returnUrl);
			
		} catch (SystemValidationException e) {
			ctx.response().setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
		}
		ctx.response().end();
	}

	@EndpointMethod(uri = "/emailAuth", headerParams = { "email", "pass", "rem" }, requestParams = { "returnUrl" },
			functionality = Functionality.PHONE_LOGIN_USER)
	public void loginByEmail(RoutingContext ctx) {

		String email = ctx.request().getHeader("email");
		String pass = ctx.request().getHeader("pass");
		String rem = ctx.request().getHeader("rem");

		String returnUrl = ctx.request().getParam("returnUrl");
		try {
			Long userId = BaseUserModel.loginByEmail(email, pass);
			String sessionToken = Utils.newRandom();

			loginUser(userId, sessionToken, ctx.request().remoteAddress().host(), rem.equals("true") ? CacheValues.SESSION_TOKEN_LONG_EXPIRY_IN_SECS
					: CacheValues.SESSION_TOKEN_SHORT_EXPIRY_IN_SECS);

			if (returnUrl.equals("null")) {
				returnUrl = WebRoutes.DEFAULT_CONSOLE_URI;
			}

			Cookie cookie = Cookie.cookie(FusionHelper.sessionTokenName(), sessionToken).setPath("/");
			if (rem.equals("true")) {
				cookie.setMaxAge(CacheValues.SESSION_TOKEN_LONG_EXPIRY_IN_SECS);
			}

			ctx.addCookie(cookie);

			ctx.response().setStatusCode(HttpServletResponse.SC_FOUND);
			ctx.response().putHeader("X-Location", returnUrl);
			
		} catch (SystemValidationException e) {
			ctx.response().setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
		}
		ctx.response().end();
	}

	private static void loginUser(Long userId, String sessionToken, String remoteAdress, int expiry) {
		Sessions.newSession(userId, sessionToken, expiry, remoteAdress, TimeUnit.SECONDS);
	}

}
