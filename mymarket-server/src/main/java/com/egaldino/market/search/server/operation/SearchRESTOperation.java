package com.egaldino.market.search.server.operation;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.egaldino.market.search.server.HibernateConfig;
import com.egaldino.market.search.server.dao.MarketProductDAO;
import com.egaldino.market.search.server.model.MarketProduct;
import com.egaldino.market.search.server.to.Search;

@Path("/search")
public class SearchRESTOperation {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/products")
	public List<Search> products(@QueryParam("market") int market) {
		MarketProductDAO dao = new MarketProductDAO(HibernateConfig.factory);
		List<MarketProduct> results = dao.getByMarket(market);
		List<Search> prices = new ArrayList<Search>();
		for (MarketProduct result : results) {
			Search price = new Search();
			price.setProduct(result.getProduct());
			price.setMarket(result.getMarket());
			price.setPrice(result.getPrice());
			price.setLastUpdate(result.getLastUpdate());

			prices.add(price);
		}
		return prices;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/prices")
	public List<Search> prices(@QueryParam("barcode") String barcode, @QueryParam("place") int place) {
		MarketProductDAO dao = new MarketProductDAO(HibernateConfig.factory);
		List<MarketProduct> results = dao.getByBarcodeAndPlace(barcode, place);
		List<Search> prices = new ArrayList<Search>();
		for (MarketProduct result : results) {
			Search price = new Search();
			price.setProduct(result.getProduct());
			price.setMarket(result.getMarket());
			price.setPrice(result.getPrice());
			price.setLastUpdate(result.getLastUpdate());

			prices.add(price);
		}
		return prices;
	}

}
