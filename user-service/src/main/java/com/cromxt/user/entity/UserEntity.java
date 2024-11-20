package com.cromxt.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "cuser")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class UserEntity implements UserDetails , Serializable {

    @Serial
    private static final long serialVersionUID = 274678156711667162L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false,name = "first_name")
    private String firstName;
    @Column(nullable = false,name = "last_name")
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(nullable = false,updatable = false)
    private Date birthdate;
    @Enumerated(EnumType.STRING)
    private Role role;
    @CreationTimestamp
    @Column(nullable = false,name = "created_on",updatable = false)
    private Timestamp createdOn;
    @UpdateTimestamp
    @Column(nullable = false,name = "updated_on")
    private Timestamp updatedOn;

    @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE)
    private RecoveryAccountDetails recoveryAccountDetails;

    @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE)
    private ProfileAvatar profileAvatar;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
