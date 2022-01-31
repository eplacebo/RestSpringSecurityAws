package com.eplacebo.springapi.repository;

import com.eplacebo.springapi.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    File getFileByFileName(String name);
}