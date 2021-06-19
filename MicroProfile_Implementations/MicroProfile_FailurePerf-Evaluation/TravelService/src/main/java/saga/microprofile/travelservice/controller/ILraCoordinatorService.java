package saga.microprofile.travelservice.controller;

public interface ILraCoordinatorService {

    void closeActiveLra(final String lraId);
}
