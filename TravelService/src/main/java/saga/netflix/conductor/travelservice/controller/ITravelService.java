package saga.netflix.conductor.travelservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import saga.netflix.conductor.travelservice.error.TravelException;
import saga.netflix.conductor.travelservice.model.TripInformation;

import java.util.List;

@Qualifier("TravelService")
public interface ITravelService {

    List<TripInformation> getTripsInformation();

    TripInformation getTripInformation(final Long tripId) throws TravelException;

    TripInformation bookTrip(final TripInformation tripInformation);

}
