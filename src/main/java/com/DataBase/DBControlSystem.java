package com.DataBase;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DBControlSystem {
    private int currentDataLine =0;
    private final String path;
    private ArrayList<ArrayList<String>> indexArea;

    DBControlSystem(String path) throws IOException {
        initializeIndexArea();
        this.path = path;
        establishConnection(path);
    }



    void addNewrecord(String record) throws IOException {
        try (FileWriter fw = new FileWriter(path + "\\data.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter printWriter = new PrintWriter(bw)) {
            printWriter.println(currentDataLine + " | " + record );
        }
        updateIndexArea(record);
        Path p = Paths.get(path + "\\index.txt");
        ArrayList<String> temp = new ArrayList<>();
        for (ArrayList<String> l: indexArea) {
            temp.addAll(l);
            temp.add(" ");
        }
        Files.write(p, temp, StandardCharsets.UTF_8);
        currentDataLine++;
    }
    void deleteRecord(String record) {

    }
    boolean editRecord(String record) { return false;}

    private void establishConnection(String path) throws IOException {
        File indexes = new File(path + "\\index.txt");
        File data = new File(path + "\\data.txt");
        if(createFiles(indexes, data)) {
            if(!(indexes.delete() && data.delete())) throw new IOException("Failed to establish the connection");
            if(createFiles(indexes, data)) throw new IOException("Failed to establish the connection");
        }

    }

    private boolean createFiles(File indexes, File data) throws IOException {
        return !indexes.createNewFile() || !data.createNewFile();
    }

    private void initializeIndexArea() {
        this.indexArea = new ArrayList<>();
        for(int i=0; i<27; i++) {
            indexArea.add(new ArrayList<>());
        }
    }

    private void updateIndexArea (String record) {
        StringBuilder stringBuilder = new StringBuilder();
        int l1 = record.charAt(0);
        int group;
        if(l1>96) {
            group = l1-97;
            stringBuilder.append(group);
        }
        else if(l1>64) {
            group = l1-65;
            stringBuilder.append(group);
        }
        else {
            group = 26;
            stringBuilder.append(group);
        }
        if(stringBuilder.length()==1) stringBuilder.insert(0, 0);
        stringBuilder.append("-").append(currentDataLine+record.charAt(0));
        indexArea.get(group).add(stringBuilder + " | " + currentDataLine);
    }
}
