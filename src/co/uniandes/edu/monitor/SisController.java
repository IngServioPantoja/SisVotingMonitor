/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.edu.monitor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import static java.lang.System.out;

/**
 *
 * @author javargas
 */
public class SisController extends Thread {

    /**
     * @param args the command line arguments
     */
    static List<MonitorResult> result = Collections.synchronizedList(new ArrayList<MonitorResult>());

    @Override
    public void run() {
        // TODO code application logic here
        List<String> addresses = new ArrayList<>();
        addresses.add("http://52.91.216.31:8080/SisWeb/webresources");
        addresses.add("http://52.91.216.31:8080/SisWeb/webresources");
        addresses.add("http://52.91.216.32:8080/SisWeb/webresources");
        for (String address : addresses) {
            try {
                SisVotingMonitor hilo = new SisVotingMonitor();
                hilo.setAddress(address);
                hilo.start();
                MonitorResult res = new MonitorResult();
                res.setUri(hilo.getAddress());
                while (hilo.getResult().equals(" ")) {
                }
                res.setResponse(hilo.getResult());
                result.add(res);
            } catch (Exception e) {

            }

        }
    }

    public static void main(String args[]) {
        while (true) {
            StringBuilder mailBody = new StringBuilder();
            SisController main = new SisController();
            Calendar monitorTimeStart = Calendar.getInstance();
            try {
                main.start();
                main.sleep(30000);
                while (result.size() < 3) {
                    out.println("Waiting for response....");
                }

                for (MonitorResult mr : result) {
                    for (MonitorResult mrTo : result) {
                        if (mr.getResponse() != null && mr.getResponse().equals(mrTo.getResponse())) {
                            mr.setVoteCounter(mr.getVoteCounter() + 1);
                        }
                    }
                    mailBody.append("_____________________________________________________________________________________");
                    mailBody.append("\n");
                    mailBody.append("  Votos para el nodo ");
                    mailBody.append(mr.getUri());
                    mailBody.append("\n");
                    mailBody.append("  Total de coincidencias ");
                    mailBody.append(mr.getVoteCounter());
                    mailBody.append("\n");
                    mailBody.append("_____________________________________________________________________________________");
                    mailBody.append("\n");

                }

                System.out.println(mailBody.toString());

                Calendar monitorTimeNext = Calendar.getInstance();
                long diff = monitorTimeNext.getTimeInMillis() - monitorTimeStart.getTimeInMillis();
                while (diff <= (24 * 60 * 60 * 1000)) {
                    monitorTimeNext = Calendar.getInstance();
                    diff = monitorTimeNext.getTimeInMillis() - monitorTimeStart.getTimeInMillis();
                }

            } catch (Exception e) {
                out.println("Error General :(");
            }
        }
    }

}
