package com.eplacebo.springapi.rest;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.eplacebo.springapi.model.File;
import com.eplacebo.springapi.model.Operation;
import com.eplacebo.springapi.service.EventService;
import com.eplacebo.springapi.service.FileService;
import com.eplacebo.springapi.service.UserService;
import com.eplacebo.springapi.service.impl.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
public class FileRestControllerV1 {

    private S3Service s3Service;
    private EventService eventService;
    private UserService userService;
    private FileService fileService;

    @Autowired
    public FileRestControllerV1(S3Service s3Service, EventService eventService, UserService userService, FileService fileService) {
        this.s3Service = s3Service;
        this.eventService = eventService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @PreAuthorize("hasAuthority('user:read')")
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<File> getFile(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        File file = this.fileService.getById(id);

        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<File> saveFile(@RequestBody @Valid File file) {
        HttpHeaders headers = new HttpHeaders();

        if (file == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        this.fileService.save(file);
        return new ResponseEntity<>(file, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<File> updateFile(@RequestBody @Valid File file, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();

        if (file == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        this.fileService.save(file);

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<File> deleteFile(@PathVariable("id") Long id) {
        File file = this.fileService.getById(id);

        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.fileService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAuthority('user:read')")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<File>> getAllFiles() {
        List<File> files = this.fileService.getAll();

        if (files.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:upload')")
    @RequestMapping(value = "/aws", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<File> uploadFile(@RequestBody MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();

        if (file == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        s3Service.uploadFile(file);
        eventService.uploadEvent(userService.getUsernameByAuth(), file, Operation.Upload);
        return new ResponseEntity<>(fileService.getFileByName(file.getOriginalFilename()), headers, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:download')")
    @RequestMapping(value = "/aws/{filename}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<File> downloadFile(@PathVariable("filename") String fileName) {
        HttpHeaders headers = new HttpHeaders();

        if (fileName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        eventService.downloadEvent(userService.getUsernameByAuth(), fileName, Operation.Download);
        s3Service.downloadFile(fileName);
        return new ResponseEntity<>(fileService.getFileByName(fileName), headers, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @RequestMapping(value = "/aws/{filename}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<File> deleteAwsFile(@PathVariable("filename") String filename) {
        File file = this.fileService.getFileByName(filename);

        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        eventService.deleteEvent(userService.getUsernameByAuth(), filename, Operation.Delete);
        s3Service.deleteFile(filename);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAuthority('user:full_read')")
    @RequestMapping(value = "/aws", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<S3ObjectSummary>> getAllAwsFiles() {
        List<S3ObjectSummary> files = this.s3Service.listFiles();

        if (files.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(files, HttpStatus.OK);
    }
}


