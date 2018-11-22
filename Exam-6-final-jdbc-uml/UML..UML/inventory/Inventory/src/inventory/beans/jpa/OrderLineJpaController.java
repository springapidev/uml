/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory.beans.jpa;

import inventory.beans.OrderLine;
import inventory.beans.jpa.exceptions.NonexistentEntityException;
import inventory.beans.jpa.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import inventory.beans.Oreder;
import inventory.beans.Stock;

/**
 *
 * @author user
 */
public class OrderLineJpaController {

    public OrderLineJpaController() {
        emf = Persistence.createEntityManagerFactory("InventoryPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(OrderLine orderLine) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Oreder oreder = orderLine.getOreder();
            if (oreder != null) {
                oreder = em.getReference(oreder.getClass(), oreder.getOrderId());
                orderLine.setOreder(oreder);
            }
            Stock stock = orderLine.getStock();
            if (stock != null) {
                stock = em.getReference(stock.getClass(), stock.getStockId());
                orderLine.setStock(stock);
            }
            em.persist(orderLine);
            if (oreder != null) {
                oreder.getOrderLineList().add(orderLine);
                oreder = em.merge(oreder);
            }
            if (stock != null) {
                stock.getOrderLineList().add(orderLine);
                stock = em.merge(stock);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOrderLine(orderLine.getOrderlineid()) != null) {
                throw new PreexistingEntityException("OrderLine " + orderLine + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OrderLine orderLine) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrderLine persistentOrderLine = em.find(OrderLine.class, orderLine.getOrderlineid());
            Oreder orederOld = persistentOrderLine.getOreder();
            Oreder orederNew = orderLine.getOreder();
            Stock stockOld = persistentOrderLine.getStock();
            Stock stockNew = orderLine.getStock();
            if (orederNew != null) {
                orederNew = em.getReference(orederNew.getClass(), orederNew.getOrderId());
                orderLine.setOreder(orederNew);
            }
            if (stockNew != null) {
                stockNew = em.getReference(stockNew.getClass(), stockNew.getStockId());
                orderLine.setStock(stockNew);
            }
            orderLine = em.merge(orderLine);
            if (orederOld != null && !orederOld.equals(orederNew)) {
                orederOld.getOrderLineList().remove(orderLine);
                orederOld = em.merge(orederOld);
            }
            if (orederNew != null && !orederNew.equals(orederOld)) {
                orederNew.getOrderLineList().add(orderLine);
                orederNew = em.merge(orederNew);
            }
            if (stockOld != null && !stockOld.equals(stockNew)) {
                stockOld.getOrderLineList().remove(orderLine);
                stockOld = em.merge(stockOld);
            }
            if (stockNew != null && !stockNew.equals(stockOld)) {
                stockNew.getOrderLineList().add(orderLine);
                stockNew = em.merge(stockNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = orderLine.getOrderlineid();
                if (findOrderLine(id) == null) {
                    throw new NonexistentEntityException("The orderLine with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrderLine orderLine;
            try {
                orderLine = em.getReference(OrderLine.class, id);
                orderLine.getOrderlineid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orderLine with id " + id + " no longer exists.", enfe);
            }
            Oreder oreder = orderLine.getOreder();
            if (oreder != null) {
                oreder.getOrderLineList().remove(orderLine);
                oreder = em.merge(oreder);
            }
            Stock stock = orderLine.getStock();
            if (stock != null) {
                stock.getOrderLineList().remove(orderLine);
                stock = em.merge(stock);
            }
            em.remove(orderLine);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<OrderLine> findOrderLineEntities() {
        return findOrderLineEntities(true, -1, -1);
    }

    public List<OrderLine> findOrderLineEntities(int maxResults, int firstResult) {
        return findOrderLineEntities(false, maxResults, firstResult);
    }

    private List<OrderLine> findOrderLineEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OrderLine.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public OrderLine findOrderLine(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OrderLine.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrderLineCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OrderLine> rt = cq.from(OrderLine.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
