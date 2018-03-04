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
    final String bd = "jdbc:mysql://35.196.79.167:3306/farmacia";
    
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
                PreparedStatement ps = con.prepareStatement(query);
                ps.execute();
                
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
}
