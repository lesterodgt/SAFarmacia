/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sa.ws_farmacia_2.WSF;

/**
 *
 * @author scxal
 */
public class WSFTest {
    
    public WSFTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void IngresoDespacho(){
        WSF webService = new WSF();
        int result = webService.IngresoDespachoF2("{despacho:[]}");
        assertEquals(result,0);
    }
    
    @Test
    public void IngresoMedicamento(){
        WSF webService = new WSF();
        int result = webService.ingresoMedicamentoF2("{medicamento:[]}");
        assertEquals(result,4);
    }
    
    @Test
    public void ConsultarMedicamento(){
        WSF webService = new WSF();
        int result = webService.consultarMedicamentoF2(999999);
        assertEquals(result,0);
    }
}
