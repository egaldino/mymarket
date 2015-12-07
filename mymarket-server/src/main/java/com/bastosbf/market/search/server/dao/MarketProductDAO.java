package com.bastosbf.market.search.server.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.bastosbf.market.search.server.model.MarketProduct;

public class MarketProductDAO extends GenericDAO<MarketProduct> {

	public MarketProductDAO(SessionFactory factory) {
		super(factory);
	}

	public MarketProduct getCheapestById(String barcode) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(MarketProduct.class)
				.add(Restrictions.eq("product.barcode", barcode))
				.addOrder(Order.asc("price"));
		criteria.setMaxResults(1);
		List<MarketProduct> list = criteria.list();
		session.close();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
}
