package com.alepiga;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Server {
    // Processo Main che avvia il thread
    public static void main(String[] args) {
        try{
            DatagramSocket socket = new DatagramSocket(9000); // Creo un nuovo socket UDP
            while(true){
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                socket.receive(packet);
                new Thread(() -> handleClient(packet)).start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // Per convertire i tick della RAM in GB
    public static double convertiGB(long valore){
        double divisore = Math.pow(1024, 3);
        return (double)valore / divisore;
    }

    // Per convertire i millisecondi dal 1/1/1970 in stringa
    public static String timestampToString(long timestamp) {
        LocalDateTime dateTime = Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    // Thread principale, riceve e stampa i pacchetti
    public static void handleClient(DatagramPacket packet){
        // Creazione stringa
        String s = new String(packet.getData(), 0, packet.getLength());
        String[] parts = s.split(";");

        // Dati parsati dall'agent
        long time = Long.parseLong(parts[0]);
        long totalMemory = Long.parseLong(parts[1]);
        long usedMemory = Long.parseLong(parts[2]);
        String os = parts[3];
        String sFinal = String.format(
                "Timestamp: %s\nMemoria totale: %.2f GB\nMemoria usata: %.2f GB\nSistema operativo: %s\n",
                timestampToString(time),
                convertiGB(totalMemory),
                convertiGB(usedMemory),
                os
        );
        System.out.println(sFinal); // Stampa stringa finale
    }
}
