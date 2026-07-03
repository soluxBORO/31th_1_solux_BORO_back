package com.boro.domain.chat.repository;

import com.boro.domain.chat.entity.ChatMessage;
import com.boro.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("""
    SELECT cm
    FROM ChatMessage cm
    WHERE cm.chatRoom = :chatRoom
    ORDER BY cm.createdAt DESC
    """)
    List<ChatMessage> findAllByChatRoomOrderByCreatedAtDesc(
            @Param("chatRoom") ChatRoom chatRoom
    );
}
