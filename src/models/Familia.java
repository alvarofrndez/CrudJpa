/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author alvaro
 */
@Entity
@Table(name = "FAMILIAS")
@NamedQueries({
    @NamedQuery(name = "Familia.findAll", query = "SELECT f FROM Familia f"),
    @NamedQuery(name = "Familia.findByCodfamilia", query = "SELECT f FROM Familia f WHERE f.codfamilia = :codfamilia"),
    @NamedQuery(name = "Familia.findByNomfamilia", query = "SELECT f FROM Familia f WHERE f.nomfamilia = :nomfamilia")})
public class Familia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODFAMILIA")
    private String codfamilia;
    @Basic(optional = false)
    @Column(name = "NOMFAMILIA")
    private String nomfamilia;
    @OneToMany(mappedBy = "codfamilia")
    private List<Articulo> articuloList;

    public Familia() {
    }

    public Familia(String codfamilia) {
        this.codfamilia = codfamilia;
    }

    public Familia(String codfamilia, String nomfamilia) {
        this.codfamilia = codfamilia;
        this.nomfamilia = nomfamilia;
    }

    public String getCodfamilia() {
        return codfamilia;
    }

    public void setCodfamilia(String codfamilia) {
        this.codfamilia = codfamilia;
    }

    public String getNomfamilia() {
        return nomfamilia;
    }

    public void setNomfamilia(String nomfamilia) {
        this.nomfamilia = nomfamilia;
    }

    public List<Articulo> getArticuloList() {
        return articuloList;
    }

    public void setArticuloList(List<Articulo> articuloList) {
        this.articuloList = articuloList;
    }
    
    public Object[] convertToObjectArray(){
        Object[] object_data = {codfamilia, nomfamilia};
        return object_data; 
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codfamilia != null ? codfamilia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Familia)) {
            return false;
        }
        Familia other = (Familia) object;
        if ((this.codfamilia == null && other.codfamilia != null) || (this.codfamilia != null && !this.codfamilia.equals(other.codfamilia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + codfamilia + " - " + nomfamilia;
    }
    
}
