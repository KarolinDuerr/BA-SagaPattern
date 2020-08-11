package saga.eventuate.tram.travelservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import saga.eventuate.tram.travelservice.error.TravelException;
import saga.eventuate.tram.travelservice.model.TripInformation;

import java.util.List;

@Qualifier("TravelService")
public interface ITravelService {

    List<TripInformation> getTripsInformation();

    TripInformation getTripInformation(Long tripId) throws TravelException;

    TripInformation bookTrip(TripInformation tripInformation);

    boolean cancelTrip(Long tripId) throws TravelException;

}
