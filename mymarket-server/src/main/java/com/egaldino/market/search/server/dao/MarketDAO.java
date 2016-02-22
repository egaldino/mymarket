package com.egaldino.market.search.server.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.egaldino.market.search.server.model.Market;

public class MarketDAO extends GenericDAO<Market> {

	public MarketDAO(SessionFactory factory) {
		super(factory);
	}

	public List<Market> list(int place) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Market.class)
				.createAlias("place", "p")
				.add(Restrictions.eq("p.id", place))
				.addOrder(Order.asc("name"));
		List<Market> list = criteria.list();
		return list;
	}

	public Market get(int id) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Market.class)
				.add(Restrictions.eq("id", id));
		List<Market> list = criteria.list();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
}
