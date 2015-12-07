package com.bastosbf.market.search.server.operation;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.bastosbf.market.search.server.HibernateConfig;
import com.bastosbf.market.search.server.dao.ProductDAO;
import com.bastosbf.market.search.server.model.Product;

@Path("/product")
public class ProductRESTOperation {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/add")
	public void add(@QueryParam("market") int market,
			@QueryParam("name") String name, @QueryParam("brand") String brand,
			@QueryParam("price") double price) {

	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list")
	public List<Product> list(@QueryParam("market") int market) {
		ProductDAO dao = new ProductDAO(HibernateConfig.factory);
		return dao.list(market);
	}

}
