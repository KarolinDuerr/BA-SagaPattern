package saga.camunda.hotelservice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.camunda.hotelservice.resources.DtoConverter;
import saga.camunda.hotelservice.model.HotelBookingRepository;

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
