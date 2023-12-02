/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author alvaro
 */
@Entity
@Table(name = "ARTICULOS")
@NamedQueries({
    @NamedQuery(name = "Articulo.findAll", query = "SELECT a FROM Articulo a"),
    @NamedQuery(name = "Articulo.findByCodarticulo", query = "SELECT a FROM Articulo a WHERE a.codarticulo = :codarticulo"),
    @NamedQuery(name = "Articulo.findByNomarticulo", query = "SELECT a FROM Articulo a WHERE a.nomarticulo = :nomarticulo")})
public class Articulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODARTICULO")
    private String codarticulo;
    @Basic(optional = false)
    @Column(name = "NOMARTICULO")
    private String nomarticulo;
    @JoinTable(name = "DETALLE_FACTURAS", joinColumns = {
        @JoinColumn(name = "CODARTICULO", referencedColumnName = "CODARTICULO")}, inverseJoinColumns = {
        @JoinColumn(name = "NUMFACTURA", referencedColumnName = "NUMFACTURA")})
    @ManyToMany
    private List<Factura> facturaList;
    @JoinColumn(name = "CODFAMILIA", referencedColumnName = "CODFAMILIA", nullable = true)
    @ManyToOne
    private Familia codfamilia;

    public Articulo() {
    }

    public Articulo(String codarticulo) {
        this.codarticulo = codarticulo;
    }

    public Articulo(String codarticulo, String nomarticulo) {
        this.codarticulo = codarticulo;
        this.nomarticulo = nomarticulo;
    }

    public String getCodarticulo() {
        return codarticulo;
    }

    public void setCodarticulo(String codarticulo) {
        this.codarticulo = codarticulo;
    }

    public String getNomarticulo() {
        return nomarticulo;
    }

    public void setNomarticulo(String nomarticulo) {
        this.nomarticulo = nomarticulo;
    }

    public List<Factura> getFacturaList() {
        return facturaList;
    }

    public void setFacturaList(List<Factura> facturaList) {
        this.facturaList = facturaList;
    }

    public Familia getCodfamilia() {
        return codfamilia;
    }

    public void setCodfamilia(Familia codfamilia) {
        this.codfamilia = codfamilia;
    }
    
    public Object[] convertToObjectArray(){
        if(codfamilia != null){
            Object[] object_data = {codarticulo, nomarticulo, codfamilia};
            return object_data; 
        }else{
            Object[] object_data = {codarticulo, nomarticulo, ""};
            return object_data; 
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codarticulo != null ? codarticulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Articulo)) {
            return false;
        }
        Articulo other = (Articulo) object;
        if ((this.codarticulo == null && other.codarticulo != null) || (this.codarticulo != null && !this.codarticulo.equals(other.codarticulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + codarticulo + " - " + nomarticulo;
    }
    
}
