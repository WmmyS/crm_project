package com.wesley.crm.app.services;

import com.wesley.crm.exceptions.CrmException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};

    public String uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CrmException("Arquivo não pode estar vazio");
        }

        // Validar tamanho
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new CrmException("Arquivo muito grande. Máximo 5MB");
        }

        // Validar extensão
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isValidImageExtension(originalFilename)) {
            throw new CrmException("Formato de arquivo inválido. Use: jpg, jpeg, png, gif");
        }

        try {
            // Criar diretório se não existir
            Path uploadPath = Paths.get(uploadDir, "clientes");
            Files.createDirectories(uploadPath);

            // Gerar nome único
            String extension = getFileExtension(originalFilename);
            String filename = UUID.randomUUID().toString() + "." + extension;
            Path filePath = uploadPath.resolve(filename);

            // Salvar arquivo
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Retornar URL relativa
            return "/uploads/clientes/" + filename;

        } catch (IOException e) {
            throw new CrmException("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    public Resource getImage(String filename) {
        try {
            Path filePath = Paths.get(uploadDir, "clientes", filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new CrmException("Imagem não encontrada: " + filename);
            }
        } catch (Exception e) {
            throw new CrmException("Erro ao carregar imagem: " + e.getMessage());
        }
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        try {
            // Remover prefixo da URL para obter o caminho do arquivo
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(uploadDir, "clientes", filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log do erro, mas não falha a operação
            System.err.println("Erro ao deletar arquivo: " + e.getMessage());
        }
    }

    private boolean isValidImageExtension(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "";
    }
}