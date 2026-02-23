package com.alepiga;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static java.net.InetAddress.getByName;

public class Agent {
    public static void main(String[] args) {
        try {
            while(true){
                // Variabili di telemetria
                SystemInfo si =  new SystemInfo();
                GlobalMemory gmemory = si.getHardware().getMemory();
                long totalMemory = gmemory.getTotal();
                long usedMemory = gmemory.getTotal() - gmemory.getAvailable();
                String os = si.getOperatingSystem().toString(); // Per stampare tutti i dettagli sul Sistema Operativo
                long time = System.currentTimeMillis();

                // Creo una stringa e i byte che la compongono
                String s = time + ";" + totalMemory + ";" + usedMemory + ";" + os;
                byte[] buffer = s.getBytes();

                // Invio il pacchetto
                InetAddress address = getByName("127.0.0.1");
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 9000);
                DatagramSocket socket = new DatagramSocket();
                socket.send(packet);

                // Attende cinque secondi
                Thread.sleep(5000);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
