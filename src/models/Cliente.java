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
@Table(name = "CLIENTES")
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findByCodcliente", query = "SELECT c FROM Cliente c WHERE c.codcliente = :codcliente"),
    @NamedQuery(name = "Cliente.findByNomcliente", query = "SELECT c FROM Cliente c WHERE c.nomcliente = :nomcliente")})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODCLIENTE")
    private String codcliente;
    @Basic(optional = false)
    @Column(name = "NOMCLIENTE")
    private String nomcliente;
    @OneToMany(mappedBy = "codcliente")
    private List<Factura> facturaList;
    
    public Cliente() {
    }

    public Cliente(String codcliente) {
        this.codcliente = codcliente;
    }

    public Cliente(String codcliente, String nomcliente) {
        this.codcliente = codcliente;
        this.nomcliente = nomcliente;
    }

    public String getCodcliente() {
        return codcliente;
    }

    public void setCodcliente(String codcliente) {
        this.codcliente = codcliente;
    }

    public String getNomcliente() {
        return nomcliente;
    }

    public void setNomcliente(String nomcliente) {
        this.nomcliente = nomcliente;
    }

    public List<Factura> getFacturaList() {
        return facturaList;
    }

    public void setFacturaList(List<Factura> facturaList) {
        this.facturaList = facturaList;
    }
    
    public Object[] convertToObjectArray(){
        Object[] object_data = {codcliente, nomcliente};
        return object_data; 
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codcliente != null ? codcliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.codcliente == null && other.codcliente != null) || (this.codcliente != null && !this.codcliente.equals(other.codcliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + codcliente + " - " + nomcliente;
    }
    
}
