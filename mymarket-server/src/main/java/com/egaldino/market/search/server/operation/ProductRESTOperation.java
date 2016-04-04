package com.egaldino.market.search.server.operation;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.egaldino.market.search.server.HibernateConfig;
import com.egaldino.market.search.server.dao.MarketProductDAO;
import com.egaldino.market.search.server.dao.ProductDAO;
import com.egaldino.market.search.server.model.Product;

@Path("/product")
public class ProductRESTOperation {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list-by-market")
	public List<Product> listByMarket(@QueryParam("market") int market) {
		ProductDAO dao = new ProductDAO(HibernateConfig.factory);
		return dao.listByMarket(market);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list-by-place")
	public List<Product> listByPlace(@QueryParam("place") int place) {
		ProductDAO dao = new ProductDAO(HibernateConfig.factory);
		return dao.listByPlace(place);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/get")
	public Product get(@QueryParam("barcode") String barcode) {
		ProductDAO dao = new ProductDAO(HibernateConfig.factory);
		return dao.get(barcode);
	}
}
