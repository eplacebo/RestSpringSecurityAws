package com.eplacebo.springapi.service.impl;

import com.eplacebo.springapi.model.Event;
import com.eplacebo.springapi.model.File;
import com.eplacebo.springapi.model.Operation;
import com.eplacebo.springapi.repository.EventRepository;
import com.eplacebo.springapi.repository.FileRepository;
import com.eplacebo.springapi.repository.UserRepository;
import com.eplacebo.springapi.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    EventRepository eventRepository;
    UserRepository userRepository;
    FileRepository fileRepository;
    S3Service s3Service;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, FileRepository fileRepository, S3Service s3Service) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.s3Service = s3Service;
    }

    @Override
    public Event getById(Long id) {
        return eventRepository.getOne(id);
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void uploadEvent(String username, MultipartFile multipartFile, Operation operation) {
        if (fileRepository.getFileByFileName(multipartFile.getOriginalFilename()) == null) {
            fileRepository.save(new File(s3Service.getUrlFile(multipartFile), multipartFile.getOriginalFilename()));
        }
        eventRepository.save(new Event(userRepository.findUserByUsername(username).orElse(null), fileRepository.getFileByFileName(multipartFile.getOriginalFilename()), operation));
    }

    @Override
    public void downloadEvent(String username, String key, Operation operation) {
        eventRepository.save(new Event(userRepository.findUserByUsername(username).orElse(null), fileRepository.getFileByFileName(key), operation));
    }

    @Override
    @Transactional
    public void deleteEvent(String username, String key, Operation operation) {
        eventRepository.save(new Event(userRepository.findUserByUsername(username).orElse(null), fileRepository.getFileByFileName(key), Operation.Delete));
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> findEventsByUsername(String username) {
        return eventRepository.findEventsByUsername(username);
    }
}
