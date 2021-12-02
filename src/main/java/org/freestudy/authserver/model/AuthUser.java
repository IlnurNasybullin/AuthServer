package org.freestudy.authserver.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@EqualsAndHashCode(of = {"id", "login"})
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            joinColumns = @JoinColumn(
                    name = "auth_user_id",
                    nullable = false
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_role_id",
                    nullable = false
            ),
            uniqueConstraints = @UniqueConstraint(
                    name = "auth_user_role_unique_constraint",
                    columnNames = {"auth_user_id", "user_role_id"}
            )
    )

    @Builder.Default
    private Set<UserRole> roles = new HashSet<>();

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default
    private boolean isActive = true;
}
