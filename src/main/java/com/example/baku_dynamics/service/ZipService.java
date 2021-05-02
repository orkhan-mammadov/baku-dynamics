package com.example.baku_dynamics.service;

import com.example.baku_dynamics.entity.TaskStatus;
import com.example.baku_dynamics.entity.ZipEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipService {
    public HashMap<Integer, ZipEntity> myLocal = new HashMap<>();

    public int directoryZip(ZipEntity zipEntity) {

        String sourceFile = zipEntity.getFilePath();
        Random random = new Random();
        int taskId = random.nextInt(Integer.MAX_VALUE);

        ZipEntity taskEntity = new ZipEntity();
        myLocal.put(taskId, taskEntity);

        Thread thread = new Thread(()->{
            try{
                File fileToZip = new File(sourceFile);
                String path = fileToZip.getParent()+"/"+fileToZip.getName().replaceFirst("[.][^.]+$", "")+".zip";
                FileOutputStream fos = new FileOutputStream(path);
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                taskEntity.setTaskStatus(TaskStatus.IN_PROGRESS);
                taskEntity.setFilePath(path);
                myLocal.put(taskId,taskEntity);
                zipFile(fileToZip, fileToZip.getName(), zipOut);
                zipOut.close();
                fos.close();
                Thread.sleep(10000);
                taskEntity.setTaskStatus(TaskStatus.COMPLETED);
                myLocal.put(taskId,taskEntity);
            }
            catch (Exception e){
                taskEntity.setTaskStatus(TaskStatus.FAILED);
                taskEntity.setFilePath(null);
                myLocal.put(taskId,taskEntity);
                e.printStackTrace();
            }
            System.out.println(">>>Task id: "+taskId);
            System.out.println(">>>Task status: "+myLocal.get(taskId).getTaskStatus());
            System.out.println(">>>File path: "+myLocal.get(taskId).getFilePath());
        });
        thread.start();

        return taskId;
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
            }
            zipOut.closeEntry();
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    public ZipEntity getResult(int id) {
        return myLocal.get(id);
    }
}
