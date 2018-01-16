package si.fri.rso.rsobnb.payments.api.resources;

import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso.rsobnb.payments.Payment;
import si.fri.rso.rsobnb.payments.services.PaymentsBean;
import org.eclipse.microprofile.metrics.annotation.Metered;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import si.fri.rso.rsobnb.payments.api.configuration.RestProperties;


@RequestScoped
@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log
public class PaymentsResource {

    @Inject
    private PaymentsBean paymentBean;

    @Inject
    private RestProperties restProperties;

    @Context
    protected UriInfo uriInfo;

    @GET
    @Metered
    @Log
    public Response getPropertyPayment() {

        List<Payment> payments = paymentBean.getPayment(uriInfo);

        return Response.ok(payments).build();
    }

    @GET
    @Path("/filtered")
    @Log
    public Response getPaymentFiltered() {

        List<Payment> payments;

        payments = paymentBean.getPaymentFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(payments).build();
    }

    @GET
    @Path("/{paymentId}")
    @Log
    public Response getPayment(@PathParam("paymentId") String paymentId) {

        Payment payment = paymentBean.getPayment(paymentId);

        if (payment == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(payment).build();
    }

    @POST
    @Log
    public Response createPayment(Payment payment) {

        if ((payment.getAmount() == null || payment.getDate().isEmpty()) || (payment.getLeaseId() == null
                || payment.getLeaseId().isEmpty())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            payment = paymentBean.createdPayment(payment);
        }

        if (payment.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(payment).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(payment).build();
        }
    }

    @PUT
    @Path("{paymentId}")
    public Response putPayment(@PathParam("paymentId") String paymentId, Payment payment) {

        payment = paymentBean.putPayment(paymentId, payment);

        if (payment == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (payment.getId() != null)
                return Response.status(Response.Status.OK).entity(payment).build();
            else
                return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @DELETE
    @Path("{paymentId}")
    public Response deleteUser(@PathParam("paymentId") String paymentId) {

        boolean deleted = paymentBean.deletePayment(paymentId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @POST
    @Path("healthy")
    public Response setHealth(Boolean healthy) {
        restProperties.setHealthy(healthy);
        return Response.ok().build();
    }

    @GET
    @Path("healthy")
    public Response getHealth() {
        restProperties.isHealthy();
        return Response.ok().entity(restProperties.isHealthy()).build();
    }
}
