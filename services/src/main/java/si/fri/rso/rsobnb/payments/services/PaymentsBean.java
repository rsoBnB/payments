package si.fri.rso.rsobnb.payments.services;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.rsobnb.payments.Payment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;


@ApplicationScoped
public class PaymentsBean {

    private Logger log = Logger.getLogger(PaymentsBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Payment> getPayment(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, Payment.class, queryParameters);

    }

    public List<Payment> getPaymentFilter(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0).build();
        return JPAUtils.queryEntities(em, Payment.class, queryParameters);
    }

    public Payment getPayment(String paymentId) {


        Payment payment = em.find(Payment.class, paymentId);

        if (payment == null) {
            throw new NotFoundException();
        }

        return payment;
    }


    public Payment createdPayment(Payment payment) {

        try {
            beginTx();
            em.persist(payment);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return payment;
    }

    public Payment putPayment(String paymentId, Payment payment) {

        Payment r = em.find(Payment.class, paymentId);

        if (r == null) {
            return null;
        }

        try {
            beginTx();
            payment.setId(r.getId());
            payment = em.merge(payment);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return payment;
    }

    public boolean deletePayment(String paymentId) {

        Payment payment = em.find(Payment.class, paymentId);

        if (payment != null) {
            try {
                beginTx();
                em.remove(payment);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
