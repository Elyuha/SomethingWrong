package com.javamaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToMany
    @JoinTable(name = "post_table",
    joinColumns =  @JoinColumn(name = "message_id"),
    inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
    @JsonManagedReference
    Set<HashtagEntity> hashtagEntitySet;

}
