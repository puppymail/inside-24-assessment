package info.the_inside.messages.repository;

import info.the_inside.messages.model.Sender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SenderRepository extends JpaRepository<Sender, String> {

    boolean existsByName(String name);

}
