/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory.beans.jpa;

import inventory.beans.Oreder;
import inventory.beans.jpa.exceptions.NonexistentEntityException;
import inventory.beans.jpa.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import inventory.beans.Client;
import inventory.beans.OrderLine;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class OrederJpaController {

    public OrederJpaController() {
        emf = Persistence.createEntityManagerFactory("InventoryPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Oreder oreder) throws PreexistingEntityException, Exception {
        if (oreder.getOrderLineList() == null) {
            oreder.setOrderLineList(new ArrayList<OrderLine>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client client = oreder.getClient();
            if (client != null) {
                client = em.getReference(client.getClass(), client.getClientId());
                oreder.setClient(client);
            }
            List<OrderLine> attachedOrderLineList = new ArrayList<OrderLine>();
            for (OrderLine orderLineListOrderLineToAttach : oreder.getOrderLineList()) {
                orderLineListOrderLineToAttach = em.getReference(orderLineListOrderLineToAttach.getClass(), orderLineListOrderLineToAttach.getOrderlineid());
                attachedOrderLineList.add(orderLineListOrderLineToAttach);
            }
            oreder.setOrderLineList(attachedOrderLineList);
            em.persist(oreder);
            if (client != null) {
                client.getOrederList().add(oreder);
                client = em.merge(client);
            }
            for (OrderLine orderLineListOrderLine : oreder.getOrderLineList()) {
                Oreder oldOrederOfOrderLineListOrderLine = orderLineListOrderLine.getOreder();
                orderLineListOrderLine.setOreder(oreder);
                orderLineListOrderLine = em.merge(orderLineListOrderLine);
                if (oldOrederOfOrderLineListOrderLine != null) {
                    oldOrederOfOrderLineListOrderLine.getOrderLineList().remove(orderLineListOrderLine);
                    oldOrederOfOrderLineListOrderLine = em.merge(oldOrederOfOrderLineListOrderLine);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOreder(oreder.getOrderId()) != null) {
                throw new PreexistingEntityException("Oreder " + oreder + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Oreder oreder) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Oreder persistentOreder = em.find(Oreder.class, oreder.getOrderId());
            Client clientOld = persistentOreder.getClient();
            Client clientNew = oreder.getClient();
            List<OrderLine> orderLineListOld = persistentOreder.getOrderLineList();
            List<OrderLine> orderLineListNew = oreder.getOrderLineList();
            if (clientNew != null) {
                clientNew = em.getReference(clientNew.getClass(), clientNew.getClientId());
                oreder.setClient(clientNew);
            }
            List<OrderLine> attachedOrderLineListNew = new ArrayList<OrderLine>();
            for (OrderLine orderLineListNewOrderLineToAttach : orderLineListNew) {
                orderLineListNewOrderLineToAttach = em.getReference(orderLineListNewOrderLineToAttach.getClass(), orderLineListNewOrderLineToAttach.getOrderlineid());
                attachedOrderLineListNew.add(orderLineListNewOrderLineToAttach);
            }
            orderLineListNew = attachedOrderLineListNew;
            oreder.setOrderLineList(orderLineListNew);
            oreder = em.merge(oreder);
            if (clientOld != null && !clientOld.equals(clientNew)) {
                clientOld.getOrederList().remove(oreder);
                clientOld = em.merge(clientOld);
            }
            if (clientNew != null && !clientNew.equals(clientOld)) {
                clientNew.getOrederList().add(oreder);
                clientNew = em.merge(clientNew);
            }
            for (OrderLine orderLineListOldOrderLine : orderLineListOld) {
                if (!orderLineListNew.contains(orderLineListOldOrderLine)) {
                    orderLineListOldOrderLine.setOreder(null);
                    orderLineListOldOrderLine = em.merge(orderLineListOldOrderLine);
                }
            }
            for (OrderLine orderLineListNewOrderLine : orderLineListNew) {
                if (!orderLineListOld.contains(orderLineListNewOrderLine)) {
                    Oreder oldOrederOfOrderLineListNewOrderLine = orderLineListNewOrderLine.getOreder();
                    orderLineListNewOrderLine.setOreder(oreder);
                    orderLineListNewOrderLine = em.merge(orderLineListNewOrderLine);
                    if (oldOrederOfOrderLineListNewOrderLine != null && !oldOrederOfOrderLineListNewOrderLine.equals(oreder)) {
                        oldOrederOfOrderLineListNewOrderLine.getOrderLineList().remove(orderLineListNewOrderLine);
                        oldOrederOfOrderLineListNewOrderLine = em.merge(oldOrederOfOrderLineListNewOrderLine);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = oreder.getOrderId();
                if (findOreder(id) == null) {
                    throw new NonexistentEntityException("The oreder with id " + id + " no longer exists.");
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
            Oreder oreder;
            try {
                oreder = em.getReference(Oreder.class, id);
                oreder.getOrderId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The oreder with id " + id + " no longer exists.", enfe);
            }
            Client client = oreder.getClient();
            if (client != null) {
                client.getOrederList().remove(oreder);
                client = em.merge(client);
            }
            List<OrderLine> orderLineList = oreder.getOrderLineList();
            for (OrderLine orderLineListOrderLine : orderLineList) {
                orderLineListOrderLine.setOreder(null);
                orderLineListOrderLine = em.merge(orderLineListOrderLine);
            }
            em.remove(oreder);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Oreder> findOrederEntities() {
        return findOrederEntities(true, -1, -1);
    }

    public List<Oreder> findOrederEntities(int maxResults, int firstResult) {
        return findOrederEntities(false, maxResults, firstResult);
    }

    private List<Oreder> findOrederEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Oreder.class));
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

    public Oreder findOreder(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Oreder.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrederCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Oreder> rt = cq.from(Oreder.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
