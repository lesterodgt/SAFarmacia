/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sa.ws_farmacia_2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.json.*;

/**
 *
 * @author scxal
 */
@WebService(serviceName = "WSF")
public class WSF {

    /**
     * This is a sample web service operation
     */
    final String bd = "jdbc:mysql://sql9.freemysqlhosting.net:3306/sql9232149";
    //final String bd = "jdbc:mysql://35.185.46.128:3306/farmacia";
    
    @WebMethod(operationName = "IngresoMedicamentoF2")
    public int ingresoMedicamentoF2(@WebParam(name = "Medicamentos") String json) {
        JSONObject obj = new JSONObject(json);
        JSONArray medicamentos = obj.getJSONArray("medicamento");
        
        try{  
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection(  
            bd,"sql9232149","H79lXDv9hX");  
            Statement stmt=con.createStatement();
            for(int a = 0; a < medicamentos.length();a++){
                JSONObject medicamento = (JSONObject)medicamentos.get(a);
                System.out.println(medicamentos.get(a));
                String query = " insert into MEDICAMENTO (Codigo, Nombre, Descripcion,Fabricante,Precio,Existencias,Bajo_prescripcion)"
                    + " values ("
                        +""+medicamento.getString("codigo")+","
                        +"\""+medicamento.getString("nombre")+"\","
                        +"\""+medicamento.getString("descripcion")+"\","
                        +"\""+medicamento.getString("fabricante")+"\","
                        +""+medicamento.getString("precio")+","
                        +""+medicamento.getString("existencias")+","
                        +""+medicamento.getString("bajo_prescripcion")+""
                        + ")";
                try{
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.execute();
                }catch(Exception ex){
                    System.out.println(ex.toString());
                }
                
            }
            con.close(); 
            }catch(Exception e){ 
                System.out.println(e);
                return 0;
            }
        return 1;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "IngresoDespachoF2")
    public int IngresoDespachoF2(@WebParam(name = "Despachos") String json) {
        JSONObject obj = new JSONObject(json);
        JSONArray despachos = obj.getJSONArray("despacho");
        
        try{  
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection(  
            bd,"sql9232149","H79lXDv9hX");  
            Statement stmt=con.createStatement();
            for(int a = 0; a < despachos.length();a++){
                JSONObject despacho = (JSONObject)despachos.get(a);
                System.out.println(despachos.get(a));
                String query = " insert into DESPACHO (Nombres, DPI,Fecha)"
                    + " values ("
                        +"\""+despacho.getString("nombres")+"\","
                        +"\""+despacho.getString("dpi")+"\","
                        +"STR_TO_DATE('"+despacho.getString("fecha")+"', '%d/%m/%Y')"
                        + ")";
                PreparedStatement ps = con.prepareStatement(query);
                ps.execute();
                JSONArray detalles = despacho.getJSONArray("detalle");
                for(int b = 0; b < detalles.length();b++){
                    ResultSet resultSet = stmt.executeQuery("select count(*) from DESPACHO");
                    int key=0;
                    while (resultSet.next()) {
                        key = resultSet.getInt(1);
                    }
                    JSONObject detalle = (JSONObject)detalles.get(b);
                    System.out.println(detalles.get(b));
                    String query1 = " insert into DETALLE_DESPACHO (Cantidad, MEDICAMENTO, DESPACHO)"
                        + " values ("
                            +""+detalle.getString("cantidad")+","
                            +""+detalle.getString("medicamento")+","
                            +""+key+""
                            + ")";
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    ps1.execute();
                }
                
            }
            con.close(); 
            }catch(Exception e){ 
                System.out.println(e);
                return 0;
            }
        return 1;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "ConsultarMedicamentoF2")
    public int consultarMedicamentoF2(@WebParam(name = "Medicamento") int Medicamento) {
        try{  
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection(  
            bd,"sql9232149","H79lXDv9hX");  
            Statement stmt=con.createStatement();
            ResultSet resultSet = stmt.executeQuery("select * from MEDICAMENTO where codigo="+Medicamento+";");
            if(!resultSet.next()){
                con.close();
                return 0;
            }else{
                con.close();
                return 1;
            }
            }catch(Exception e){ 
                System.out.println(e);
                return 0;
            }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "DespachoReceta")
    public String DespachoReceta(@WebParam(name = "Receta") int Receta) {
        //TODO write your implementation code here:
        try{  
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection(  
            bd,"sql9232149","H79lXDv9hX");  
            Statement stmt=con.createStatement();
            ResultSet resultSet = stmt.executeQuery("select * from DESPACHO,DETALLE_DESPACHO,MEDICAMENTO where DESPACHO.idDESPACHO=DETALLE_DESPACHO.DESPACHO and DETALLE_DESPACHO.MEDICAMENTO=MEDICAMENTO.idMEDICAMENTO and DESPACHO.Receta="+Receta);
            org.json.simple.JSONArray despachos = new org.json.simple.JSONArray();
            while(resultSet.next()){
                JSONObject despacho = new JSONObject();
                despacho.put("receta",resultSet.getString("Receta"));
                despacho.put("nombre",resultSet.getString("DESPACHO.Nombres"));
                despacho.put("dpi",resultSet.getString("DPI"));
                despacho.put("cantidad",resultSet.getString("Cantidad"));
                despacho.put("medicamento",resultSet.getString("MEDICAMENTO.Nombre"));
                despachos.add(despacho);
            }
            JSONObject obj = new JSONObject();
            obj.put("despacho",despachos);
            return obj.toString();
            }catch(Exception e){ 
                System.out.println(e);
                return "{}";
            }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "TrasladoMedicamento")
    public int TrasladoMedicamento(@WebParam(name = "Fecha") String Fecha,@WebParam(name = "Origen") int Origen, @WebParam(name = "Destino") int Destino, @WebParam(name = "informacion_medicamento") String informacion_medicamento) {
        //TODO write your implementation code here:
        JSONObject obj = new JSONObject(informacion_medicamento);
        
        try{  
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection(  
            bd,"sql9232149","H79lXDv9hX");  
            Statement stmt=con.createStatement();
                String query = "";
                /*query += " insert into MEDICAMENTO (Codigo, Nombre, Descripcion,Fabricante,Precio,Existencias,Bajo_prescripcion)"
                    + " values ("
                        +""+obj.getString("codigo")+","
                        +"\""+obj.getString("nombre")+"\","
                        +"\""+obj.getString("descripcion")+"\","
                        +"\""+obj.getString("fabricante")+"\","
                        +""+obj.getString("precio")+","
                        +""+obj.getString("existencias")+","
                        +""+obj.getString("bajo_prescripcion")+""
                        + ");\n";*/
                query += "insert into TRASLADO_MEDICAMENTO (FECHA,CODIGO,Origen,Destino)values("
                        +"STR_TO_DATE('"+Fecha+"', '%d/%m/%Y'),"
                        +obj.getString("codigo")+","
                        +Origen+","
                        +Destino+");";
                try{
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.execute();
                }catch(Exception ex){
                    System.out.println(ex.toString());
                }
            con.close(); 
            }catch(Exception e){ 
                System.out.println(e);
                return 0;
            }
        return 1;
    }
}
