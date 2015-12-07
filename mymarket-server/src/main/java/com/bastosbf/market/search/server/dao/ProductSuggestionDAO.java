package com.bastosbf.market.search.server.dao;

import org.hibernate.SessionFactory;

import com.bastosbf.market.search.server.model.ProductSuggestion;

public class ProductSuggestionDAO extends GenericDAO<ProductSuggestion> {

	public ProductSuggestionDAO(SessionFactory factory) {
		super(factory);
	}
}
