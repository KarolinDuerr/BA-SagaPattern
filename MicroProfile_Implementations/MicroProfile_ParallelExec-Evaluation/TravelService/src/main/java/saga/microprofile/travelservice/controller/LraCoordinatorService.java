package saga.microprofile.travelservice.controller;


import saga.microprofile.travelservice.saga.CancelLRARunnable;
import saga.microprofile.travelservice.saga.CloseLRARunnable;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

@ApplicationScoped
@LraCoordinatorServiceImpl
public class LraCoordinatorService implements ILraCoordinatorService {

    private static final Logger logger = Logger.getLogger(LraCoordinatorService.class.toString());

    @Resource(lookup = "concurrent/threadFactory2")
    private ThreadFactory threadFactory;

    @Override
    public void closeActiveLra(String lraId) {
        logger.info("Starting CloseLraRunnable Thread.");
        threadFactory.newThread(new CloseLRARunnable(lraId)).start();
    }

    @Override
    public void cancelActiveLra(String lraId) {
        logger.info("Starting CancelLraRunnable Thread.");
        threadFactory.newThread(new CancelLRARunnable(lraId)).start();
    }
}
