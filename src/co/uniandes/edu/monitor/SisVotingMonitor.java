/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.edu.monitor;

import co.uniandes.edu.sis.client.SisWebClient;
import static java.lang.System.out;


/**
 *
 * @author javargas
 */
public class SisVotingMonitor extends Thread {

    String address=" ";
    String result =" ";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public void run() {

        try {
            out.println("Monitor a  " + this.getAddress() );
            SisWebClient client = new SisWebClient(this.getAddress());
            this.setResult(client.countREST());
            out.println("Valor count " + this.getResult());
        }
        catch (javax.ws.rs.ProcessingException pe)
        {
            String counter = "Error";
            this.setResult(counter);
            System.out.println(pe.getMessage());
        }
        catch (Exception e) {
            String counter = "Error";
            this.setResult(counter);
             System.out.println(e.getMessage());
         
        }
    }
}
