package org.imgoing.api.entity;

import lombok.*;
import org.imgoing.api.config.BaseTime;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_tb")
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(length = 50, nullable = false, unique = true)
    private String phone;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    private String profile;
}
