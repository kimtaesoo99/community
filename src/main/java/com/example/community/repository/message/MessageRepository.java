package com.example.community.repository.message;

import com.example.community.domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query("select m from Message m join fetch m.receiver r " +
        "where m.deletedByReceiver=false and r.username =:username")
    List<Message> findAllByReceiverQuery(@Param("username") String username);

    @Query("select m from Message m join fetch  m.receiver r " +
        "where m.deletedByReceiver = false and m.id =:id and r.username=:username")
    Optional<Message> findByIdWithReceiver(@Param("id") Long id, @Param("username") String username);

    @Query("select m from Message m join fetch m.sender r " +
        "where m.deletedBySender=false and r.username =:username")
    List<Message> findAllBySenderQuery(@Param("username") String username);

    @Query("select m from Message m join fetch  m.sender r " +
        "where m.deletedBySender = false and m.id =:id and r.username=:username")
    Optional<Message> findByIdWithSender(@Param("id") Long id, @Param("username") String username);



}
