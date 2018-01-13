package com.kylantis.eaa.core.fusion.services;

import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.models.BaseUserModel;
import com.ce.ems.models.CacheModel;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.fusion.FusionHelper;
import com.kylantis.eaa.core.keys.CacheKeys;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@BlockerTodo("Always verity user's email")
@EndpointClass(uri = "/users")
public class UserService extends BaseService {

	@EndpointMethod(uri = "/get-own-profile", functionality = Functionality.VIEW_OWN_PROFILE)
	public void getOwnProfile(RoutingContext ctx) {
		Long userId = FusionHelper.getUserId(ctx.request());
		String json = (String) CacheModel.get(CacheKeys.USER_PROFILE_$USER.replace("$USER", userId.toString()));
		if (json != null) {
			ctx.response().setChunked(true).write(json);
		} else {
			json = GsonFactory.newInstance().toJson(BaseUserModel.getProfile(userId));
			CacheModel.put(CacheKeys.USER_PROFILE_$USER.replace("$USER", userId.toString()), json);
			ctx.response().setChunked(true).write(json);
		}
	}

	@EndpointMethod(uri = "/get-user-profile", requestParams = {
			"userId" }, functionality = Functionality.GET_USER_PROFILE)
	public void getUserProfile(RoutingContext ctx) {

		Long userId = Long.parseLong(ctx.request().getParam("userId"));
		String json = (String) CacheModel.get(CacheKeys.USER_PROFILE_$USER.replace("$USER", userId.toString()));
		if (json != null) {
			ctx.response().setChunked(true).write(json);
		} else {
			json = GsonFactory.newInstance().toJson(BaseUserModel.getProfile(userId));
			CacheModel.put(CacheKeys.USER_PROFILE_$USER.replace("$USER", userId.toString()), json);
			ctx.response().setChunked(true).write(json);
		}
	}

	@EndpointMethod(uri = "/get-own-avatar", functionality = Functionality.VIEW_OWN_PROFILE)
	public void getOwnAvatar(RoutingContext ctx) {
		Long userId = FusionHelper.getUserId(ctx.request());
		String image = BaseUserModel.getAvatar(userId);
		ctx.response().setChunked(true).write(new JsonObject().put("image", image).encode());
	}

	@EndpointMethod(uri = "/get-user-avatar", requestParams = {
			"userId" }, functionality = Functionality.MANAGE_USER_ACCOUNTS)
	public void geUserAvatar(RoutingContext ctx) {
		Long userId = Long.parseLong(ctx.request().getParam("userId"));
		String image = BaseUserModel.getAvatar(userId);
		ctx.response().setChunked(true).write(new JsonObject().put("image", image).encode());
	}
 
	@EndpointMethod(uri = "/get-own-role", functionality = Functionality.VIEW_OWN_PROFILE)
	public void getOwnRole(RoutingContext ctx) {
		Long userId = FusionHelper.getUserId(ctx.request());
		String role = BaseUserModel.getRole(userId);
		ctx.response().setChunked(true).write(new JsonObject().put("role", role).encode());
	}

	@EndpointMethod(uri = "/get-user-role", requestParams = {
			"userId" }, functionality = Functionality.MANAGE_USER_ACCOUNTS)
	public void geUserRole(RoutingContext ctx) {
		Long userId = Long.parseLong(ctx.request().getParam("userId"));
		String role = BaseUserModel.getRole(userId);
		ctx.response().setChunked(true).write(new JsonObject().put("role", role).encode());
	}

	@EndpointMethod(uri = "/update-own-email", bodyParams = {
			"email" }, method = HttpMethod.POST, functionality = Functionality.MANAGE_OWN_PROFILE)
	public void updateOwnEmail(RoutingContext ctx) {

		Long userId = FusionHelper.getUserId(ctx.request());

		JsonObject body = ctx.getBodyAsJson();
		String email = body.getString("email");

		BaseUserModel.updateEmail(null, userId, email);
		CacheModel.del(CacheKeys.USER_PROFILE_$USER.replace("$USER", userId.toString()));
	}

	@EndpointMethod(uri = "/update-user-email", bodyParams = { "userId",
			"email" }, method = HttpMethod.POST, functionality = Functionality.MANAGE_USER_ACCOUNTS)
	public void updateUserEmail(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long principal = FusionHelper.getUserId(ctx.request());

		Long userId = body.getLong("userId");
		String email = body.getString("email");

		BaseUserModel.updateEmail(principal, userId, email);
		CacheModel.del(CacheKeys.USER_PROFILE_$USER.replace("$USER", userId.toString()));
	}

	@EndpointMethod(uri = "/update-own-phone", bodyParams = {
			"phone" }, method = HttpMethod.POST, functionality = Functionality.MANAGE_OWN_PROFILE)
	public void updateOwnPhone(RoutingContext ctx) {

		Long userId = FusionHelper.getUserId(ctx.request());

		JsonObject body = ctx.getBodyAsJson();
		Long phone = Long.parseLong(body.getString("phone"));

		BaseUserModel.updatePhone(null, userId, phone);
		CacheModel.del(CacheKeys.USER_PROFILE_$USER.replace("$USER", userId.toString()));
	}

	@EndpointMethod(uri = "/update-user-phone", bodyParams = { "userId",
			"phone" }, method = HttpMethod.POST, functionality = Functionality.MANAGE_USER_ACCOUNTS)
	public void updateUserPhone(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long principal = FusionHelper.getUserId(ctx.request());

		Long userId = body.getLong("userId");
		Long phone = Long.parseLong(body.getString("phone"));

		BaseUserModel.updatePhone(principal, userId, phone);
		CacheModel.del(CacheKeys.USER_PROFILE_$USER.replace("$USER", userId.toString()));
	}

	@EndpointMethod(uri = "/update-own-password", bodyParams = { "current",
			"newPassword" }, method = HttpMethod.POST, functionality = Functionality.MANAGE_OWN_PROFILE)
	public void updateOwnPassword(RoutingContext ctx) {

		Long userId = FusionHelper.getUserId(ctx.request());

		JsonObject body = ctx.getBodyAsJson();
		String current = body.getString("current");
		String newPassword = body.getString("newPassword");

		BaseUserModel.updatePassword(null, userId, current, newPassword);
		CacheModel.del(CacheKeys.USER_PROFILE_$USER.replace("$USER", userId.toString()));
	}

	@EndpointMethod(uri = "/update-user-password", bodyParams = { "userId", "current",
			"newPassword" }, method = HttpMethod.POST, functionality = Functionality.MANAGE_USER_ACCOUNTS)
	public void updateUserPassword(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long principal = FusionHelper.getUserId(ctx.request());

		Long userId = body.getLong("userId");
		String current = body.getString("current");
		String newPassword = body.getString("newPassword");

		BaseUserModel.updatePassword(principal, userId, current, newPassword);
		CacheModel.del(CacheKeys.USER_PROFILE_$USER.replace("$USER", userId.toString()));
	}

	@EndpointMethod(uri = "/update-own-avatar", bodyParams = {
			"blobId" }, method = HttpMethod.POST, functionality = Functionality.MANAGE_OWN_PROFILE)
	public void updateOwnAvatar(RoutingContext ctx) {

		Long userId = FusionHelper.getUserId(ctx.request());

		JsonObject body = ctx.getBodyAsJson();
		String blobId = body.getString("blobId");

		BaseUserModel.updateAvatar(null, userId, blobId);
		CacheModel.del(CacheKeys.USER_PROFILE_$USER.replace("$USER", userId.toString()));
	}

	@EndpointMethod(uri = "/update-user-avatar", bodyParams = { "userId",
			"blobId" }, method = HttpMethod.POST, functionality = Functionality.MANAGE_USER_ACCOUNTS)
	public void updateUserAvatar(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long principal = FusionHelper.getUserId(ctx.request());

		Long userId = body.getLong("userId");
		String blobId = body.getString("blobId");

		BaseUserModel.updateAvatar(principal, userId, blobId);
		CacheModel.del(CacheKeys.USER_PROFILE_$USER.replace("$USER", userId.toString()));
	}

	@EndpointMethod(uri = "/update-role", bodyParams = { "userId",
			"role" }, method = HttpMethod.POST, functionality = Functionality.MANAGE_USER_ACCOUNTS)
	public void updateUserRole(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long principal = FusionHelper.getUserId(ctx.request());

		Long userId = Long.parseLong(body.getString("userId"));
		String role = body.getString("role");

		BaseUserModel.updateRole(principal, userId, role);
		CacheModel.del(CacheKeys.USER_PROFILE_$USER.replace("$USER", userId.toString()));
	}

	@EndpointMethod(uri = "/get-person-name", requestParams = { "userId",
			"full" }, functionality = Functionality.GET_PERSON_NAMES)
	public void getPersonName(RoutingContext ctx) {
		   
		Long userId = null;
  
		if (ctx.request().getParam("userId").equals("undefined")) {
			userId = FusionHelper.getUserId(ctx.request());
		} else { 
			userId = Long.parseLong(ctx.request().getParam("userId"));
		}  

		Boolean full = Boolean.parseBoolean(ctx.request().getParam("full"));
 
		Object personName = BaseUserModel.getPersonName(userId, full);
 
		ctx.response().setChunked(true).write(new JsonObject().put("name", personName).encode());
	}
 
}
