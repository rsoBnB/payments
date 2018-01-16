package si.fri.rso.rsobnb.payments.api.health;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import si.fri.rso.rsobnb.payments.api.configuration.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@Health
@ApplicationScoped
public class PaymentServiceHealthCheck implements HealthCheck{

    @Inject
    private RestProperties restProperties;

    private Logger log = Logger.getLogger(si.fri.rso.rsobnb.payments.api.health.PaymentServiceHealthCheck.class.getName());

    @Override
    public HealthCheckResponse call() {

        if (restProperties.isHealthy()) {
            return HealthCheckResponse.named(si.fri.rso.rsobnb.payments.api.health.PaymentServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(si.fri.rso.rsobnb.payments.api.health.PaymentServiceHealthCheck.class.getSimpleName()).down().build();
        }

    }
}
