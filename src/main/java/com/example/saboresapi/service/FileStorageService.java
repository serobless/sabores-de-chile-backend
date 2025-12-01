package com.example.saboresapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo crear el directorio para almacenar archivos", ex);
        }
    }

    // Guardar archivo con nombre único
    public String storeFile(MultipartFile file) {
        // Validar nombre del archivo
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.contains("..")) {
            throw new RuntimeException("Nombre de archivo inválido: " + originalFileName);
        }

        try {
            // Generar nombre único usando UUID
            String fileExtension = "";
            int dotIndex = originalFileName.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = originalFileName.substring(dotIndex);
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // Copiar archivo al directorio de destino
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo almacenar el archivo " + originalFileName, ex);
        }
    }

    // Cargar archivo como Resource
    public Resource loadFileAsResource(String fileName) {
        try {
            // Validar nombre del archivo
            if (fileName.contains("..")) {
                throw new RuntimeException("Nombre de archivo inválido: " + fileName);
            }

            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Archivo no encontrado: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Archivo no encontrado: " + fileName, ex);
        }
    }

    // Eliminar archivo
    public boolean deleteFile(String fileName) {
        try {
            // Validar nombre del archivo
            if (fileName == null || fileName.contains("..")) {
                return false;
            }

            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo eliminar el archivo: " + fileName, ex);
        }
    }
}
