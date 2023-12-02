/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import models.Articulo;
import models.Cliente;
import models.Factura;
import models.Familia;

/**
 *
 * @author alvaro
 */
public class SessionController {

    private String UNIT_NAME = "jpaunidad";
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory(UNIT_NAME);
    private EntityManager em;
    private ArticuloJpaController articulo_ctrl = new ArticuloJpaController(emf);
    private ClienteJpaController cliente_ctrl = new ClienteJpaController(emf);
    private FamiliaJpaController familia_ctrl = new FamiliaJpaController(emf);
    private FacturaJpaController factura_ctrl = new FacturaJpaController(emf);
    
    public SessionController(){
        
    }
    
    public Object[] getById(String id, String table){
        if(!haveConnection()){
            return null;
        }else{
            Object[] result = null;
            switch (table.toLowerCase()) {
                case "familias":
                    Familia familia = familia_ctrl.findFamilia(id);
                    result = familia.convertToObjectArray();
                    break;
                case "articulos":
                    Articulo articulo = articulo_ctrl.findArticulo(id);
                    result = articulo.convertToObjectArray();
                    break;
                case "facturas":
                    Factura factura = factura_ctrl.findFactura(Long.valueOf(id));
                    result = factura.convertToObjectArray();
                    break;
                case "clientes":
                    Cliente cliente = cliente_ctrl.findCliente(id);
                    result = cliente.convertToObjectArray();
                    break;
            }
            return result;
        }
    }
    
    public List getTableById(String id, String table){
        if(!haveConnection()){
            return null;
        }else{
            List result = null;
            switch (table.toLowerCase()) {
                case "familias":
                    Familia familia = familia_ctrl.findFamilia(id);
                    result = new ArrayList<>(familia.getArticuloList());
                    break;
                case "articulos":
                    Articulo articulo = articulo_ctrl.findArticulo(id);
                    result = new ArrayList<>(articulo.getFacturaList());
                    break;
                case "facturas":
                    Factura factura = factura_ctrl.findFactura(Long.valueOf(id));
                    result = new ArrayList<>(factura.getArticuloList());
                    break;
                case "clientes":
                    Cliente cliente = cliente_ctrl.findCliente(id);
                    result = new ArrayList<>(cliente.getFacturaList());
                    break;
            }
            return result;
        }
    }
    
    public List getTable(String table){
        if(!haveConnection()){
            return null;
        }else{
            openConnection();
            switch (table.toLowerCase()){
                case "familias":
                    return familia_ctrl.findFamiliaEntities();
                case "articulos":
                    return articulo_ctrl.findArticuloEntities();
                case "facturas":
                    return factura_ctrl.findFacturaEntities();
                case "clientes":
                    return cliente_ctrl.findClienteEntities();
                default:
                    return null;
            }
        }
    }
    
    public List getOthers(String id, String table){
        if(!haveConnection()){
            return null;
        }else{
            List result = null;
            switch (table.toLowerCase()) {
                case "familias":
                    Familia familia = familia_ctrl.findFamilia(id);;
                    List all_fam = articulo_ctrl.findArticuloEntities();
                    
                    all_fam.removeAll(familia.getArticuloList());
                    result = all_fam;
                    break;
                case "articulos":
                    Articulo articulo = articulo_ctrl.findArticulo(id);
                    List all_art = factura_ctrl.findFacturaEntities();
                    
                    all_art.removeAll(articulo.getFacturaList());
                    result = all_art;
                    break;
                case "facturas":
                    Factura factura = factura_ctrl.findFactura(Long.valueOf(id));
                    List all_fac = articulo_ctrl.findArticuloEntities();
                    
                    all_fac.removeAll(factura.getArticuloList());
                    result = all_fac;
                    break;
                case "clientes":
                    Cliente cliente = cliente_ctrl.findCliente(id);
                    List all_cli = factura_ctrl.findFacturaEntities();
                    
                    all_cli.removeAll(cliente.getFacturaList());
                    result = all_cli;
                    break;
            }
            return result;
        }
    }
    
    public String[] getColumnsName(String table){
        switch(table.toLowerCase()){
            case "familias":
                return familia_ctrl.getColumnsName();
            case "articulos":
                return articulo_ctrl.getColumnsName();
            case "facturas":
                return factura_ctrl.getColumnsName();
            case "clientes":
                return cliente_ctrl.getColumnsName();
            default:
                return null;
        }
    }
    
    public Boolean addRegister(List<String> values, String table_selected){
        if(haveConnection()){
            try{
                switch(table_selected.toLowerCase()){
                    case "familias":
                        Familia familia = new Familia(values.get(0),values.get(1));
                        familia_ctrl.create(familia);
                        break;
                    case "articulos":
                        Familia familia_foreing = (Familia) familia_ctrl.findFamilia(values.get(2).split("-")[0].trim());
                        Articulo articulo = new Articulo(values.get(0), values.get(1));
                        articulo.setCodfamilia(familia_foreing);
                        articulo_ctrl.create(articulo);
                        break;
                    case "facturas":
                        Cliente cliente_foreing = (Cliente) cliente_ctrl.findCliente(values.get(1).split("-")[0].trim());
                        Articulo articulo_foreing = (Articulo) articulo_ctrl.findArticulo(values.get(2).split("-")[0].trim());
                        Factura factura = new Factura(Long.valueOf(values.get(0)), new Date());
                        factura.setCodcliente(cliente_foreing);
                        List<Articulo> list = new ArrayList<>();
                        list.add(articulo_foreing);
                        factura.setArticuloList(list);
                        factura_ctrl.create(factura);
                        break;
                    case "clientes":
                        Cliente cliente = new Cliente(values.get(0), values.get(1));
                        cliente_ctrl.create(cliente);
                        break;
                }
                return true;
            }catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return false;
    }
    
    public void updateRegister(List<String> values, String table_selected, String id){
        if(haveConnection()){
            try{
                switch(table_selected.toLowerCase()){
                    case "familias":
                        Familia familia = familia_ctrl.findFamilia(id);
                        familia.setNomfamilia(values.get(0));
                        familia_ctrl.edit(familia);
                        break;
                    case "articulos":
                        Articulo articulo = articulo_ctrl.findArticulo(id);
                        articulo.setNomarticulo(values.get(0));
                        articulo_ctrl.edit(articulo);
                        break;
                    case "clientes":
                        Cliente cliente = cliente_ctrl.findCliente(id);
                        cliente.setNomcliente(values.get(0));
                        cliente_ctrl.edit(cliente);
                        break;
                }
            }catch (Exception e) {
            }finally{
            }
        }
    }
    
    public void deleteRegister(String id, String table_selected){
        if(haveConnection()){
            try{
                switch(table_selected.toLowerCase()){
                    case "familias":
                        Familia familia = familia_ctrl.findFamilia(id);
                        
                        for(Articulo art : familia.getArticuloList()){
                            for(Factura fac : art.getFacturaList()){
                                disassociateRegister(String.valueOf(fac.getNumfactura()), art.getCodarticulo(), "articulos");
                            }
                            disassociateRegister(art.getCodarticulo(), familia.getCodfamilia(), "familias");
                            articulo_ctrl.destroy(art.getCodarticulo());
                        }
                        
                        familia_ctrl.destroy(id);
                        break;
                    case "articulos":
                        Articulo articulo = articulo_ctrl.findArticulo(id);
                        Familia fam_art = familia_ctrl.findFamilia(articulo.getCodfamilia().getCodfamilia());
                        
                        disassociateRegister(articulo.getCodarticulo(), fam_art.getCodfamilia(), "familias");
                        
                        for(Factura fac : articulo.getFacturaList()){
                            disassociateRegister(String.valueOf(fac.getNumfactura()), articulo.getCodarticulo(), "articulos");
                        }
                        
                        articulo_ctrl.destroy(id);
                        break;
                    case "facturas":
                        Factura factura = factura_ctrl.findFactura(Long.valueOf(id));
                        Cliente cli_fac = cliente_ctrl.findCliente(factura.getCodcliente().getCodcliente());
                        
                        disassociateRegister(String.valueOf(factura.getNumfactura()), cli_fac.getCodcliente(), "clientes");
                        
                        for(Articulo art : factura.getArticuloList()){
                            disassociateRegister(String.valueOf(art.getCodarticulo()), String.valueOf(factura.getNumfactura()), "facturas");
                        }
                        
                        factura_ctrl.destroy(Long.valueOf(id));
                        break;
                    case "clientes":
                        Cliente cliente = cliente_ctrl.findCliente(id);
                        
                        for(Factura fac : cliente.getFacturaList()){
                            for(Articulo art : fac.getArticuloList()){
                                disassociateRegister(String.valueOf(art.getCodarticulo()), String.valueOf(fac.getNumfactura()), "facturas");
                            }
                            disassociateRegister(String.valueOf(fac.getNumfactura()), cliente.getCodcliente(), "clientes");
                            factura_ctrl.destroy(fac.getNumfactura());
                        }
                                
                        cliente_ctrl.destroy(id);
                        break;
                }
            }catch (Exception e) {
            }
        }
    }
    
    public void disassociateRegister(String id, String id_table, String table_selected){
        if(haveConnection()){
            try{
                switch(table_selected.toLowerCase()){
                    case "familias":
                        Familia familia = familia_ctrl.findFamilia(id_table);
                        Articulo art_fam = articulo_ctrl.findArticulo(id);
  
                        if(familia != null && art_fam != null){
                            art_fam.setCodfamilia(null);
                            familia.getArticuloList().remove(art_fam);
                            
                            articulo_ctrl.edit(art_fam);
                            familia_ctrl.edit(familia);
                        }
                        break;
                    case "articulos":
                        Articulo articulo = articulo_ctrl.findArticulo(id_table);
                        Factura fac_art = factura_ctrl.findFactura(Long.valueOf(id));
                        
                        if(articulo != null && fac_art != null){
                            articulo.getFacturaList().remove(fac_art);
                            fac_art.getArticuloList().remove(articulo);
                            
                            articulo_ctrl.edit(articulo);
                            factura_ctrl.edit(fac_art);
                        }
                        break;
                    case "facturas":
                        Factura factura = factura_ctrl.findFactura(Long.valueOf(id_table));
                        Articulo art_fac = articulo_ctrl.findArticulo(id);
                        
                        if(factura != null && art_fac != null){
                            factura.getArticuloList().remove(art_fac);
                            art_fac.getFacturaList().remove(factura);
                            
                            articulo_ctrl.edit(art_fac);
                            factura_ctrl.edit(factura);
                        }
                        break;
                    case "clientes":
                        Cliente cliente = cliente_ctrl.findCliente(id_table);
                        Factura fac_cli = factura_ctrl.findFactura(Long.valueOf(id));
                        
                        if(cliente != null && fac_cli != null){
                            cliente.getFacturaList().remove(fac_cli);
                            fac_cli.setCodcliente(null);
                            
                            factura_ctrl.edit(fac_cli);
                            cliente_ctrl.edit(cliente);
                        }
                        break;
                }
            }catch (Exception e) {
            }
        }
    }
    
    public Boolean associateRegister(String id, String id_table, String table_selected){
        if(haveConnection()){
            try{
                switch(table_selected.toLowerCase()){
                    case "familias":
                        Familia familia = familia_ctrl.findFamilia(id_table);
                        Articulo art_fam = articulo_ctrl.findArticulo(id);
  
                        if(familia != null && art_fam != null && art_fam.getCodfamilia()== null){
                            art_fam.setCodfamilia(familia);
                            familia.getArticuloList().add(art_fam);
                        
                            articulo_ctrl.edit(art_fam);
                            familia_ctrl.edit(familia);
                            return true;
                        }
                        return false;
                    case "articulos":
                        Articulo articulo = articulo_ctrl.findArticulo(id_table);
                        Factura fac_art = factura_ctrl.findFactura(Long.valueOf(id));
                        
                        if(articulo != null && fac_art != null){
                            articulo.getFacturaList().add(fac_art);
                            fac_art.getArticuloList().add(articulo);
                            
                            factura_ctrl.edit(fac_art);
                            articulo_ctrl.edit(articulo);
                            return true;
                        }
                        return false;
                    case "facturas":
                        Factura factura = factura_ctrl.findFactura(Long.valueOf(id_table));
                        Articulo art_fac = articulo_ctrl.findArticulo(id);
                        
                        if(factura != null && art_fac != null){
                            factura.getArticuloList().add(art_fac);
                            art_fac.getFacturaList().add(factura);
                            
                            articulo_ctrl.edit(art_fac);
                            factura_ctrl.edit(factura);
                            return true;
                        }
                        return false;
                    case "clientes":
                        Cliente cliente = cliente_ctrl.findCliente(id_table);
                        Factura fac_cli = factura_ctrl.findFactura(Long.valueOf(id));
                        
                        if(cliente != null && fac_cli != null && fac_cli.getCodcliente()== null){
                            cliente.getFacturaList().add(fac_cli);
                            fac_cli.setCodcliente(cliente);
                            
                            factura_ctrl.edit(fac_cli);
                            cliente_ctrl.edit(cliente);
                            return true;
                        }
                        return false;
                }
            }catch (Exception e) {
                return false;
            }finally{
            }
        }
        return false; 
    }
    
    public Boolean haveConnection(){
        try{
            openConnection();
            
            return this.em.isOpen();
        }catch(Exception e){
            
        }finally{
            closeConnection();
        }
        return false;
    }
    
    public void openConnection(){
        this.em = this.emf.createEntityManager();
    }
    
    public void closeConnection(){
        if(this.em.isOpen())
            this.em.close();
    }
    
    public ArticuloJpaController getArticulo_ctrl() {
        return articulo_ctrl;
    }

    public ClienteJpaController getCliente_ctrl() {
        return cliente_ctrl;
    }

    public FamiliaJpaController getFamilia_ctrl() {
        return familia_ctrl;
    }

    public FacturaJpaController getFactura_ctrl() {
        return factura_ctrl;
    }
}
