/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Familia;
import models.Factura;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import models.Articulo;

/**
 *
 * @author alvaro
 */
public class ArticuloJpaController implements Serializable {

    public ArticuloJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public String[] getColumnsName(){
        String[] columns_name = {"código", "nombre", "familia"};
        return columns_name;
    } 

    public void create(Articulo articulo) throws PreexistingEntityException, Exception {
        if (articulo.getFacturaList() == null) {
            articulo.setFacturaList(new ArrayList<Factura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Familia codfamilia = articulo.getCodfamilia();
            if (codfamilia != null) {
                codfamilia = em.getReference(codfamilia.getClass(), codfamilia.getCodfamilia());
                articulo.setCodfamilia(codfamilia);
            }
            List<Factura> attachedFacturaList = new ArrayList<Factura>();
            for (Factura facturaListFacturaToAttach : articulo.getFacturaList()) {
                facturaListFacturaToAttach = em.getReference(facturaListFacturaToAttach.getClass(), facturaListFacturaToAttach.getNumfactura());
                attachedFacturaList.add(facturaListFacturaToAttach);
            }
            articulo.setFacturaList(attachedFacturaList);
            em.persist(articulo);
            if (codfamilia != null) {
                codfamilia.getArticuloList().add(articulo);
                codfamilia = em.merge(codfamilia);
            }
            for (Factura facturaListFactura : articulo.getFacturaList()) {
                facturaListFactura.getArticuloList().add(articulo);
                facturaListFactura = em.merge(facturaListFactura);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex);    
            if (findArticulo(articulo.getCodarticulo()) != null) {
                throw new PreexistingEntityException("Articulo " + articulo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Articulo articulo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articulo persistentArticulo = em.find(Articulo.class, articulo.getCodarticulo());
            Familia codfamiliaOld = persistentArticulo.getCodfamilia();
            Familia codfamiliaNew = articulo.getCodfamilia();
            List<Factura> facturaListOld = persistentArticulo.getFacturaList();
            List<Factura> facturaListNew = articulo.getFacturaList();
            if (codfamiliaNew != null) {
                codfamiliaNew = em.getReference(codfamiliaNew.getClass(), codfamiliaNew.getCodfamilia());
                articulo.setCodfamilia(codfamiliaNew);
            }
            List<Factura> attachedFacturaListNew = new ArrayList<Factura>();
            for (Factura facturaListNewFacturaToAttach : facturaListNew) {
                facturaListNewFacturaToAttach = em.getReference(facturaListNewFacturaToAttach.getClass(), facturaListNewFacturaToAttach.getNumfactura());
                attachedFacturaListNew.add(facturaListNewFacturaToAttach);
            }
            facturaListNew = attachedFacturaListNew;
            articulo.setFacturaList(facturaListNew);
            articulo = em.merge(articulo);
            if (codfamiliaOld != null && !codfamiliaOld.equals(codfamiliaNew)) {
                codfamiliaOld.getArticuloList().remove(articulo);
                codfamiliaOld = em.merge(codfamiliaOld);
            }
            if (codfamiliaNew != null && !codfamiliaNew.equals(codfamiliaOld)) {
                codfamiliaNew.getArticuloList().add(articulo);
                codfamiliaNew = em.merge(codfamiliaNew);
            }
            for (Factura facturaListOldFactura : facturaListOld) {
                if (!facturaListNew.contains(facturaListOldFactura)) {
                    facturaListOldFactura.getArticuloList().remove(articulo);
                    facturaListOldFactura = em.merge(facturaListOldFactura);
                }
            }
            for (Factura facturaListNewFactura : facturaListNew) {
                if (!facturaListOld.contains(facturaListNewFactura)) {
                    facturaListNewFactura.getArticuloList().add(articulo);
                    facturaListNewFactura = em.merge(facturaListNewFactura);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
             System.out.println(ex.getMessage()+"****");
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = articulo.getCodarticulo();
                if (findArticulo(id) == null) {
                    throw new NonexistentEntityException("The articulo with id " + id + " no longer exists.");
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
            Articulo articulo;
            try {
                articulo = em.getReference(Articulo.class, id);
                articulo.getCodarticulo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The articulo with id " + id + " no longer exists.", enfe);
            }
            Familia codfamilia = articulo.getCodfamilia();
            if (codfamilia != null) {
                codfamilia.getArticuloList().remove(articulo);
                codfamilia = em.merge(codfamilia);
            }
            List<Factura> facturaList = articulo.getFacturaList();
            for (Factura facturaListFactura : facturaList) {
                facturaListFactura.getArticuloList().remove(articulo);
                facturaListFactura = em.merge(facturaListFactura);
            }
            em.remove(articulo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public Boolean checkValues(List<String> values){
        return true;
    } 

    public List<Articulo> findArticuloEntities() {
        return findArticuloEntities(true, -1, -1);
    }

    public List<Articulo> findArticuloEntities(int maxResults, int firstResult) {
        return findArticuloEntities(false, maxResults, firstResult);
    }

    private List<Articulo> findArticuloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Articulo.class));
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

    public Articulo findArticulo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Articulo.class, id);
        } finally {
            em.close();
        }
    }

    public int getArticuloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Articulo> rt = cq.from(Articulo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
