/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory.beans.jpa;

import inventory.beans.Product;
import inventory.beans.jpa.exceptions.NonexistentEntityException;
import inventory.beans.jpa.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import inventory.beans.Stock;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class ProductJpaController {

    public ProductJpaController() {
        emf = Persistence.createEntityManagerFactory("InventoryPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Product product) throws PreexistingEntityException, Exception {
        if (product.getStockList() == null) {
            product.setStockList(new ArrayList<Stock>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Stock> attachedStockList = new ArrayList<Stock>();
            for (Stock stockListStockToAttach : product.getStockList()) {
                stockListStockToAttach = em.getReference(stockListStockToAttach.getClass(), stockListStockToAttach.getStockId());
                attachedStockList.add(stockListStockToAttach);
            }
            product.setStockList(attachedStockList);
            em.persist(product);
            for (Stock stockListStock : product.getStockList()) {
                Product oldProductOfStockListStock = stockListStock.getProduct();
                stockListStock.setProduct(product);
                stockListStock = em.merge(stockListStock);
                if (oldProductOfStockListStock != null) {
                    oldProductOfStockListStock.getStockList().remove(stockListStock);
                    oldProductOfStockListStock = em.merge(oldProductOfStockListStock);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProduct(product.getProductId()) != null) {
                throw new PreexistingEntityException("Product " + product + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Product product) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Product persistentProduct = em.find(Product.class, product.getProductId());
            List<Stock> stockListOld = persistentProduct.getStockList();
            List<Stock> stockListNew = product.getStockList();
            List<Stock> attachedStockListNew = new ArrayList<Stock>();
            for (Stock stockListNewStockToAttach : stockListNew) {
                stockListNewStockToAttach = em.getReference(stockListNewStockToAttach.getClass(), stockListNewStockToAttach.getStockId());
                attachedStockListNew.add(stockListNewStockToAttach);
            }
            stockListNew = attachedStockListNew;
            product.setStockList(stockListNew);
            product = em.merge(product);
            for (Stock stockListOldStock : stockListOld) {
                if (!stockListNew.contains(stockListOldStock)) {
                    stockListOldStock.setProduct(null);
                    stockListOldStock = em.merge(stockListOldStock);
                }
            }
            for (Stock stockListNewStock : stockListNew) {
                if (!stockListOld.contains(stockListNewStock)) {
                    Product oldProductOfStockListNewStock = stockListNewStock.getProduct();
                    stockListNewStock.setProduct(product);
                    stockListNewStock = em.merge(stockListNewStock);
                    if (oldProductOfStockListNewStock != null && !oldProductOfStockListNewStock.equals(product)) {
                        oldProductOfStockListNewStock.getStockList().remove(stockListNewStock);
                        oldProductOfStockListNewStock = em.merge(oldProductOfStockListNewStock);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = product.getProductId();
                if (findProduct(id) == null) {
                    throw new NonexistentEntityException("The product with id " + id + " no longer exists.");
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
            Product product;
            try {
                product = em.getReference(Product.class, id);
                product.getProductId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The product with id " + id + " no longer exists.", enfe);
            }
            List<Stock> stockList = product.getStockList();
            for (Stock stockListStock : stockList) {
                stockListStock.setProduct(null);
                stockListStock = em.merge(stockListStock);
            }
            em.remove(product);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Product> findProductEntities() {
        return findProductEntities(true, -1, -1);
    }

    public List<Product> findProductEntities(int maxResults, int firstResult) {
        return findProductEntities(false, maxResults, firstResult);
    }

    private List<Product> findProductEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Product.class));
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

    public Product findProduct(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Product.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Product> rt = cq.from(Product.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
