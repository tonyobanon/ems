package com.kylantis.eaa.core.fusion;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.core.Logger;
import com.ce.ems.models.CacheModel;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.kylantis.eaa.core.cron.Scheduler;
import com.kylantis.eaa.core.keys.CacheKeys;

public class Sessions {

	public static final String USER_SESSIONS_HASH = "user_sessions";
	public static final String SESSION_ADDRESSES_HASH = "session_addresses";

	public static void newSession(Long userId, String sessionToken, int duration, String remoteAddress,
			TimeUnit timeUnit) {

		// Save token in Redis
		CacheModel.put(CacheKeys.SESSION_TOKEN_TO_USER_ID_$TOKEN.replace("$TOKEN", sessionToken), userId);

		// Store Session Token for this userId

		String currentTokens = getMap(USER_SESSIONS_HASH).get(userId.toString());
		String newTokens = (currentTokens != null ? currentTokens + "," : "") + sessionToken;

		getMap(USER_SESSIONS_HASH).put(userId.toString(), newTokens);

		// Store remote address for this session
		getMap(SESSION_ADDRESSES_HASH).put(sessionToken, remoteAddress);

		//Create map for storing session data
		String sessionDataKey = CacheKeys.SESSION_DATA_$SESSIONTOKEN.replace("$SESSIONTOKEN", sessionToken);
		CacheModel.put(sessionDataKey, new FluentHashMap<>());
		
		// Schedule token deletion on expiration
		Scheduler.schedule(new Runnable() {
			
			@Override
			public void run() {
				
				removeSession(userId, sessionToken);
			}
		}, duration, timeUnit);
	}

	protected static Long getUserId(String sessionToken) {
		String userId = (String) CacheModel.get(CacheKeys.SESSION_TOKEN_TO_USER_ID_$TOKEN.replace("$TOKEN", sessionToken));
		return Long.parseLong(userId);
	}

	protected static void removeSession(String sessionToken) {
		removeSession(getUserId(sessionToken), sessionToken);
	}

	protected static void removeSession(Long userId, String sessionToken) {

		CacheModel.del(CacheKeys.SESSION_TOKEN_TO_USER_ID_$TOKEN.replace("$TOKEN", sessionToken));
		
		// Remove Session Token for this userId
		List<String> currentTokens = Splitter.on(",").splitToList(getMap(USER_SESSIONS_HASH).get(userId.toString()));
		currentTokens.remove(sessionToken);

		if (currentTokens.size() > 0) {
			
			getMap(USER_SESSIONS_HASH).put(userId.toString(), Joiner.on(",").join(currentTokens));
		} else {
			getMap(USER_SESSIONS_HASH).remove(userId.toString());
		}

		// Remove remote address for this sessionToken
		getMap(SESSION_ADDRESSES_HASH).remove(sessionToken);

		// Remove Session Data
		CacheModel.del(CacheKeys.SESSION_DATA_$SESSIONTOKEN.replace("$SESSIONTOKEN", sessionToken));
	}

	protected static void set(String sessionToken, String key, String value) {
		String sessionDatahashKey = CacheKeys.SESSION_DATA_$SESSIONTOKEN.replace("$SESSIONTOKEN", sessionToken);
		getMap(sessionDatahashKey).put(key, value);
	}

	protected static String get(String sessionToken, String key) {
		String sessionDatahashKey = CacheKeys.SESSION_DATA_$SESSIONTOKEN.replace("$SESSIONTOKEN", sessionToken);
		return getMap(sessionDatahashKey).get(key);
	}

	private static Map<String, String> getMap(String name) {
		return (Map<String, String>) CacheModel.getLonglivedCache().get(name);
	}
	
	static {
		
		Logger.info("Creating Session Caches");
		
		CacheModel.put(USER_SESSIONS_HASH, new FluentHashMap<>());
		CacheModel.put(SESSION_ADDRESSES_HASH, new FluentHashMap<>());
	}

}
