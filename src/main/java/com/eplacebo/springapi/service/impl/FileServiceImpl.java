package com.eplacebo.springapi.service.impl;

import com.eplacebo.springapi.model.File;
import com.eplacebo.springapi.repository.FileRepository;
import com.eplacebo.springapi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileRepository fileRepository;

    @Override
    public File getById(Long id) {
        return fileRepository.getById(id);
    }

    @Override
    public File save(File file) {
        return fileRepository.save(file);
    }

    @Override
    public void delete(Long id) {
        fileRepository.deleteById(id);
    }

    @Override
    public List<File> getAll() {
        return fileRepository.findAll();
    }

    @Override
    public File getFileByName(String name) {
        return fileRepository.getFileByFileName(name);
    }

}

