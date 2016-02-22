package com.egaldino.market.search.server.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.egaldino.market.search.server.model.Place;

public class PlaceDAO extends GenericDAO<Place> {

	public PlaceDAO(SessionFactory factory) {
		super(factory);
	}

	public List<Place> list() {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Place.class)
				.addOrder(Order.asc("name"));
		List<Place> list = criteria.list();
		return list;
	}

	public Place get(int id) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Place.class).add(Restrictions.eq("id", id));
		List<Place> list = criteria.list();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

}
