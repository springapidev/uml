/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory.beans.jpa;

import inventory.beans.Client;
import inventory.beans.jpa.exceptions.NonexistentEntityException;
import inventory.beans.jpa.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import inventory.beans.ClientType;
import inventory.beans.Oreder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class ClientJpaController {

    public ClientJpaController() {
        emf = Persistence.createEntityManagerFactory("InventoryPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Client client) throws PreexistingEntityException, Exception {
        if (client.getOrederList() == null) {
            client.setOrederList(new ArrayList<Oreder>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClientType clientType = client.getClientType();
            if (clientType != null) {
                clientType = em.getReference(clientType.getClass(), clientType.getClientTypeId());
                client.setClientType(clientType);
            }
            List<Oreder> attachedOrederList = new ArrayList<Oreder>();
            for (Oreder orederListOrederToAttach : client.getOrederList()) {
                orederListOrederToAttach = em.getReference(orederListOrederToAttach.getClass(), orederListOrederToAttach.getOrderId());
                attachedOrederList.add(orederListOrederToAttach);
            }
            client.setOrederList(attachedOrederList);
            em.persist(client);
            if (clientType != null) {
                clientType.getClientList().add(client);
                clientType = em.merge(clientType);
            }
            for (Oreder orederListOreder : client.getOrederList()) {
                Client oldClientOfOrederListOreder = orederListOreder.getClient();
                orederListOreder.setClient(client);
                orederListOreder = em.merge(orederListOreder);
                if (oldClientOfOrederListOreder != null) {
                    oldClientOfOrederListOreder.getOrederList().remove(orederListOreder);
                    oldClientOfOrederListOreder = em.merge(oldClientOfOrederListOreder);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClient(client.getClientId()) != null) {
                throw new PreexistingEntityException("Client " + client + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Client client) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client persistentClient = em.find(Client.class, client.getClientId());
            ClientType clientTypeOld = persistentClient.getClientType();
            ClientType clientTypeNew = client.getClientType();
            List<Oreder> orederListOld = persistentClient.getOrederList();
            List<Oreder> orederListNew = client.getOrederList();
            if (clientTypeNew != null) {
                clientTypeNew = em.getReference(clientTypeNew.getClass(), clientTypeNew.getClientTypeId());
                client.setClientType(clientTypeNew);
            }
            List<Oreder> attachedOrederListNew = new ArrayList<Oreder>();
            for (Oreder orederListNewOrederToAttach : orederListNew) {
                orederListNewOrederToAttach = em.getReference(orederListNewOrederToAttach.getClass(), orederListNewOrederToAttach.getOrderId());
                attachedOrederListNew.add(orederListNewOrederToAttach);
            }
            orederListNew = attachedOrederListNew;
            client.setOrederList(orederListNew);
            client = em.merge(client);
            if (clientTypeOld != null && !clientTypeOld.equals(clientTypeNew)) {
                clientTypeOld.getClientList().remove(client);
                clientTypeOld = em.merge(clientTypeOld);
            }
            if (clientTypeNew != null && !clientTypeNew.equals(clientTypeOld)) {
                clientTypeNew.getClientList().add(client);
                clientTypeNew = em.merge(clientTypeNew);
            }
            for (Oreder orederListOldOreder : orederListOld) {
                if (!orederListNew.contains(orederListOldOreder)) {
                    orederListOldOreder.setClient(null);
                    orederListOldOreder = em.merge(orederListOldOreder);
                }
            }
            for (Oreder orederListNewOreder : orederListNew) {
                if (!orederListOld.contains(orederListNewOreder)) {
                    Client oldClientOfOrederListNewOreder = orederListNewOreder.getClient();
                    orederListNewOreder.setClient(client);
                    orederListNewOreder = em.merge(orederListNewOreder);
                    if (oldClientOfOrederListNewOreder != null && !oldClientOfOrederListNewOreder.equals(client)) {
                        oldClientOfOrederListNewOreder.getOrederList().remove(orederListNewOreder);
                        oldClientOfOrederListNewOreder = em.merge(oldClientOfOrederListNewOreder);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = client.getClientId();
                if (findClient(id) == null) {
                    throw new NonexistentEntityException("The client with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client client;
            try {
                client = em.getReference(Client.class, id);
                client.getClientId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The client with id " + id + " no longer exists.", enfe);
            }
            ClientType clientType = client.getClientType();
            if (clientType != null) {
                clientType.getClientList().remove(client);
                clientType = em.merge(clientType);
            }
            List<Oreder> orederList = client.getOrederList();
            for (Oreder orederListOreder : orederList) {
                orederListOreder.setClient(null);
                orederListOreder = em.merge(orederListOreder);
            }
            em.remove(client);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Client> findClientEntities() {
        return findClientEntities(true, -1, -1);
    }

    public List<Client> findClientEntities(int maxResults, int firstResult) {
        return findClientEntities(false, maxResults, firstResult);
    }

    private List<Client> findClientEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Client.class));
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

    public Client findClient(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Client> rt = cq.from(Client.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
