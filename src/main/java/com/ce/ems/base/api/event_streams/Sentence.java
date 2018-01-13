package com.ce.ems.base.api.event_streams;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ce.ems.base.classes.ClientResources;
import com.ce.ems.base.classes.ClientResources.ClientRBRef;
import com.ce.ems.base.classes.IndexedNameSpec;
import com.ce.ems.base.classes.KeyValuePair;

public class Sentence implements Serializable {

	private static final long serialVersionUID = 1L;

	// ClientResources.ClientRBRef | SubjectEntity | IndexedNameSpec
	private Object subject;

	private CustomPredicate predicate;
	private ObjectEntity object;

	// V: ClientResources.ClientRBRef | ObjectEntity | SubjectEntity
	private Map<Preposition, Object> prepositions = new HashMap<>();

	// ClientResources.ClientRBRef | ObjectEntityKind
	private KeyValuePair<SubordinatingConjuction, Object> subordinativeClause;

	// ClientResources.ClientRBRef | Sentence
	private KeyValuePair<CoordinatingConjuction, Object> coordinativeClause;

	public static Sentence newInstance() {
		return new Sentence();
	}

	private Sentence() {
	}

	public Object getSubject() {
		return subject;
	}

	public Sentence setSubject(ClientResources.ClientRBRef subject) {
		this.subject = subject;
		return this;
	}

	public Sentence setSubject(SubjectEntity subject) {
		this.subject = subject;
		return this;
	}

	public Sentence setSubject(IndexedNameSpec subject) {
		this.subject = subject;
		return this;
	}

	public CustomPredicate getPredicate() {
		return predicate;
	}

	public Sentence setPredicate(CustomPredicate predicate) {
		this.predicate = predicate;
		return this;
	}

	public ObjectEntity getObject() {
		return object;
	}

	public Sentence setObject(ObjectEntity object) {
		this.object = object;
		return this;
	}

	public Map<Preposition, Object> getPrepositions() {
		return prepositions;
	}

	public Sentence withPreposition(Preposition preposition, ClientResources.ClientRBRef clause) {
		this.prepositions.put(preposition, clause);
		return this;
	}

	public Sentence withPreposition(Preposition preposition, ObjectEntity clause) {
		this.prepositions.put(preposition, clause);
		return this;
	}

	public Sentence withPreposition(Preposition preposition, SubjectEntity clause) {
		this.prepositions.put(preposition, clause);
		return this;
	}

	public KeyValuePair<SubordinatingConjuction, Object> getSubordinativeClause() {
		return subordinativeClause;
	}

	public Sentence setSubordinativeClause(SubordinatingConjuction conjunction, ClientRBRef clause) {
		this.subordinativeClause = new KeyValuePair<SubordinatingConjuction, Object>(conjunction, clause);
		return this;
	}

	public Sentence setSubordinativeClause(SubordinatingConjuction conjunction, ObjectEntity clause) {
		this.subordinativeClause = new KeyValuePair<SubordinatingConjuction, Object>(conjunction, clause);
		return this;
	}

	public KeyValuePair<CoordinatingConjuction, Object> getCoordinativeClause() {
		return coordinativeClause;
	}

	public Sentence setCoordinativeClause(CoordinatingConjuction conjunction, ClientRBRef clause) {
		this.coordinativeClause = new KeyValuePair<CoordinatingConjuction, Object>(conjunction, clause);
		;
		return this;
	}

	public Sentence setCoordinativeClause(CoordinatingConjuction conjunction, Sentence clause) {
		this.coordinativeClause = new KeyValuePair<CoordinatingConjuction, Object>(conjunction, clause);
		;
		return this;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		String separator = ClientResources.HtmlCharacterEnties.SPACE.toString();

		builder.append(subject).append(separator).append(predicate).append(separator).append(object);

		prepositions.forEach((k, v) -> {
			builder.append(separator).append(k).append(separator).append(v);
		});

		if (subordinativeClause != null) {
			builder.append(separator).append(subordinativeClause.getKey()).append(separator)
					.append(subordinativeClause.getValue());
		}

		if (coordinativeClause != null) {
			builder.append(ClientResources.HtmlCharacterEnties.COMMA).append(separator)
					.append(coordinativeClause.getKey()).append(separator).append(coordinativeClause.getValue());
		}

		return builder.toString();
	}

}
