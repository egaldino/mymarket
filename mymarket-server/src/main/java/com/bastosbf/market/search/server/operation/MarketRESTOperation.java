package com.bastosbf.market.search.server.operation;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bastosbf.market.search.server.HibernateConfig;
import com.bastosbf.market.search.server.dao.MarketDAO;
import com.bastosbf.market.search.server.model.Market;

@Path("/market")
public class MarketRESTOperation {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list")
	public List<Market> list() {
		MarketDAO dao = new MarketDAO(HibernateConfig.factory);
		return dao.list();
	}

}
