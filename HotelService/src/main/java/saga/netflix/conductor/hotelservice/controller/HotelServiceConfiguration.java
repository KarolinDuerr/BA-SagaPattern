package saga.netflix.conductor.hotelservice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.netflix.conductor.hotelservice.model.HotelBookingRepository;
import saga.netflix.conductor.hotelservice.resources.DtoConverter;

@Configuration
@EnableAutoConfiguration
public class HotelServiceConfiguration {

    @Bean
    public IHotelService hotelService(HotelBookingRepository hotelBookingRepository) {
        return new HotelService(hotelBookingRepository);
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter();
    }
}
