package com.eplacebo.springapi.dto;


import com.eplacebo.springapi.model.Event;
import com.eplacebo.springapi.model.File;
import com.eplacebo.springapi.model.Operation;
import lombok.Data;

import java.util.Date;

@Data
public class EventDto {

    private Long id;
    private Operation operation;
    private Date created;
    private File file;

    public Event toEvent() {
        Event event = new Event();
        event.setId(id);
        event.setOperation(operation);
        event.setCreated(created);
        event.setFile(file);
        return event;
    }

    public static EventDto fromEvent(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setOperation(event.getOperation());
        event.setCreated(event.getCreated());
        event.setFile(event.getFile());
        return eventDto;
    }
}