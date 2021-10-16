package com.DataBase;

import java.io.*;

public class DBControlSystem {
    private int currentDataLine =0;
    private final String path;

    DBControlSystem(String path) throws IOException {
        this.path = path;
        establishConnction(path);
    }



    void addNewrecord(String record) throws IOException {
        try (FileWriter fw = new FileWriter(path + "\\data.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter printWriter = new PrintWriter(bw)) {
            printWriter.println(currentDataLine + " | " + record );
        }
        try (FileWriter fw = new FileWriter(path + "\\index.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter printWriter = new PrintWriter(bw)) {
            printWriter.println(currentDataLine + " | " + record );
        }
        currentDataLine++;
    }
    boolean deleteRecord(String record) { return false;}
    boolean editRecord(String record) { return false;}

    private void establishConnction(String path) throws IOException {
        File indexes = new File(path + "\\index.txt");
        File data = new File(path + "\\data.txt");
        if(!createFiles(indexes, data)) {
            if(!(indexes.delete() && data.delete())) throw new IOException("Failed to establish the connection");
            if(!createFiles(indexes, data)) throw new IOException("Failed to establish the connection");
        }

    }

    private boolean createFiles(File indexes, File data) throws IOException {
        return indexes.createNewFile() && data.createNewFile();
    }
}
