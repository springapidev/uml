/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory.beans.jpa;

import inventory.beans.ClientType;
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class ClientTypeJpaController {

    public ClientTypeJpaController() {
        emf = Persistence.createEntityManagerFactory("InventoryPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClientType clientType) throws PreexistingEntityException, Exception {
        if (clientType.getClientList() == null) {
            clientType.setClientList(new ArrayList<Client>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Client> attachedClientList = new ArrayList<Client>();
            for (Client clientListClientToAttach : clientType.getClientList()) {
                clientListClientToAttach = em.getReference(clientListClientToAttach.getClass(), clientListClientToAttach.getClientId());
                attachedClientList.add(clientListClientToAttach);
            }
            clientType.setClientList(attachedClientList);
            em.persist(clientType);
            for (Client clientListClient : clientType.getClientList()) {
                ClientType oldClientTypeOfClientListClient = clientListClient.getClientType();
                clientListClient.setClientType(clientType);
                clientListClient = em.merge(clientListClient);
                if (oldClientTypeOfClientListClient != null) {
                    oldClientTypeOfClientListClient.getClientList().remove(clientListClient);
                    oldClientTypeOfClientListClient = em.merge(oldClientTypeOfClientListClient);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClientType(clientType.getClientTypeId()) != null) {
                throw new PreexistingEntityException("ClientType " + clientType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClientType clientType) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClientType persistentClientType = em.find(ClientType.class, clientType.getClientTypeId());
            List<Client> clientListOld = persistentClientType.getClientList();
            List<Client> clientListNew = clientType.getClientList();
            List<Client> attachedClientListNew = new ArrayList<Client>();
            for (Client clientListNewClientToAttach : clientListNew) {
                clientListNewClientToAttach = em.getReference(clientListNewClientToAttach.getClass(), clientListNewClientToAttach.getClientId());
                attachedClientListNew.add(clientListNewClientToAttach);
            }
            clientListNew = attachedClientListNew;
            clientType.setClientList(clientListNew);
            clientType = em.merge(clientType);
            for (Client clientListOldClient : clientListOld) {
                if (!clientListNew.contains(clientListOldClient)) {
                    clientListOldClient.setClientType(null);
                    clientListOldClient = em.merge(clientListOldClient);
                }
            }
            for (Client clientListNewClient : clientListNew) {
                if (!clientListOld.contains(clientListNewClient)) {
                    ClientType oldClientTypeOfClientListNewClient = clientListNewClient.getClientType();
                    clientListNewClient.setClientType(clientType);
                    clientListNewClient = em.merge(clientListNewClient);
                    if (oldClientTypeOfClientListNewClient != null && !oldClientTypeOfClientListNewClient.equals(clientType)) {
                        oldClientTypeOfClientListNewClient.getClientList().remove(clientListNewClient);
                        oldClientTypeOfClientListNewClient = em.merge(oldClientTypeOfClientListNewClient);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = clientType.getClientTypeId();
                if (findClientType(id) == null) {
                    throw new NonexistentEntityException("The clientType with id " + id + " no longer exists.");
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
            ClientType clientType;
            try {
                clientType = em.getReference(ClientType.class, id);
                clientType.getClientTypeId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientType with id " + id + " no longer exists.", enfe);
            }
            List<Client> clientList = clientType.getClientList();
            for (Client clientListClient : clientList) {
                clientListClient.setClientType(null);
                clientListClient = em.merge(clientListClient);
            }
            em.remove(clientType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ClientType> findClientTypeEntities() {
        return findClientTypeEntities(true, -1, -1);
    }

    public List<ClientType> findClientTypeEntities(int maxResults, int firstResult) {
        return findClientTypeEntities(false, maxResults, firstResult);
    }

    private List<ClientType> findClientTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClientType.class));
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

    public ClientType findClientType(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClientType.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClientType> rt = cq.from(ClientType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
