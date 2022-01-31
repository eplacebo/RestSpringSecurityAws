package com.eplacebo.springapi.repository;

import com.eplacebo.springapi.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "select * from public.events where id_user = (select id from public.users where username = ?1)", nativeQuery = true)
    List<Event> findEventsByUsername(String username);
}
