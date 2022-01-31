package com.eplacebo.springapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "files")
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "name")
    private String fileName;

    @OneToMany(mappedBy = "user")
    private List<Event> events;

    public File(String location, String fileName) {
        this.location = location;
        this.fileName = fileName;
    }
}
