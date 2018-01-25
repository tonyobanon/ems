package com.kylantis.eaa.core.fusion;

import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.models.BaseUserModel;
import com.ce.ems.models.RoleModel;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.ext.web.RoutingContext;

@BlockerTodo("Addm helper method here to check param instead of always checking for undefined")
public abstract class BaseService {

	protected String getLocationHeader() {
		return "X-Location";
	}

	public void isAccessAllowed(RoutingContext ctx, Functionality... functionalities) {

		Long principal = FusionHelper.getUserId(ctx.request());
		String roleName = BaseUserModel.getRole(principal);

		Boolean b = RoleModel.isAccessAllowed(roleName, functionalities);

		ctx.response().write(b.toString()).setChunked(true);
	}

}
