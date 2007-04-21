package org.crank.crud.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Group extends Criterion {
	private List<Criterion> criteria = new ArrayList<Criterion>();
	private Junction junction = Junction.AND;
	
	public Group () {
		
	}
	
	public Group (final Junction aJunction, final Criterion... aCriteria) {
		this.junction = aJunction;
		this.criteria.addAll(Arrays.asList(aCriteria));
	}

	public Group add (Criterion criterion) {
		criteria.add(criterion);
		return this;
	}

	public Group add (String name, Operator operator, Object value) {
		criteria.add(new Comparison(name, operator, value));
		return this;
	}

	public Group eq (String name, Object value) {
		criteria.add(new Comparison(name, Operator.EQ, value));
		return this;
	}
	public Group ne (String name, Object value) {
		criteria.add(new Comparison(name, Operator.NE, value));
		return this;
	}
	public Group gt (String name, Object value) {
		criteria.add(new Comparison(name, Operator.GT, value));
		return this;
	}
	public Group lt (String name, Object value) {
		criteria.add(new Comparison(name, Operator.LT, value));
		return this;
	}
	public Group ge (String name, Object value) {
		criteria.add(new Comparison(name, Operator.GE, value));
		return this;
	}
	public Group le (String name, Object value) {
		criteria.add(new Comparison(name, Operator.LE, value));
		return this;
	}
	
	public static Group and () {
		Group group =  new Group(); group.junction = Junction.AND;
		return group;
	}

	public static Group or () {
		Group group =  new Group(); group.junction = Junction.OR;
		return group;
	}
	
	public static Group and (final Criterion... criteria) {
		return  new Group(Junction.AND, criteria);
	}

	public static Group or (final Criterion... criteria) {
		return  new Group(Junction.OR, criteria);
	}
	
	public String toString () {
		return "(" + junction + " " + criteria + ")";
	}
	
}
	