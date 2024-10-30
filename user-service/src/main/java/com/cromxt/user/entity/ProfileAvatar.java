package com.cromxt.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "avatar")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProfileAvatar {

    @Id
    @Column(name = "cuser_id")
    private String id;

//    UserEntity id is used here as primary key so here create a OneToOne Relationship.
    @OneToOne
    @MapsId
    @JoinColumn(name = "cuser_id")
    private UserEntity user;
    private String url;
    private String format;
    private String imgSize;
}
