package com.eplacebo.springapi.rest;

import com.eplacebo.springapi.model.Event;
import com.eplacebo.springapi.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventRestControllerV1 {

    @Autowired
    private EventService eventService;

    @PreAuthorize("hasAuthority('user:full_read')")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = this.eventService.getAll();

        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:full_read')")
    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Event>> getAllEventsByUsername(@PathVariable("username") String username) {
        List<Event> events = this.eventService.findEventsByUsername(username);

        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/{id}")
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Event> getEvent(@PathVariable("id") Long eventId) {
        if (eventId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Event event = this.eventService.getById(eventId);

        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Event> saveEvent(@RequestBody @Valid Event event) {
        HttpHeaders headers = new HttpHeaders();

        if (event == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        this.eventService.save(event);
        return new ResponseEntity<>(event, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Event> deleteEvent(@PathVariable("id") Long id) {
        Event event = this.eventService.getById(id);

        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.eventService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Event> updateEvent(@RequestBody @Valid Event event, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();

        if (event == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        this.eventService.save(event);

        return new ResponseEntity<>(event, headers, HttpStatus.OK);
    }
}

