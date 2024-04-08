package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.DataFormProject;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;

import java.io.*;

// Сервис для записи в файл и восстановлении из файла данных проекта
@Service
@RequiredArgsConstructor
public class DataFormProjectService {

    private final String filePathSave = "dataFormProjectSave/save.ser";
    private final String filePathRecover = "dataFormProjectSave/recover.ser";

    /**
     * Метод сохранения в файл данных проекта
     * @param dataFormProject данные проекта
     */
    public void dataFormProjectSave(DataFormProject dataFormProject){
        try {
            FileOutputStream outputStream = new FileOutputStream(filePathSave);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(dataFormProject);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод восстановления из файла данных проекта
     * @return данные проекта
     */
    public DataFormProject dataFormProjectRecover(){
        DataFormProject dataFormProject;
        try {
            FileInputStream fileInputStream = new FileInputStream(filePathRecover);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            dataFormProject = (DataFormOilPad) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return dataFormProject;
    }

    public String getFilePathSave(){
        return filePathSave;
    }

    public String getFilePathRecover() {
        return filePathRecover;
    }
}
