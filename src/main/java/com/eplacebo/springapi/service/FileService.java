package com.eplacebo.springapi.service;


import com.eplacebo.springapi.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface FileService {
    File getById(Long id);

    File save(File file);

    void delete(Long id);

    List<File> getAll();

    File getFileByName(String name);
}
