package info.the_inside.messages.repository;

import info.the_inside.messages.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> getBySender_Name(String name);

    List<Message> getBySender_NameOrderByCreatedAtDesc(String name);

    List<Message> getAllByOrderByCreatedAtDesc();

    @Query(value = "SELECT * FROM Message m ORDER BY m.created_at DESC LIMIT :size",
           nativeQuery = true)
    List<Message> getTopMessages(@Param("size") int historySize);

}
