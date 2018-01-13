package com.ce.ems.models;

import java.util.Collection;
import java.util.LinkedHashMap;

import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.ClassIdentityType;
import com.ce.ems.base.core.ClasspathScanner;
import com.ce.ems.base.core.Exceptions;
import com.ce.ems.base.core.Logger;
import com.ce.ems.base.core.Model;

@BlockerTodo("Create classes containing entity attribute names. See project <eaa>")
public abstract class BaseModel {

	public static final String DEFAULT_MODEL_VERSION = "";
	
	private static LinkedHashMap<String, BaseModel> linkedModels = new LinkedHashMap<String, BaseModel>();
	
	/**
	 * This method is used by models to populate data into their tables after
	 * Install Options are available, as well as add default metric data. It also
	 * should contain logic required to start the model
	 */
	public void install(InstallOptions options) {

	}
	
	public abstract String path();
	
	public static Collection<BaseModel> getModels() {
		return linkedModels.values();
	}

	public static void scanModels() {
		
		Logger.info("Scanning models ..");
		
		new ClasspathScanner<>("Model", BaseModel.class, ClassIdentityType.SUPER_CLASS).scanClasses().forEach(e -> {
			addConcreteModel(null, e);
		});
	}
	
	private static final void addConcreteModel(String dependants, Class<? extends BaseModel> model) {

		//Logger.info("Registering " + model.getSimpleName());
		
		Model modelAnnotation = model.getAnnotation(Model.class);

		if (modelAnnotation != null) {

			if (!modelAnnotation.enabled()) {
				return;
			}

			for (Class<? extends BaseModel> dependency : modelAnnotation.dependencies()) {
				
				if (dependants != null) {
					if (dependants.contains(dependency.getSimpleName())) {
						// Circular reference was detected
						Exceptions.throwRuntime(new RuntimeException("Circular reference was detected: "
								+ (dependants + " -> " + model.getSimpleName() + " -> " + dependency.getSimpleName())
										.replaceAll(dependency.getSimpleName(),
												"(" + dependency.getSimpleName() + ")")));
					}
					
					addConcreteModel(dependants + " -> " + model.getSimpleName(), dependency);
					
					
				} else {
					addConcreteModel(model.getSimpleName(), dependency);
				}
			}
		}

		try {

			BaseModel instance = model.newInstance();

			if(!linkedModels.containsKey(instance.path())) {
				linkedModels.put(instance.path(), instance);
			}

		} catch (InstantiationException | IllegalAccessException e) {
			Exceptions.throwRuntime(e);
		}
	}

	public void start() {
		
	}
	
	public void update() {
	}
	
	/**
	 * This method is used by models to populate data into their tables before
	 * Install Options are available, as well as add default metric data.
	 */
	public void preInstall() {
	}

	public void unInstall() {
	}

}
