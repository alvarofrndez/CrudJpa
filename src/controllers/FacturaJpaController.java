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
import models.Cliente;
import models.Articulo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import models.Factura;

/**
 *
 * @author alvaro
 */
public class FacturaJpaController implements Serializable {

    public FacturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public String[] getColumnsName(){
        String[] columns_name = {"numero", "fecha", "cliente"};
        return columns_name;
    }

    public void create(Factura factura) throws PreexistingEntityException, Exception {
        if (factura.getArticuloList() == null) {
            factura.setArticuloList(new ArrayList<Articulo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente codcliente = factura.getCodcliente();
            if (codcliente != null) {
                codcliente = em.getReference(codcliente.getClass(), codcliente.getCodcliente());
                factura.setCodcliente(codcliente);
            }
            List<Articulo> attachedArticuloList = new ArrayList<Articulo>();
            for (Articulo articuloListArticuloToAttach : factura.getArticuloList()) {
                articuloListArticuloToAttach = em.getReference(articuloListArticuloToAttach.getClass(), articuloListArticuloToAttach.getCodarticulo());
                attachedArticuloList.add(articuloListArticuloToAttach);
            }
            factura.setArticuloList(attachedArticuloList);
            em.persist(factura);
            if (codcliente != null) {
                codcliente.getFacturaList().add(factura);
                codcliente = em.merge(codcliente);
            }
            for (Articulo articuloListArticulo : factura.getArticuloList()) {
                articuloListArticulo.getFacturaList().add(factura);
                articuloListArticulo = em.merge(articuloListArticulo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFactura(factura.getNumfactura()) != null) {
                throw new PreexistingEntityException("Factura " + factura + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Factura factura) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura persistentFactura = em.find(Factura.class, factura.getNumfactura());
            Cliente codclienteOld = persistentFactura.getCodcliente();
            Cliente codclienteNew = factura.getCodcliente();
            List<Articulo> articuloListOld = persistentFactura.getArticuloList();
            List<Articulo> articuloListNew = factura.getArticuloList();
            if (codclienteNew != null) {
                codclienteNew = em.getReference(codclienteNew.getClass(), codclienteNew.getCodcliente());
                factura.setCodcliente(codclienteNew);
            }
            List<Articulo> attachedArticuloListNew = new ArrayList<Articulo>();
            for (Articulo articuloListNewArticuloToAttach : articuloListNew) {
                articuloListNewArticuloToAttach = em.getReference(articuloListNewArticuloToAttach.getClass(), articuloListNewArticuloToAttach.getCodarticulo());
                attachedArticuloListNew.add(articuloListNewArticuloToAttach);
            }
            articuloListNew = attachedArticuloListNew;
            factura.setArticuloList(articuloListNew);
            factura = em.merge(factura);
            if (codclienteOld != null && !codclienteOld.equals(codclienteNew)) {
                codclienteOld.getFacturaList().remove(factura);
                codclienteOld = em.merge(codclienteOld);
            }
            if (codclienteNew != null && !codclienteNew.equals(codclienteOld)) {
                codclienteNew.getFacturaList().add(factura);
                codclienteNew = em.merge(codclienteNew);
            }
            for (Articulo articuloListOldArticulo : articuloListOld) {
                if (!articuloListNew.contains(articuloListOldArticulo)) {
                    articuloListOldArticulo.getFacturaList().remove(factura);
                    articuloListOldArticulo = em.merge(articuloListOldArticulo);
                }
            }
            for (Articulo articuloListNewArticulo : articuloListNew) {
                if (!articuloListOld.contains(articuloListNewArticulo)) {
                    articuloListNewArticulo.getFacturaList().add(factura);
                    articuloListNewArticulo = em.merge(articuloListNewArticulo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = factura.getNumfactura();
                if (findFactura(id) == null) {
                    throw new NonexistentEntityException("The factura with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura factura;
            try {
                factura = em.getReference(Factura.class, id);
                factura.getNumfactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The factura with id " + id + " no longer exists.", enfe);
            }
            Cliente codcliente = factura.getCodcliente();
            if (codcliente != null) {
                codcliente.getFacturaList().remove(factura);
                codcliente = em.merge(codcliente);
            }
            List<Articulo> articuloList = factura.getArticuloList();
            for (Articulo articuloListArticulo : articuloList) {
                articuloListArticulo.getFacturaList().remove(factura);
                articuloListArticulo = em.merge(articuloListArticulo);
            }
            em.remove(factura);
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

    public List<Factura> findFacturaEntities() {
        return findFacturaEntities(true, -1, -1);
    }

    public List<Factura> findFacturaEntities(int maxResults, int firstResult) {
        return findFacturaEntities(false, maxResults, firstResult);
    }

    private List<Factura> findFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Factura.class));
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

    public Factura findFactura(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Factura> rt = cq.from(Factura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
