/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory.jpa;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;



public class MyEntityManager
{
     private EntityManagerFactory emf = null;

    
    public MyEntityManager()
    {
         emf = Persistence.createEntityManagerFactory("InventoryPU");
        

    }

    public EntityManager getEntityManager()
    {
       
         return emf.createEntityManager();
       
    }


    public boolean add(Object object)
    {
        boolean success = false;
        try
        {
            EntityManager em = getEntityManager();
            em.getTransaction().begin();
            em.persist(object);
            em.flush();
            em.getTransaction().commit();
            em.close();
            success=true;
        }
        catch (Exception ex)
        {
            Logger.getLogger(MyEntityManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return success;
    }

    public boolean update(Object object)
    {
        boolean success = false;
        try {
            EntityManager em = getEntityManager();
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(object);
            em.flush();
            em.getTransaction().commit();
            em.close();
            success=true;
        } catch (Exception ex) {
            Logger.getLogger(MyEntityManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    public boolean delete(Object object)
    {   boolean success = false;
        try {EntityManager em = getEntityManager();
             em = getEntityManager();
             em.getTransaction().begin();
             em.remove(em.merge(object));
             em.getTransaction().commit();
             em.close();
            success=true;
        } catch (Exception ex) {
            Logger.getLogger(MyEntityManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    public Object find(Class objectClass,Object id)
    {
       return getEntityManager().find(objectClass, id);
    }

    public int getNewId(Class clazz,String idColumn)
    {
        EntityManager em = getEntityManager();
        int max=0;
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(em.getCriteriaBuilder().max(cq.from(clazz).get(idColumn)));
        Query q = em.createQuery(cq);
        if(q.getSingleResult()!=null)
            return ((Integer) q.getSingleResult()).intValue()+1;
          else
            return 1;
    }

       public int getNewStringId(Class clazz,String idColumn)
    {
        EntityManager em = getEntityManager();
        int max=0;
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(em.getCriteriaBuilder().max(cq.from(clazz).get(idColumn)));
        Query q = em.createQuery(cq);
        if(q.getSingleResult()!=null)
        {
            String str=q.getSingleResult().toString();
            return  Integer.parseInt( str)+1;
        } else
            return 1;
    }

    

}


