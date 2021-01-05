/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wolff_doctor;

import POJOS.Com_data_client;
import POJOS.Patient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 *
 * @author ALVARO
 */
public class ClientThreadsServer implements Runnable {

    private Com_data_client com_data_client;
    private ArrayList<Patient> patients; //Patients that are going to be got from the server

    /**
     * Empty (default) constructor.
     */
    public ClientThreadsServer() {
    }

    @Override
    public synchronized void run() {
        try {

            ObjectInputStream objectInputStream = com_data_client.getObjectInputStream();
            Object tmp;
            System.out.println("Before order");
            //Instruction received
            String instruction;
            tmp = objectInputStream.readObject();//we receive the instruction
            instruction = (String) tmp;

            System.out.println("Order received");
            switch (instruction) {
                case "RECEIVE_PATIENT_LIST": { //For login
                    System.out.println(instruction + " option running");

                    tmp = objectInputStream.readObject();//we receive the patientS
                    patients = (ArrayList<Patient>) tmp;
                    if (patients != null) {
                        System.out.println("Patients received: " + patients);
                    } else {
                        System.out.println("Null list: " + patients);
                    }
                    notify(); //we awake thread to get data
                    break;
                }
                default: {
                    System.out.println("Error");
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client closed");
        }
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    

    public void setCom_data_client(Com_data_client com_data_client) {
        this.com_data_client = com_data_client;
    }

}
