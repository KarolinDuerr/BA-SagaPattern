package saga.microprofile.hotelservice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.microprofile.hotelservice.model.HotelBookingRepository;
import saga.microprofile.hotelservice.resources.DtoConverter;

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
