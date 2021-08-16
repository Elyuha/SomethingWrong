package com.javamaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "message_table")
@Data
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String mess;

    @Column
    private LocalDateTime date;

    @ManyToOne
    @JsonIgnore
    @JoinColumn
    UserEntity userEntity;

}
