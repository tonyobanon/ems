package com.kylantis.eaa.core.fusion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.core.Logger;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.kylantis.eaa.core.cron.Scheduler;
import com.kylantis.eaa.core.keys.CacheKeys;

public class Sessions {

	public static final String USER_SESSIONS_HASH = "user_sessions";
	public static final String SESSION_ADDRESSES_HASH = "session_addresses";

	public static void newSession(Long userId, String sessionToken, int duration, String remoteAddress,
			TimeUnit timeUnit) {

		Logger.trace("Creating session: " + sessionToken + " for user: " + userId);

		// Save token in Cache
		CacheAdapter.put(CacheKeys.SESSION_TOKEN_TO_USER_ID_$TOKEN.replace("$TOKEN", sessionToken), userId);

		Map<String, String> userSessions = getMap(USER_SESSIONS_HASH);
		Map<String, String> sessionAddresses = getMap(SESSION_ADDRESSES_HASH);
		
		if (userSessions == null) {
			
			// Cache was probably cleared, create session maps
			createMap(USER_SESSIONS_HASH);
			createMap(SESSION_ADDRESSES_HASH);
			
			userSessions = getMap(USER_SESSIONS_HASH);
			sessionAddresses = getMap(SESSION_ADDRESSES_HASH);
		}
		
		// Store Session Token for this userId

		String currentTokens = userSessions.get(userId.toString());
		String newTokens = (currentTokens != null ? currentTokens + "," : "") + sessionToken;


		userSessions.put(userId.toString(), newTokens);

		// Store remote address for this session
		sessionAddresses.put(sessionToken, remoteAddress);

		// Create map for storing session data
		String sessionDataKey = CacheKeys.SESSION_DATA_$SESSIONTOKEN.replace("$SESSIONTOKEN", sessionToken);
		CacheAdapter.put(sessionDataKey, new FluentHashMap<>());

		// Schedule token deletion on expiration
		Scheduler.schedule(new Runnable() {

			@Override
			public void run() {

				removeSession(userId, sessionToken);
			}
		}, duration, timeUnit);
	}

	protected static Long getUserId(String sessionToken) {
		String userId = (String) CacheAdapter
				.get(CacheKeys.SESSION_TOKEN_TO_USER_ID_$TOKEN.replace("$TOKEN", sessionToken));
		return Long.parseLong(userId);
	}

	protected static void removeSession(String sessionToken) {
		removeSession(getUserId(sessionToken), sessionToken);
	}

	protected static void removeSession(Long userId, String sessionToken) {

		CacheAdapter.del(CacheKeys.SESSION_TOKEN_TO_USER_ID_$TOKEN.replace("$TOKEN", sessionToken));

		Logger.trace("Removing session: " + sessionToken + " for user: " + userId);

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
		CacheAdapter.del(CacheKeys.SESSION_DATA_$SESSIONTOKEN.replace("$SESSIONTOKEN", sessionToken));
	}

	protected static void set(String sessionToken, String key, String value) {
		String sessionDatahashKey = CacheKeys.SESSION_DATA_$SESSIONTOKEN.replace("$SESSIONTOKEN", sessionToken);
		getMap(sessionDatahashKey).put(key, value);
	}

	protected static String get(String sessionToken, String key) {
		String sessionDatahashKey = CacheKeys.SESSION_DATA_$SESSIONTOKEN.replace("$SESSIONTOKEN", sessionToken);
		return getMap(sessionDatahashKey).get(key);
	}

	private static void createMap(String name) {
		CacheAdapter.getLonglivedCache().put(name, new HashMap<>());
	}

	private static Map<String, String> getMap(String name) {
		return (Map<String, String>) CacheAdapter.getLonglivedCache().get(name);
	}
}
