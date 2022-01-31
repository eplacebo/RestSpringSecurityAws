package com.eplacebo.springapi.service;

import com.eplacebo.springapi.model.Event;
import com.eplacebo.springapi.model.Operation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface EventService {

    Event getById(Long id);

    Event save(Event event);

    void uploadEvent(String username, MultipartFile multipartFile, Operation operation);

    void downloadEvent(String username, String key, Operation operation);

    void deleteEvent(String username, String key, Operation operation);

    void deleteById(Long id);

    List<Event> getAll();

    List<Event> findEventsByUsername(String username);
}
