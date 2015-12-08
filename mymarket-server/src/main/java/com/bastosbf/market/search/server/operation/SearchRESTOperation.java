package com.bastosbf.market.search.server.operation;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.bastosbf.market.search.server.HibernateConfig;
import com.bastosbf.market.search.server.dao.MarketProductDAO;
import com.bastosbf.market.search.server.model.MarketProduct;
import com.bastosbf.market.search.server.to.SearchTO;

@Path("/search")
public class SearchRESTOperation {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/products")
	public List<SearchTO> products(@QueryParam("market") int market) {
		MarketProductDAO dao = new MarketProductDAO(HibernateConfig.factory);
		List<MarketProduct> results = dao.getByMarket(market);
		List<SearchTO> prices = new ArrayList<SearchTO>();
		for (MarketProduct result : results) {
			SearchTO price = new SearchTO();
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
	public List<SearchTO> prices(@QueryParam("barcode") String barcode, @QueryParam("place") int place) {
		MarketProductDAO dao = new MarketProductDAO(HibernateConfig.factory);
		List<MarketProduct> results = dao.getByBarcodeAndPlace(barcode, place, 3);
		List<SearchTO> prices = new ArrayList<SearchTO>();
		for (MarketProduct result : results) {
			SearchTO price = new SearchTO();
			price.setProduct(result.getProduct());
			price.setMarket(result.getMarket());
			price.setPrice(result.getPrice());
			price.setLastUpdate(result.getLastUpdate());

			prices.add(price);
		}
		return prices;
	}

}
