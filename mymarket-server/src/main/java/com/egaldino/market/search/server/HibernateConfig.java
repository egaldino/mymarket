package com.egaldino.market.search.server;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.egaldino.market.search.server.model.Market;
import com.egaldino.market.search.server.model.MarketProduct;
import com.egaldino.market.search.server.model.MarketSuggestion;
import com.egaldino.market.search.server.model.Place;
import com.egaldino.market.search.server.model.Product;

public class HibernateConfig {

	public static final SessionFactory factory = new Configuration()
			.configure().addClass(Market.class)
			.addClass(Place.class)
			.addClass(Product.class)
			.addClass(MarketProduct.class)
			.addClass(MarketSuggestion.class)
			.buildSessionFactory();

	public static void main(String[] args) throws Exception {
		
	}
}
