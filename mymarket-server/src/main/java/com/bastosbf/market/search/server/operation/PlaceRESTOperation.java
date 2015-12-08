package com.bastosbf.market.search.server.operation;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.bastosbf.market.search.server.HibernateConfig;
import com.bastosbf.market.search.server.dao.PlaceDAO;
import com.bastosbf.market.search.server.model.Place;

@Path("/place")
public class PlaceRESTOperation {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list")
	public List<Place> list() {
		PlaceDAO dao = new PlaceDAO(HibernateConfig.factory);
		return dao.list();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/get")
	public Place get(@QueryParam("place") int place) {
		PlaceDAO dao = new PlaceDAO(HibernateConfig.factory);
		return dao.get(place);
	}

}
