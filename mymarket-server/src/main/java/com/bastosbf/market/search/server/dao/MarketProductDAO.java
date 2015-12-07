package com.bastosbf.market.search.server.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.bastosbf.market.search.server.model.MarketProduct;
import com.bastosbf.market.search.server.model.Product;

public class MarketProductDAO extends GenericDAO<MarketProduct> {

	public MarketProductDAO(SessionFactory factory) {
		super(factory);
	}

	public Product getCheapestById(String barcode) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(MarketProduct.class).add(Restrictions.eq("product.barcode", barcode)).addOrder(Order.asc("price"));
		criteria.setMaxResults(1);
		List<MarketProduct> list = criteria.list();
		if (!list.isEmpty()) {
			Product product = list.get(0).getProduct();
			return product;
		}
		return null;
	}
}
