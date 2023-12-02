/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Articulo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import models.Familia;

/**
 *
 * @author alvaro
 */
public class FamiliaJpaController implements Serializable {

    public FamiliaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public String[] getColumnsName(){
        String[] columns_name = {"código", "nombre"};
        return columns_name;
    }

    public void create(Familia familia) throws PreexistingEntityException, Exception {
        if (familia.getArticuloList() == null) {
            familia.setArticuloList(new ArrayList<Articulo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Articulo> attachedArticuloList = new ArrayList<Articulo>();
            for (Articulo articuloListArticuloToAttach : familia.getArticuloList()) {
                articuloListArticuloToAttach = em.getReference(articuloListArticuloToAttach.getClass(), articuloListArticuloToAttach.getCodarticulo());
                attachedArticuloList.add(articuloListArticuloToAttach);
            }
            familia.setArticuloList(attachedArticuloList);
            em.persist(familia);
            for (Articulo articuloListArticulo : familia.getArticuloList()) {
                Familia oldCodfamiliaOfArticuloListArticulo = articuloListArticulo.getCodfamilia();
                articuloListArticulo.setCodfamilia(familia);
                articuloListArticulo = em.merge(articuloListArticulo);
                if (oldCodfamiliaOfArticuloListArticulo != null) {
                    oldCodfamiliaOfArticuloListArticulo.getArticuloList().remove(articuloListArticulo);
                    oldCodfamiliaOfArticuloListArticulo = em.merge(oldCodfamiliaOfArticuloListArticulo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFamilia(familia.getCodfamilia()) != null) {
                throw new PreexistingEntityException("Familia " + familia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Familia familia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Familia persistentFamilia = em.find(Familia.class, familia.getCodfamilia());
            List<Articulo> articuloListOld = persistentFamilia.getArticuloList();
            List<Articulo> articuloListNew = familia.getArticuloList();
            List<String> illegalOrphanMessages = null;
            for (Articulo articuloListOldArticulo : articuloListOld) {
                if (!articuloListNew.contains(articuloListOldArticulo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Articulo " + articuloListOldArticulo + " since its codfamilia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Articulo> attachedArticuloListNew = new ArrayList<Articulo>();
            for (Articulo articuloListNewArticuloToAttach : articuloListNew) {
                articuloListNewArticuloToAttach = em.getReference(articuloListNewArticuloToAttach.getClass(), articuloListNewArticuloToAttach.getCodarticulo());
                attachedArticuloListNew.add(articuloListNewArticuloToAttach);
            }
            articuloListNew = attachedArticuloListNew;
            familia.setArticuloList(articuloListNew);
            familia = em.merge(familia);
            for (Articulo articuloListNewArticulo : articuloListNew) {
                if (!articuloListOld.contains(articuloListNewArticulo)) {
                    Familia oldCodfamiliaOfArticuloListNewArticulo = articuloListNewArticulo.getCodfamilia();
                    articuloListNewArticulo.setCodfamilia(familia);
                    articuloListNewArticulo = em.merge(articuloListNewArticulo);
                    if (oldCodfamiliaOfArticuloListNewArticulo != null && !oldCodfamiliaOfArticuloListNewArticulo.equals(familia)) {
                        oldCodfamiliaOfArticuloListNewArticulo.getArticuloList().remove(articuloListNewArticulo);
                        oldCodfamiliaOfArticuloListNewArticulo = em.merge(oldCodfamiliaOfArticuloListNewArticulo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage()+"----");
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = familia.getCodfamilia();
                if (findFamilia(id) == null) {
                    throw new NonexistentEntityException("The familia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Familia familia;
            try {
                familia = em.getReference(Familia.class, id);
                familia.getCodfamilia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The familia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Articulo> articuloListOrphanCheck = familia.getArticuloList();
            for (Articulo articuloListOrphanCheckArticulo : articuloListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Familia (" + familia + ") cannot be destroyed since the Articulo " + articuloListOrphanCheckArticulo + " in its articuloList field has a non-nullable codfamilia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(familia);
            em.getTransaction().commit();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public Boolean checkValues(List<String> values){
        return true;
    } 

    public List<Familia> findFamiliaEntities() {
        return findFamiliaEntities(true, -1, -1);
    }

    public List<Familia> findFamiliaEntities(int maxResults, int firstResult) {
        return findFamiliaEntities(false, maxResults, firstResult);
    }

    private List<Familia> findFamiliaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Familia.class));
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

    public Familia findFamilia(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Familia.class, id);
        } finally {
            em.close();
        }
    }

    public int getFamiliaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Familia> rt = cq.from(Familia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
