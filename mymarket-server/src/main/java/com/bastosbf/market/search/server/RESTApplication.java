package com.bastosbf.market.search.server;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.bastosbf.market.search.server.model.Market;
import com.bastosbf.market.search.server.model.MarketProduct;
import com.bastosbf.market.search.server.model.Product;

@ApplicationPath("/")
public class RESTApplication extends ResourceConfig {
}