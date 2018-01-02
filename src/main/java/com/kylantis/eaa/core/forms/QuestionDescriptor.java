package com.kylantis.eaa.core.forms;

public class QuestionDescriptor {

	private final QuestionType type;
	private final boolean isDefault;
	
	public QuestionDescriptor(QuestionType type, boolean isDefault) {
		this.type = type;
		this.isDefault = isDefault;
	}

	public QuestionType getType() {
		return type;
	}

	public boolean isDefault() {
		return isDefault;
	}
	
}
