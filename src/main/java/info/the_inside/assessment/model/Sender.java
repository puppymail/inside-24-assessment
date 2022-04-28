package info.the_inside.assessment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "sender")
public class Sender {

    @Id
    @NonNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NonNull
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "token")
    private String token = null;

    @OneToMany(mappedBy = "sender", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

}
