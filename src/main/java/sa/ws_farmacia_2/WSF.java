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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.json.*;
import java.util.Date;

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
            bd,"root","grupo2sa");  
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
            bd,"root","grupo2sa");  
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
            bd,"root","grupo2sa");  
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
    public String DespachoReceta(@WebParam(name = "Receta") String Receta) {
        //TODO write your implementation code here:
        try{  
            Class.forName("com.mysql.jdbc.Driver");  
            JSONObject ent = new JSONObject(Receta);
            Connection con=DriverManager.getConnection(  
            bd,"sql9232149","H79lXDv9hX");  
            Statement stmt=con.createStatement();
            org.json.simple.JSONArray despachos = new org.json.simple.JSONArray();
            org.json.simple.JSONArray medicamentos = new org.json.simple.JSONArray();
            ResultSet resultSet = stmt.executeQuery("select * from DESPACHO,DETALLE_DESPACHO,MEDICAMENTO where DESPACHO.idDESPACHO=DETALLE_DESPACHO.DESPACHO and DETALLE_DESPACHO.MEDICAMENTO=MEDICAMENTO.idMEDICAMENTO and DESPACHO.Receta="+ent.getString("Receta"));
            int i = 0;
            JSONObject despacho = new JSONObject();
            while(resultSet.next()){
                if(i==0){
                    despacho.put("Receta",resultSet.getString("Receta"));
                    despacho.put("Nombre",resultSet.getString("DESPACHO.Nombres"));
                    despacho.put("DPI",resultSet.getString("DPI"));
                    despacho.put("Fecha","null");
                    despachos.add(despacho);
                    i++;
                }
                JSONObject medicamento = new JSONObject();
                medicamento.put("cantidad",resultSet.getString("Cantidad"));
                medicamento.put("medicamento",resultSet.getString("MEDICAMENTO.Nombre"));
                medicamentos.add(medicamento);
            }
            despacho.put("Medicamentos",medicamentos);
            JSONObject obj = new JSONObject();
            obj.put("Despachos",despachos);
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
    public String TrasladoMedicamento(@WebParam(name = "informacion_medicamento") String informacion_medicamento) {
        //TODO write your implementation code here:
        JSONObject obj = new JSONObject(informacion_medicamento);
        try{  
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection(  
            bd,"sql9232149","H79lXDv9hX");  
            Statement stmt=con.createStatement();
                String query = "";
                String query1 = "";
                JSONArray medicamentos = obj.getJSONArray("Medicamentos");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                for(int i = 0; i < medicamentos.length();i++){
                    JSONObject medicamento = (JSONObject)medicamentos.get(i);
                    query += "insert into TRASLADO_MEDICAMENTO (FECHA,CODIGO,Origen,Destino,Cantidad)values("
                        +"STR_TO_DATE('"+dateFormat.format(date)+"', '%d/%m/%Y'),"
                        +medicamento.getString("Codigo")+","
                        +obj.getString("Origen")+","
                        +obj.getString("Destino")+","
                        +medicamento.getString("Cantidad")+");\n";
                    if(obj.getString("Destino").equals("2")){
                        query1 += "update MEDICAMENTO set Existencias=Existencias+"+medicamento.getString("Cantidad")+" where Codigo="+medicamento.getString("Codigo")+";";
                    }else{
                        query1 += "update MEDICAMENTO set Existencias=Existencias-"+medicamento.getString("Cantidad")+" where Codigo="+medicamento.getString("Codigo")+";";
                    }
                }
                try{
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.execute();
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    ps1.execute();
                }catch(Exception ex){
                    System.out.println(ex.toString());
                }
            con.close(); 
            }catch(Exception e){ 
                System.out.println(e);
                return "{ \"Exito\":\"0\",\"Error\":\""+e.toString()+"\"}";
            }
        return "{ \"Exito\":\"1\",\"Error\":\"\"}";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "ConsultarExistencias")
    public String ConsultarExistencias(@WebParam(name = "Codigo") String Codigo) {
        //TODO write your implementation code here:
        try{  
            JSONObject obj = new JSONObject(Codigo);
            System.out.println(obj.toString());
            String result="";
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection(  
            bd,"sql9232149","H79lXDv9hX");  
            Statement stmt=con.createStatement();
            ResultSet resultSet = stmt.executeQuery("select * from MEDICAMENTO where codigo="+obj.getString("Codigo")+";");
            if(!resultSet.next()){
                con.close();
                result= "{\"Cantidad\":\"0\"}";
            }else{
                result= "{\"Cantidad\":\""+resultSet.getString("Existencias")+"\"}";
                con.close();
            }
            return result;
            }catch(Exception e){ 
                System.out.println(e);
                return "{\"Cantidad\":\"0\"}";
            }
    }
}
