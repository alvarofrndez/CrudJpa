/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author alvaro
 */
@Entity
@Table(name = "FACTURAS")
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f"),
    @NamedQuery(name = "Factura.findByNumfactura", query = "SELECT f FROM Factura f WHERE f.numfactura = :numfactura"),
    @NamedQuery(name = "Factura.findByFechafactura", query = "SELECT f FROM Factura f WHERE f.fechafactura = :fechafactura")})
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NUMFACTURA")
    private Long numfactura;
    @Column(name = "FECHAFACTURA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafactura;
    @ManyToMany(mappedBy = "facturaList")
    private List<Articulo> articuloList;
    @JoinColumn(name = "CODCLIENTE", referencedColumnName = "CODCLIENTE")
    @ManyToOne(optional = true)
    private Cliente codcliente;
    
    public Factura() {
    }

    public Factura(Long numfactura) {
        this.numfactura = numfactura;
    }
    
    public Factura(Long numfactura, Date fechafactura){
        this.numfactura = numfactura;
        this.fechafactura = fechafactura;
    }

    public Long getNumfactura() {
        return numfactura;
    }

    public void setNumfactura(Long numfactura) {
        this.numfactura = numfactura;
    }

    public Date getFechafactura() {
        return fechafactura;
    }

    public void setFechafactura(Date fechafactura) {
        this.fechafactura = fechafactura;
    }

    public List<Articulo> getArticuloList() {
        return articuloList;
    }

    public void setArticuloList(List<Articulo> articuloList) {
        this.articuloList = articuloList;
    }

    public Cliente getCodcliente() {
        return codcliente;
    }

    public void setCodcliente(Cliente codcliente) {
        this.codcliente = codcliente;
    }
    
    public Object[] convertToObjectArray(){
        if(codcliente != null){
            Object[] object_data = {numfactura, fechafactura, codcliente};
            return object_data; 
        }else{
            Object[] object_data = {numfactura, fechafactura, ""};
            return object_data; 
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numfactura != null ? numfactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.numfactura == null && other.numfactura != null) || (this.numfactura != null && !this.numfactura.equals(other.numfactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + numfactura + " - " + fechafactura;
    }
    
}
