package saga.camunda.travelservice.saga.failure;

import java.io.Serializable;
import java.util.Map;

public class StartSagaRequest implements Serializable {

    private Map<String, Object> variables;

    private String businessKey;

    public StartSagaRequest() {

    }

    public StartSagaRequest(final String businessKey, final Map<String, Object> variables) {
        this.businessKey = businessKey;
        this.variables = variables;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "StartSagaRequest{" +
                "businessKey=" + businessKey +
                ", variables='" + variables +
                '}';
    }
}
