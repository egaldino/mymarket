package com.egaldino.market.search.server.dao;

import org.hibernate.SessionFactory;

import com.egaldino.market.search.server.model.MarketSuggestion;

public class MarketSuggestionDAO extends GenericDAO<MarketSuggestion> {

	public MarketSuggestionDAO(SessionFactory factory) {
		super(factory);
	}
}
