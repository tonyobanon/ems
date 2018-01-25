package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ce.ems.base.classes.CursorMoveType;
import com.ce.ems.base.classes.EntityUtils;
import com.ce.ems.base.classes.FluentArrayList;
import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.IndexedNameSpec;
import com.ce.ems.base.classes.IndexedNameType;
import com.ce.ems.base.classes.ListableContext;
import com.ce.ems.base.classes.ListingFilter;
import com.ce.ems.base.classes.ListingType;
import com.ce.ems.base.classes.QueryFilter;
import com.ce.ems.base.classes.SearchableUISpec;
import com.ce.ems.base.core.BlockerBlockerTodo;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.CacheType;
import com.ce.ems.base.core.ClassIdentityType;
import com.ce.ems.base.core.ClasspathScanner;
import com.ce.ems.base.core.Exceptions;
import com.ce.ems.base.core.Listable;
import com.ce.ems.base.core.Logger;
import com.ce.ems.base.core.ModelMethod;
import com.ce.ems.base.core.ResourceException;
import com.ce.ems.base.core.Todo;
import com.ce.ems.entites.IndexedNameEntity;
import com.ce.ems.models.helpers.CacheHelper;
import com.ce.ems.utils.ObjectUtils;
import com.ce.ems.utils.Utils;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.QueryKeys;
import com.kylantis.eaa.core.fusion.CacheAdapter;
import com.kylantis.eaa.core.users.Functionality;

@BlockerTodo("Cleanup of listing contexts is poorly done, and this could be expensive")
@BlockerBlockerTodo("No sorting is done, please add support for sorting. It is extremely a Blocker")
@Todo("Make search functionality configurable")
public class SearchModel extends BaseModel {

	private static Map<Integer, Listable<?>> listables = new FluentHashMap<>();
	private static Map<Integer, SearchableUISpec> searchables = new FluentHashMap<>();

	// Persistent
	public static final String CACHE_KEY_LIST_$TYPE = "CACHE_KEY_LIST_$TYPE";
	// Short-lived
	public static final String CACHE_KEY_SEARCH_$TYPE_$PHRASE = "CACHE_KEY_SEARCH_$TYPE_$PHRASE";

	// Persistent
	public static final String CACHE_KEY_LIST_ENTRIES = "CACHE_KEY_LIST_ENTRIES";
	// Persistent
	public static final String CACHE_KEY_SEARCH_ENTRIES = "CACHE_KEY_SEARCH_ENTRIES";

	// Short-lived
	public static final String LISTABLE_CONTEXT_$KEY = "LISTABLE_CONTEXT_$KEY";

	@Override
	public void preInstall() {
		start();
	}

	@Override
	public void start() {

		Logger.info("Scanning Listables");

		new ClasspathScanner<>("List", Listable.class, ClassIdentityType.SUPER_CLASS).scanClasses().forEach(e -> {

			Listable<?> instance = null;
			try {
				instance = e.newInstance();
			} catch (InstantiationException | IllegalAccessException ex) {
				Exceptions.throwRuntime(ex);
			}

			listables.put(instance.type().getValue(), instance);

			if (instance.searchable()) {
				searchables.put(instance.type().getValue(), instance.searchableUiSpec());
			}

		});
	}

	@Override
	public String path() {
		return "core/search";
	}

	private static List<String> getListKeys() {
		return CacheHelper.getListOrDefault(CacheType.PERSISTENT, CACHE_KEY_LIST_ENTRIES, () -> {
			return new FluentArrayList<>();
		});
	}

	private static List<String> getSearchKeys() {
		return CacheHelper.getListOrDefault(CacheType.PERSISTENT, CACHE_KEY_SEARCH_ENTRIES, () -> {
			return new FluentArrayList<>();
		});

	}

	private static final String buildCacheListKey(IndexedNameType type, List<ListingFilter> listingFilters) {

		StringBuilder key = new StringBuilder().append(CACHE_KEY_LIST_$TYPE.replace("$TYPE", type.name().toString()));

		for (ListingFilter listingFilter : listingFilters) {

			key.append("____");

			for (Entry<String, Object> filter : listingFilter.getFilters().entrySet()) {
				key.append("__" + filter.getKey() + "_" + filter.getValue());
			}
		}

		return key.toString();
	}

	private static final String buildCacheSearchKey(IndexedNameType type, String phrase) {
		return CACHE_KEY_SEARCH_$TYPE_$PHRASE.replace("$TYPE", type.name()).replace("$PHRASE", phrase);
	}

	/**
	 * Because cached list entries, can be updated in real-time by calling
	 * addCachedListKey(..) and removeCachedListKey(..), it always contains
	 * up-to-date data, and therefore has a cache type of PERSISTENT
	 */
	private static final List<String> _list(IndexedNameType type, String order, List<ListingFilter> listingFilters) {
		String key = buildCacheListKey(type, listingFilters);

		List<String> cachedValue = CacheHelper.getListOrDefault(CacheType.PERSISTENT, key, () -> {
			CacheHelper.addToListOrCreate(CacheType.PERSISTENT, CACHE_KEY_LIST_ENTRIES, key);

			List<String> fetchedValue = list(type, order, listingFilters);
			return fetchedValue;
		});

		return cachedValue;
	}

	/**
	 * This has a cache type of VOLATILE
	 */
	private static final List<String> _search(IndexedNameType type, String phrase) {
		String key = buildCacheSearchKey(type, phrase);

		List<String> cachedValue = CacheHelper.getListOrDefault(CacheType.SHORT_LIVED, key, () -> {
			CacheHelper.addToListOrCreate(CacheType.PERSISTENT, CACHE_KEY_SEARCH_ENTRIES, key);

			List<String> fetchedValue = search(type, phrase);
			return fetchedValue;
		});

		return cachedValue;
	}

	public static void addCachedListKey(IndexedNameType type, Object elem) {
		addCachedListKey(type, new ArrayList<>(), elem);
	}

	public static void removeCachedListKey(IndexedNameType type, Object elem) {
		removeCachedListKey(type, new ArrayList<>(), elem);
	}

	/**
	 * Add a new key to a set of existing list keys for the specified type
	 */
	public static void addCachedListKey(IndexedNameType type, List<ListingFilter> listingFilters, Object elem) {
		String key = buildCacheListKey(type, listingFilters);
		CacheHelper.addToList(CacheType.PERSISTENT, key, elem.toString());
	}

	/**
	 * Remove a key to a set of existing list keys for the specified type
	 */
	public static void removeCachedListKey(IndexedNameType type, List<ListingFilter> listingFilters, Object elem) {
		String key = buildCacheListKey(type, listingFilters);
		CacheHelper.removeFromList(CacheType.PERSISTENT, key, elem.toString());
	}

	private static final String getCacheContextKey(String key) {
		return LISTABLE_CONTEXT_$KEY.replace("$KEY", key);
	}

	private static String newContext(IndexedNameType type, List<String> keys, Integer pageSize) {

		Integer keysSize = keys.size();

		Integer pageCount = null;

		if (keysSize <= pageSize || pageSize == -1) {
			pageCount = keysSize > 0 ? 1 : 0;
			pageSize = keysSize;
		} else {
			pageCount = keysSize / pageSize;

			if (keysSize % pageSize > 0) {
				pageCount += 1;
			}
		}

		ListableContext ctx = new ListableContext().setType(type).setPageSize(pageSize).setCurrentPage(0)
				.setId(Utils.newRandom());

		int index = 0;

		for (int i = 1; i <= pageCount; i++) {

			List<String> pageKeys = new ArrayList<>();

			for (int j = 0; j < pageSize && index < keysSize; j++) {
				pageKeys.add(keys.get(index));
				index++;
			}

			ctx.addPage(i, pageKeys);
		}

		ctx.setPageCount(pageCount);
		
		CacheAdapter.put(CacheType.SHORT_LIVED, getCacheContextKey(ctx.getId()), ctx);

		return ctx.getId();
	}

	private static boolean _hasNext(ListableContext ctx) {
		return (ctx.getCurrentPage() < ctx.getPageCount());
	}

	private static boolean _hasPrevious(ListableContext ctx) {
		return (ctx.getCurrentPage() > 1);
	}

	private static ListableContext getContext(String contextKey) {
		String cacheKey = getCacheContextKey(contextKey);
		ListableContext ctx = (ListableContext) CacheAdapter.get(cacheKey);
		return ctx;
	}

	@ModelMethod(functionality = Functionality.PERFORM_LIST_OPERATION)
	public static String newListContext(Long userId, IndexedNameType type, Integer pageSize, String order,
			List<ListingFilter> listingFilters) {

		Listable<?> model = listables.get(type.getValue());

		if (!model.authenticate(userId, listingFilters)) {
			throw new ResourceException(ResourceException.ACCESS_NOT_ALLOWED);
		}

		List<String> keys = _list(type, order, listingFilters);

		return newContext(type, keys, pageSize);
	}

	/**
	 * Note: When a search result for a particular phrase is cached, it takes
	 * CacheType.SHORT_LIVED seconds to be clear from cache
	 */
	@ModelMethod(functionality = Functionality.PERFORM_LIST_OPERATION)
	public static String newSearchContext(Long userId, IndexedNameType type, String phrase, Integer pageSize) {

		Listable<?> model = listables.get(type.getValue());

		if (!model.authenticate(userId, null)) {
			throw new ResourceException(ResourceException.ACCESS_NOT_ALLOWED);
		}

		List<String> keys = _search(type, phrase);
		return newContext(type, keys, pageSize);
	}

	@ModelMethod(functionality = Functionality.MANAGE_SYSTEM_CACHES)
	public static void clearCache(ListingType type) {
		switch (type) {
		case LIST:
			getListKeys().forEach(k -> {
				CacheAdapter.del(CacheType.PERSISTENT, k);
			});
			break;
		case SEARCH:
			getSearchKeys().forEach(k -> {
				CacheAdapter.del(CacheType.SHORT_LIVED, k);
			});
			break;
		}
	}

	@ModelMethod(functionality = Functionality.PERFORM_LIST_OPERATION)
	public static Boolean has(Long userId, CursorMoveType moveType, String contextKey) {

		ListableContext ctx = getContext(contextKey);

		Listable<?> instance = listables.get(ctx.getType().getValue());

		if (!instance.authenticate(userId, null)) {
			throw new ResourceException(ResourceException.ACCESS_NOT_ALLOWED);
		}

		switch (moveType) {
		case NEXT:
			return _hasNext(ctx);
		case PREVIOUS:
			return _hasPrevious(ctx);
		}
		return false;
	}

	@ModelMethod(functionality = Functionality.PERFORM_LIST_OPERATION)
	public static Boolean isContextAvailable(String contextKey) {
		return CacheAdapter.containsKey(CacheType.SHORT_LIVED, getCacheContextKey(contextKey));
	}

	@ModelMethod(functionality = Functionality.PERFORM_LIST_OPERATION)
	public static Map<?, ?> next(Long userId, CursorMoveType moveType, String contextKey) {

		ListableContext ctx = getContext(contextKey);

		IndexedNameType type = ctx.getType();

		Listable<?> o = listables.get(type.getValue());

		if (!o.authenticate(userId, null)) {
			throw new ResourceException(ResourceException.ACCESS_NOT_ALLOWED);
		}

		switch (moveType) {
		case NEXT:
			if (!_hasNext(ctx)) {
				return new HashMap<>();
			}
			break;
		case PREVIOUS:
			if (!_hasPrevious(ctx)) {
				return new HashMap<>();
			}
			break;
		}

		Integer currentPage = ctx.getCurrentPage() + (moveType.equals(CursorMoveType.NEXT) ? 1 : -1);

		List<String> keys = ctx.getPage(currentPage);
		
		ctx.setCurrentPage(currentPage);

		CacheAdapter.put(CacheType.SHORT_LIVED, getCacheContextKey(contextKey), ctx);

		return o.getAll(keys);
	}

	@ModelMethod(functionality = Functionality.GET_SEARCHABLE_LISTS)
	public static Map<Integer, SearchableUISpec> getSearchableLists(Long userId) {

		Map<Integer, SearchableUISpec> result = new FluentHashMap<>();

		searchables.keySet().forEach(k -> {

			Listable<?> o = listables.get(k);

			if (o.authenticate(userId, null)) {
				result.put(k, searchables.get(k));
			}
		});

		return searchables;
	}

	/**
	 * This is equivalent to calling removeIndexedName(..), then addIndexedName(..)
	 * for two entries that contain the same name permutations. The advantage of
	 * using this function instead is that the name permutations do not need to be
	 * re-computed and stored
	 */
	public static void updateIndexedNameType(Object oldEntityId, Object newEntityId, IndexedNameType oldType,
			IndexedNameType newType) {

		ofy().load().type(IndexedNameEntity.class).filter("entityId = ", oldEntityId.toString()).forEach(e -> {
			if (e.getType().equals(oldType.getValue())) {

				e.setEntityId(newEntityId.toString());
				e.setType(newType.getValue());

				ofy().save().entity(e);
			}
		});
	}

	@Todo("Investigate if filter for an indexed field can be added to a query, and any filter for non-indexed field can be added afterwards")
	public static void removeIndexedName(String entityId, IndexedNameType type) {

		List<Key<?>> keys = new FluentArrayList<>();

		ofy().load().type(IndexedNameEntity.class).filter("entityId = ", entityId).forEach(e -> {
			if (e.getType().equals(type.getValue())) {
				keys.add(Key.create(IndexedNameEntity.class, e.getId()));
			}
		});

		ofy().delete().keys(keys).now();
	}

	public static void addIndexedName(IndexedNameSpec spec, IndexedNameType type) {

		if (spec.getX() == null) {
			return;
		}

		// Run permutation function on names to get possible combinations

		List<String> nameList = new FluentArrayList<String>().with(spec.getX()).addIfNotNull(spec.getY())
				.addIfNotNull(spec.getZ());
		Integer[] indexes = Utils.indexes(nameList.size());

		List<IndexedNameEntity> ies = new FluentArrayList<>();

		Utils.permute(indexes).forEach(l1 -> {

			String x = null;
			String y = null;
			String z = null;

			switch (indexes.length) {
			case 1:
				x = nameList.get(l1.get(0));
				break;
			case 2:
				x = nameList.get(l1.get(0));
				y = nameList.get(l1.get(1));
				break;
			case 3:
				x = nameList.get(l1.get(0));
				y = nameList.get(l1.get(1));
				z = nameList.get(l1.get(2));
				break;
			}

			IndexedNameEntity ie = new IndexedNameEntity().setType(type.getValue()).setEntityId(spec.getKey()).setX(x)
					.setY(y).setZ(z);

			ies.add(ie);
		});

		// Batch save

		ofy().save().entities(ies).now();
	}

	private static final <T> List<String> list(IndexedNameType type, String order, List<ListingFilter> listingFilters) {

		Listable<?> o = listables.get(type.getValue());

		Class<T> T = (Class<T>) o.entityType();

		// Fetch all keys for this entity

		List<String> keys = new FluentArrayList<>();

		if (listingFilters.isEmpty()) {
			EntityUtils.lazyQuery(T, order).keys().forEach(k -> {
				keys.add(ObjectUtils.toKeyString(k));
			});
		} else {

			for (ListingFilter listingFilter : listingFilters) {

				List<QueryFilter> filters = new FluentArrayList<>();

				listingFilter.getFilters().forEach((k, v) -> {
					filters.add(QueryFilter.get(k, v));
				});
				
				QueryKeys<T> queryKeys = EntityUtils.lazyQuery(T, order, filters.toArray(new QueryFilter[filters.size()])).keys();
				
				for(Key<T> k : queryKeys) {
					keys.add(ObjectUtils.toKeyString(k));	
				}
			}
		}

		return keys;
	}

	@BlockerTodo("Add metrics to measure performance")
	private static List<String> search(IndexedNameType type, String phrase) {

		if (!searchables.containsKey(type.getValue())) {
			throw new ResourceException(ResourceException.ACCESS_NOT_ALLOWED,
					"Index type: " + type.name() + " is not searchable");
		}

		String[] fields = new String[] { "x", "y", "z" };
		String[] names = phrase.split("\\s");

		FluentHashMap<String, String> values = FluentHashMap.forNameMap();

		switch (names.length) {
		case 1:
			values.with(fields[0], names[0]);
			break;
		case 2:
			values.with(fields[0], names[0]).with(fields[1], names[1]);
			break;
		case 3:
			values.with(fields[0], names[0]).with(fields[1], names[1]).with(fields[2], names[2]);
			break;
		}

		List<Long> keys = new FluentArrayList<>();
		List<String> filteredKeys = new FluentArrayList<>();

		EntityUtils.search(IndexedNameEntity.class, values, o -> {
			keys.add(o.getId());
		});

		ofy().load().type(IndexedNameEntity.class).ids(keys).forEach((k, v) -> {
			if (v.getType().equals(type.getValue()) && !filteredKeys.contains(v.getEntityId())) {
				filteredKeys.add(v.getEntityId());
			}
		});

		return filteredKeys;
	}

	static {
	}

}
