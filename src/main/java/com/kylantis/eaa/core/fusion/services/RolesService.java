package com.kylantis.eaa.core.fusion.services;

import java.util.List;
import java.util.Map;

import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.models.CacheModel;
import com.ce.ems.models.RoleModel;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.keys.CacheKeys;
import com.kylantis.eaa.core.users.Functionality;
import com.kylantis.eaa.core.users.RoleRealm;
import com.kylantis.eaa.core.users.RoleUpdateAction;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@EndpointClass(uri = "/roles")
public class RolesService extends BaseService {

	@EndpointMethod(uri = "/new-role", bodyParams = { "roleName", "realm"}, method = HttpMethod.PUT,
			functionality = Functionality.MANAGE_ROLES)
	public void newRole(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		String roleName = body.getString("roleName");
		RoleRealm roleRealm = RoleRealm.from(body.getInteger("realm"));
		
		RoleModel.newRole(roleName, roleRealm);
	}

	@EndpointMethod(uri = "/role", requestParams = { "roleName" }, method = HttpMethod.DELETE,
			functionality = Functionality.MANAGE_ROLES)
	public void deleteRole(RoutingContext ctx) {
		String roleName = ctx.request().getParam("roleName");
		RoleModel.deleteRole(roleName);
	}
	

	@EndpointMethod(uri = "/list", requestParams = { "realm" }, method = HttpMethod.GET,
			functionality = Functionality.MANAGE_ROLES)
	public void listRoles(RoutingContext ctx) {
		
		String realm = ctx.request().getParam("realm");
		
		Map<String, Integer> roles = 
				realm == null ?
				RoleModel.listRoles() :
					RoleModel.listRoles(RoleRealm.from(Integer.parseInt(realm)));
				
		ctx.response().write(GsonFactory.newInstance().toJson(roles)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/user-count", bodyParams = { "roleNames" }, method = HttpMethod.POST,
			functionality = Functionality.MANAGE_ROLES)
	public void getUsersCount(RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();
		JsonArray names = body.getJsonArray("roleNames");
		
		Map<String, Integer> result = RoleModel.getUsersCount(names.getList());
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/realms", method = HttpMethod.GET,
			functionality = Functionality.LIST_ROLE_REALMS)
	public void listRealms(RoutingContext ctx) {
		Map<String, Integer> roles = RoleModel.listRoles();
		ctx.response().write(GsonFactory.newInstance().toJson(roles)).setChunked(true).end();
	}
	
	/**
	 * This retrieves all the functionalities applicable to this role realm
	 * */
	@EndpointMethod(uri = "/realm-functionalities", requestParams = { "realm" },
			functionality = Functionality.MANAGE_ROLES)
	public void getRealmFunctionalities(RoutingContext ctx) {
		RoleRealm roleRealm = RoleRealm.from(Integer.parseInt(ctx.request().getParam("realm")));
		String json = GsonFactory.newInstance().toJson(RoleModel.getRealmFunctionalities(roleRealm));
		ctx.response().setChunked(true).write(json).end();
	}

	/**
	 * This retrieves all the functionalities for this role
	 * */
	@EndpointMethod(uri = "/functionalities", requestParams = { "roleName" },
			functionality = Functionality.MANAGE_ROLES)
	public void getRoleFunctionalities(RoutingContext ctx) {
		
		String roleName = ctx.request().getParam("roleName");
		List<Integer> e = RoleModel.getRoleFunctionalities(roleName);

		CacheModel.put(CacheKeys.ROLE_FUNCTIONALITIES_$ROLE.replace("$ROLE", roleName),
				new JsonArray(e).toString());

		String json = GsonFactory.newInstance().toJson(e);
		ctx.response().setChunked(true).write(json).end();
	}

	@EndpointMethod(uri = "/update-spec", bodyParams = { "roleName", "functionality", "action" }, method = HttpMethod.POST,
			functionality = Functionality.MANAGE_ROLES)
	public void updateRoleSpec(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		String roleName = body.getString("roleName");
		Integer functionality = Integer.parseInt(body.getString("functionality"));
		RoleUpdateAction updateAction = RoleUpdateAction.from(body.getInteger("action"));

		RoleModel.updateRoleSpec(roleName, updateAction, functionality);

		CacheModel.del(CacheKeys.ROLE_FUNCTIONALITIES_$ROLE.replace("$ROLE", roleName));
	}
	
	@EndpointMethod(uri = "/default-role", requestParams = { "realm" },
			functionality = Functionality.MANAGE_ROLES)
	public void getDefaultRole(RoutingContext ctx) {
		RoleRealm roleRealm = RoleRealm.from(Integer.parseInt(ctx.request().getParam("realm")));
		String role = RoleModel.getDefaultRole(roleRealm);
		ctx.response().setChunked(true).write(role).end();
	}

}
