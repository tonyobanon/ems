package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.ClientResources;
import com.ce.ems.base.classes.ClientResources.ClientRBRef;
import com.ce.ems.base.classes.EntityUtils;
import com.ce.ems.base.classes.QueryFilter;
import com.ce.ems.base.classes.spec.BaseUserSpec;
import com.ce.ems.base.classes.spec.DepartmentLevelSpec;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.ResourceException;
import com.ce.ems.entites.BaseUserEntity;
import com.ce.ems.entites.directory.DepartmentEntity;
import com.ce.ems.entites.directory.LecturerEntity;
import com.ce.ems.entites.directory.StudentEntity;
import com.googlecode.objectify.Key;
import com.kylantis.eaa.core.users.RoleRealm;

public class UserModel extends BaseModel {

	@Override
	public String path() {
		return "core/users";
	}

	@BlockerTodo("Reduce <keys> to a definite number. Find fix")
	public static List<BaseUserSpec> getSuggestedProfiles(Long principal, Long userId) {

		if(!BaseUserModel.canAccessUserProfile(principal, userId)) {
			throw new ResourceException(ResourceException.ACCESS_NOT_ALLOWED);
		}
				
		// Get role
		String role = BaseUserModel.getRole(userId);
		RoleRealm realm = RoleModel.getRealm(role);

		Map<Long, String> keys = new HashMap<>();

		switch (realm) {

		case ADMIN:
		case EXAM_OFFICER:

			List<Key<BaseUserEntity>> _keys = new ArrayList<>();

			// Get other users with this role
			_keys = EntityUtils.lazyQuery(BaseUserEntity.class, QueryFilter.get("role", role)).keys().list();

			if (_keys.size() == 0) {
				for (String r : RoleModel.listRoles(realm).keySet()) {
					_keys.addAll(EntityUtils.lazyQuery(BaseUserEntity.class, QueryFilter.get("role", r)).keys().list());
				}
			}

			_keys.forEach(k -> {
				keys.put(k.getId(), ClientRBRef.get(role).toString());
			});

			break;

		case DEAN:

			DModel.listFaculties().forEach(e -> {
				keys.put(e.getDean(),
						ClientRBRef.forAll("Dean", "of").toString() + ClientResources.HtmlCharacterEnties.SPACE
								+ e.getName() + ClientResources.HtmlCharacterEnties.SPACE
								+ ClientRBRef.get("Faculty").toString());
			});

			break;

		case HEAD_OF_DEPARTMENT:

			ofy().load().type(DepartmentEntity.class).forEach(e -> {
				keys.put(e.getHeadOfDepartment(),
						ClientRBRef.forAll("Head_of_department", "for").toString()
								+ e.getName()
								+ ClientResources.HtmlCharacterEnties.SPACE + ClientRBRef.get("Department").toString());
			}); 
 
			break;

		case LECTURER:

			LecturerEntity le = ofy().load().type(LecturerEntity.class).id(userId).safe();
			DepartmentEntity de = ofy().load().type(DepartmentEntity.class).id(le.getDepartment()).safe();

			DModel.getLecturerIds(le.getDepartment()).forEach(l -> {
				keys.put(l,
						ClientRBRef.forAll("Lecturer", "in").toString() + ClientResources.HtmlCharacterEnties.SPACE
								+ de.getName() + ClientResources.HtmlCharacterEnties.SPACE
								+ ClientRBRef.get("Department").toString());
			});

			break;
		case STUDENT:

			StudentEntity se = ofy().load().type(StudentEntity.class).id(userId).safe();
			DepartmentLevelSpec dl = DModel.getDepartmentLevelForId(se.getDepartmentLevel());

			DModel.getStudentIds(dl.getId()).forEach(l -> {
				keys.put(l, ClientRBRef.forAll("Student", "of").toString() + ClientResources.HtmlCharacterEnties.SPACE
						+ DModel.getDepartmentName(dl.getDepartment())
						+ ClientResources.HtmlCharacterEnties.SPACE + ClientRBRef.get(dl.toString()).toString());
			});

			break;
		}

		keys.remove(userId);
		
		
		
		return getBaseUserSpec(keys);
	}

	private static List<BaseUserSpec> getBaseUserSpec(Map<Long, String> keys) {

		List<BaseUserSpec> result = new ArrayList<>();

		ofy().load().type(BaseUserEntity.class).ids(keys.keySet()).forEach((k, v) -> {

			BaseUserSpec spec = new BaseUserSpec().setId(v.getId()).setRole(v.getRole()).setDescription(keys.get(k))
					.setName(v.getFirstName() + ClientResources.HtmlCharacterEnties.SPACE + v.getLastName())
					.setDateCreated(v.getDateCreated()).setDateUpdated(v.getDateUpdated()).setImage(v.getImage());

			result.add(spec);

		});
		return result;
	}

}
