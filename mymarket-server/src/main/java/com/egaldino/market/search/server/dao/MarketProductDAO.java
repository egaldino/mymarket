package com.egaldino.market.search.server.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.egaldino.market.search.server.model.MarketProduct;

public class MarketProductDAO extends GenericDAO<MarketProduct> {

	public MarketProductDAO(SessionFactory factory) {
		super(factory);
	}

	public List<MarketProduct> getByMarket(int market) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(MarketProduct.class)
				.createAlias("product", "p")
				.createAlias("market", "m")
				.add(Restrictions.eq("m.id", market))
				.addOrder(Order.asc("p.name"));
		List<MarketProduct> list = criteria.list();		
		return list;
	}
	
	public void updatePrice(int market, String barcode, double price) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(MarketProduct.class)
				.createAlias("product", "p")
				.createAlias("market", "m")
				.add(Restrictions.eq("p.barcode", barcode))
				.add(Restrictions.eq("m.id", market));
		List<MarketProduct> list = criteria.list();
		if(!list.isEmpty()) {
			MarketProduct mp = list.get(0);
			mp.setLastUpdate(new Date());
			mp.setPrice(price);
			update(mp);
		}		
	}
	
	public void confirmPrice(int market, String barcode, Date date) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(MarketProduct.class)
				.createAlias("product", "p")
				.createAlias("market", "m")
				.add(Restrictions.eq("p.barcode", barcode))
				.add(Restrictions.eq("m.id", market));
		List<MarketProduct> list = criteria.list();
		if(!list.isEmpty()) {
			MarketProduct mp = list.get(0);
			mp.setLastUpdate(date);
			update(mp);
		}		
	}
	
	public List<MarketProduct> getByBarcodeAndPlace(String barcode, int place, int maxResults) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(MarketProduct.class)
				.createAlias("product", "p")
				.createAlias("market", "m")
				.add(Restrictions.eq("p.barcode", barcode))
				.add(Restrictions.eq("m.place.id", place))
				.addOrder(Order.asc("price"));
		criteria.setMaxResults(maxResults);
		List<MarketProduct> list = criteria.list();		
		return list;
	}
	
	public List<MarketProduct> getByBarcodeAndPlace(String barcode, int place) {
		return getByBarcodeAndPlace(barcode, place, -1);
	}
}
