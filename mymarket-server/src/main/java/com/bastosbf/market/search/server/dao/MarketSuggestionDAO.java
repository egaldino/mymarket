package com.bastosbf.market.search.server.dao;

import org.hibernate.SessionFactory;

import com.bastosbf.market.search.server.model.MarketSuggestion;

public class MarketSuggestionDAO extends GenericDAO<MarketSuggestion> {

	public MarketSuggestionDAO(SessionFactory factory) {
		super(factory);
	}
}
