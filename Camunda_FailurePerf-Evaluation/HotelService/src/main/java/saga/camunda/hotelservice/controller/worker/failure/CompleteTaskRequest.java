package saga.camunda.hotelservice.controller.worker.failure;

import java.io.Serializable;
import java.util.Map;

public class CompleteTaskRequest implements Serializable {

    private String workerId;

    private Map<String, Object> variables;

    public CompleteTaskRequest() {

    }

    public CompleteTaskRequest(final String workerId, final Map<String, Object> variables) {
        this.workerId = workerId;
        this.variables = variables;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "CompleteTaskRequest{" +
                "workerId=" + workerId +
                ", variables='" + variables +
                '}';
    }
}
