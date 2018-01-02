package com.kylantis.eaa.core.fusion;

import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;
import com.ce.ems.models.BaseUserModel;
import com.ce.ems.models.CacheModel;
import com.ce.ems.models.RoleModel;
import com.kylantis.eaa.core.keys.CacheKeys;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;

public class FusionHelper {

	public static Long getUserIdFromToken(String sessionToken) {
		
		if(sessionToken == null) {
			return null;
		}
		
		String value = (String) CacheModel.get(CacheKeys.SESSION_TOKEN_TO_USER_ID_$TOKEN.replace("$TOKEN", sessionToken));
		return value != null ? Long.parseLong(value) : null;
	}

	public static Long getUserId(HttpServerRequest req) {
		return Long.parseLong(req.getParam(APIRoutes.USER_ID_PARAM_NAME));
	}
	
	public static void setUserId(HttpServerRequest req, Long userId) {
		req.params().add(APIRoutes.USER_ID_PARAM_NAME, userId.toString());
	}

	public static List<String> getRoles(Long userId) {
		return new FluentArrayList<String>().with(BaseUserModel.getRole(userId));
	}

	public static final String sessionTokenName() {
		return "X-Session-Token";
	}
	
	public static boolean isAccessAllowed(String roleName, Integer functionalityId) {

		// Check that this role has the right to view this page

		List<Integer> functionalities = FusionHelper.getCachedFunctionalities(roleName);

		if (functionalities == null) {

			functionalities = FusionHelper.getFunctionalities(roleName);

			// Cache role functionalities
			FusionHelper.cacheFunctionalities(roleName, functionalities);
		}

		return (functionalities.contains(functionalityId));
	}

	public static void cacheFunctionalities(String roleName, List<Integer> functionalities) {
		CacheModel.put(CacheKeys.ROLE_FUNCTIONALITIES_$ROLE.replace("$ROLE", roleName),
				new JsonArray(functionalities).toString());
	}

	public static List<Integer> getCachedFunctionalities(String roleName) {
		String o = CacheModel.get(CacheKeys.ROLE_FUNCTIONALITIES_$ROLE.replace("$ROLE", roleName)).toString();
		if (o != null) {
			return new JsonArray(o).getList();
		}
		return null;
	}

	private static List<Integer> getFunctionalities(String roleName) {
		return RoleModel.getRoleFunctionalities(roleName);
	}
}
