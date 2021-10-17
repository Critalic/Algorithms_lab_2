package com.DataBase;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBControlSystem {
    private int currentDataLine =0;
    private final String path;
    private ArrayList<String[]> indexArea;
    private final ArrayList<String> overflowArea = new ArrayList<>();

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
        insertRecord(record);
        updateIndexArea();
        currentDataLine++;
    }

    void deleteRecord(String record) throws IOException {
        String[] cridentials = record.split("-");
        int group = Integer.parseInt(cridentials[0]);
        int id = Integer.parseInt(cridentials[1]);
        if(group==27) {
            int i = Math.floorDiv(overflowArea.size(), 2);
            overflowArea.remove(binarysearch(overflowArea, i, i, id));
        } else {
            List<String> bucket= Arrays.asList(indexArea.get(group));
            int i = Math.floorDiv(bucket.size(), 2);
            indexArea.get(group)[binarysearch(bucket, i, i, id)] = null;
        }
        updateIndexArea();
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

    private void updateIndexArea() throws IOException {
        Path p = Paths.get(path + "\\index.txt");
        ArrayList<String> temp = new ArrayList<>();
        for (String[] l: indexArea) {
            temp.addAll(Arrays.asList(l));
            temp.add(" ");
        }
        temp.addAll(overflowArea);
        Files.write(p, temp, StandardCharsets.UTF_8);
    }

    private void initializeIndexArea() {
        this.indexArea = new ArrayList<>();
        for(int i=0; i<27; i++) {
            indexArea.add(new String[11]);
        }
    }

    private void insertRecord(String record) {
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
        stringBuilder.append("-").append(currentDataLine + record.charAt(0))
                     .append(" | ").append(currentDataLine);
        int indexToAdd = findFreeSpace(indexArea.get(group));
        if(indexToAdd==-1) {
            stringBuilder.replace(0, 2, "27");
            overflowArea.add(stringBuilder.toString());
        }
        else indexArea.get(group)[indexToAdd] = stringBuilder.toString();
    }

    public int binarysearch(List<String> arrayList, int current, int change, int id) {
        int curr = parseIndex(arrayList.get(current));
        if(curr==id) return current;
        if(curr<id) {
            return binarysearch(arrayList, current + (Math.floorDiv(change, 2)+1), Math.floorDiv(change, 2),id);
        }
        else {
            return binarysearch(arrayList, current - (Math.floorDiv(change, 2)+1), Math.floorDiv(change,2), id);
        }
    }
    private int parseIndex (String index) {
        if (index ==null) return 999999;
        String id = index.split(" | ")[0];
        return Integer.parseInt(id.split("-")[1]);
    }

    private int findFreeSpace (String[] arr) {
        for(int i=0; i<arr.length; i++) {
            if(arr[i]==null) return i;
        }
        return -1;
    }
}
