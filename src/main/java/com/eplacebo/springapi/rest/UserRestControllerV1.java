package com.eplacebo.springapi.rest;

import com.eplacebo.springapi.dto.EventDto;
import com.eplacebo.springapi.model.Event;
import com.eplacebo.springapi.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/user")
public class UserRestControllerV1 {

    @Autowired
    private EventService eventService;

    @PreAuthorize("hasAuthority('user:read')")
    @RequestMapping(value = "/events/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<EventDto> getEvent(@PathVariable("id") Long id) {

        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Event event = this.eventService.getById(id);

        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        EventDto eventDto = EventDto.fromEvent(event);

        return new ResponseEntity<>(eventDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:read')")
    @RequestMapping(value = "/events", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<Event> events = this.eventService.getAll();

        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<EventDto> result = events.stream().map(EventDto::fromEvent).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
