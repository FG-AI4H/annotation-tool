package org.fgai4h.ap.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID userUUID;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "annotator_role_annotator_uuid")
    private AnnotatorEntity annotatorRole;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "reviewer_role_ID")
    private ReviewerEntity reviewerRole;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "supervisor_role_ID")
    private SupervisorEntity supervisorRole;

    private String idpID;
    private String username;
    private Date birthdate;

}
