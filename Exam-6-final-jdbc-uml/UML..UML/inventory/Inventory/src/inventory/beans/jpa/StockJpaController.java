/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory.beans.jpa;

import inventory.beans.Stock;
import inventory.beans.jpa.exceptions.NonexistentEntityException;
import inventory.beans.jpa.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import inventory.beans.Product;
import inventory.beans.OrderLine;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class StockJpaController {

    public StockJpaController() {
        emf = Persistence.createEntityManagerFactory("InventoryPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Stock stock) throws PreexistingEntityException, Exception {
        if (stock.getOrderLineList() == null) {
            stock.setOrderLineList(new ArrayList<OrderLine>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Product product = stock.getProduct();
            if (product != null) {
                product = em.getReference(product.getClass(), product.getProductId());
                stock.setProduct(product);
            }
            List<OrderLine> attachedOrderLineList = new ArrayList<OrderLine>();
            for (OrderLine orderLineListOrderLineToAttach : stock.getOrderLineList()) {
                orderLineListOrderLineToAttach = em.getReference(orderLineListOrderLineToAttach.getClass(), orderLineListOrderLineToAttach.getOrderlineid());
                attachedOrderLineList.add(orderLineListOrderLineToAttach);
            }
            stock.setOrderLineList(attachedOrderLineList);
            em.persist(stock);
            if (product != null) {
                product.getStockList().add(stock);
                product = em.merge(product);
            }
            for (OrderLine orderLineListOrderLine : stock.getOrderLineList()) {
                Stock oldStockOfOrderLineListOrderLine = orderLineListOrderLine.getStock();
                orderLineListOrderLine.setStock(stock);
                orderLineListOrderLine = em.merge(orderLineListOrderLine);
                if (oldStockOfOrderLineListOrderLine != null) {
                    oldStockOfOrderLineListOrderLine.getOrderLineList().remove(orderLineListOrderLine);
                    oldStockOfOrderLineListOrderLine = em.merge(oldStockOfOrderLineListOrderLine);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStock(stock.getStockId()) != null) {
                throw new PreexistingEntityException("Stock " + stock + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Stock stock) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Stock persistentStock = em.find(Stock.class, stock.getStockId());
            Product productOld = persistentStock.getProduct();
            Product productNew = stock.getProduct();
            List<OrderLine> orderLineListOld = persistentStock.getOrderLineList();
            List<OrderLine> orderLineListNew = stock.getOrderLineList();
            if (productNew != null) {
                productNew = em.getReference(productNew.getClass(), productNew.getProductId());
                stock.setProduct(productNew);
            }
            List<OrderLine> attachedOrderLineListNew = new ArrayList<OrderLine>();
            for (OrderLine orderLineListNewOrderLineToAttach : orderLineListNew) {
                orderLineListNewOrderLineToAttach = em.getReference(orderLineListNewOrderLineToAttach.getClass(), orderLineListNewOrderLineToAttach.getOrderlineid());
                attachedOrderLineListNew.add(orderLineListNewOrderLineToAttach);
            }
            orderLineListNew = attachedOrderLineListNew;
            stock.setOrderLineList(orderLineListNew);
            stock = em.merge(stock);
            if (productOld != null && !productOld.equals(productNew)) {
                productOld.getStockList().remove(stock);
                productOld = em.merge(productOld);
            }
            if (productNew != null && !productNew.equals(productOld)) {
                productNew.getStockList().add(stock);
                productNew = em.merge(productNew);
            }
            for (OrderLine orderLineListOldOrderLine : orderLineListOld) {
                if (!orderLineListNew.contains(orderLineListOldOrderLine)) {
                    orderLineListOldOrderLine.setStock(null);
                    orderLineListOldOrderLine = em.merge(orderLineListOldOrderLine);
                }
            }
            for (OrderLine orderLineListNewOrderLine : orderLineListNew) {
                if (!orderLineListOld.contains(orderLineListNewOrderLine)) {
                    Stock oldStockOfOrderLineListNewOrderLine = orderLineListNewOrderLine.getStock();
                    orderLineListNewOrderLine.setStock(stock);
                    orderLineListNewOrderLine = em.merge(orderLineListNewOrderLine);
                    if (oldStockOfOrderLineListNewOrderLine != null && !oldStockOfOrderLineListNewOrderLine.equals(stock)) {
                        oldStockOfOrderLineListNewOrderLine.getOrderLineList().remove(orderLineListNewOrderLine);
                        oldStockOfOrderLineListNewOrderLine = em.merge(oldStockOfOrderLineListNewOrderLine);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = stock.getStockId();
                if (findStock(id) == null) {
                    throw new NonexistentEntityException("The stock with id " + id + " no longer exists.");
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
            Stock stock;
            try {
                stock = em.getReference(Stock.class, id);
                stock.getStockId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stock with id " + id + " no longer exists.", enfe);
            }
            Product product = stock.getProduct();
            if (product != null) {
                product.getStockList().remove(stock);
                product = em.merge(product);
            }
            List<OrderLine> orderLineList = stock.getOrderLineList();
            for (OrderLine orderLineListOrderLine : orderLineList) {
                orderLineListOrderLine.setStock(null);
                orderLineListOrderLine = em.merge(orderLineListOrderLine);
            }
            em.remove(stock);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Stock> findStockEntities() {
        return findStockEntities(true, -1, -1);
    }

    public List<Stock> findStockEntities(int maxResults, int firstResult) {
        return findStockEntities(false, maxResults, firstResult);
    }

    private List<Stock> findStockEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Stock.class));
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

    public Stock findStock(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Stock.class, id);
        } finally {
            em.close();
        }
    }

    public int getStockCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Stock> rt = cq.from(Stock.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
