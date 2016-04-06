package com.egaldino.market.search.server.operation;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.egaldino.market.search.server.HibernateConfig;
import com.egaldino.market.search.server.dao.MarketDAO;
import com.egaldino.market.search.server.dao.MarketProductDAO;
import com.egaldino.market.search.server.dao.MarketSuggestionDAO;
import com.egaldino.market.search.server.dao.ProductDAO;
import com.egaldino.market.search.server.model.Market;
import com.egaldino.market.search.server.model.MarketProduct;
import com.egaldino.market.search.server.model.MarketSuggestion;
import com.egaldino.market.search.server.model.Product;

@Path("/collaboration")
public class CollaborationRESTOperation {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/suggest-market")
	public void suggestMarket(@QueryParam("name") String name,
			@QueryParam("address") String address) {
		MarketSuggestion suggestion = new MarketSuggestion();
		suggestion.setName(name);
		suggestion.setAddress(address);
		MarketSuggestionDAO dao = new MarketSuggestionDAO(
				HibernateConfig.factory);
		dao.add(suggestion);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/suggest-product")
	public void suggestProduct(@QueryParam("market") int market,
			@QueryParam("barcode") String barcode,
			@QueryParam("name") String name,
			@QueryParam("price") double price) {
		Market m = null;
		{
			MarketDAO dao = new MarketDAO(HibernateConfig.factory);
			m = dao.get(market);
		}
		if (m != null) {
			Product product = null;
			{
				ProductDAO dao = new ProductDAO(HibernateConfig.factory);
				product = dao.get(barcode);
			}
			if (product == null) {
				{
					product = new Product();
					product.setBarcode(barcode);
					product.setName(name);
					
					ProductDAO dao = new ProductDAO(HibernateConfig.factory);
					dao.add(product);
				}
				{
					MarketProduct mp = new MarketProduct();
					mp.setMarket(m);
					mp.setProduct(product);
					mp.setLastUpdate(new Date());
					mp.setPrice(price);
					
					MarketProductDAO dao = new MarketProductDAO(
							HibernateConfig.factory);
					dao.add(mp);
				}
			}
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/suggest-price")
	public void suggestPrice(@QueryParam("market") int market,
			@QueryParam("product") String product,
			@QueryParam("price") double price) {
		Market m = null;
		{
			MarketDAO dao = new MarketDAO(HibernateConfig.factory);
			m = dao.get(market);
		}
		if (m != null) {
			Product p = null;
			{
				ProductDAO dao = new ProductDAO(HibernateConfig.factory);
				p = dao.get(product);
			}
			if (p != null) {
				MarketProductDAO dao = new MarketProductDAO(
						HibernateConfig.factory);
				dao.updatePrice(market, product, price);
			}
		}

	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/confirm-price")
	public void confirmPrice(@QueryParam("market") int market, @QueryParam("barcode") String product) {
		Market m = null;
		{
			MarketDAO dao = new MarketDAO(HibernateConfig.factory);
			m = dao.get(market);
		}
		if (m != null) {
			Product p = null;
			{
				ProductDAO dao = new ProductDAO(HibernateConfig.factory);
				p = dao.get(product);
			}
			if (p != null) {
				MarketProductDAO dao = new MarketProductDAO(
						HibernateConfig.factory);
				dao.confirmPrice(market, product, new Date());
			}
		}
	}

}
