package com.antiques.app.modules.person.persistence;

import java.util.Set;

import com.antiques.app.modules.personType.persistence.PersonType;
import com.antiques.app.persistence.Audit;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name="person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_person")
    private Long personId;

    @Column(unique = true)
    String username;
    
    String password;

    @Column(name = "is_enable")
    boolean isEnable;

    @Column(name = "account_no_expired")
    boolean accountNoExpired;

    @Column(name = "account_no_locked")
    boolean accountNoLocked;

    @Column(name = "credential_no_expired")
    boolean credentialNoExpired;

    @ManyToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "person_type_person",
        joinColumns = @JoinColumn (name = "id_person" ,referencedColumnName = "id_person"),
        inverseJoinColumns = @JoinColumn (name = "id_type_person", referencedColumnName = "id_type_person")
    )
    private Set<PersonType> personTypes;

    @Embedded
    private final Audit audit = new Audit();

    @PrePersist
    public void prePersist() {
        audit.prePersistAudit();
    }
    
    @PreUpdate
    public void preUpdate() {
        audit.preUpdateAudit();
    }
}