package saga.eventuate.tram.hotelservice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.eventuate.tram.hotelservice.model.HotelBookingRepository;

@Configuration
@EnableAutoConfiguration
public class HotelServiceConfiguration {

    @Bean
    public IHotelService hotelService(HotelBookingRepository hotelBookingRepository) {
        return new HotelService(hotelBookingRepository);
    }
}
