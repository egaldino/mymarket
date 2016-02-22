package com.egaldino.market.search.server;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.egaldino.market.search.server.model.Market;
import com.egaldino.market.search.server.model.MarketProduct;
import com.egaldino.market.search.server.model.Product;

@ApplicationPath("/")
public class RESTApplication extends ResourceConfig {
}