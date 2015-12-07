package com.bastosbf.market.search.server.dao;

import org.hibernate.SessionFactory;

import com.bastosbf.market.search.server.model.PriceSuggestion;

public class PriceSuggestionDAO extends GenericDAO<PriceSuggestion> {

	public PriceSuggestionDAO(SessionFactory factory) {
		super(factory);
	}
}
