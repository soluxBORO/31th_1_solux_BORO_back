package com.boro.domain.chat.repository;

import com.boro.domain.chat.entity.ChatMessage;
import com.boro.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    Optional<ChatMessage> findTopByChatRoomIdOrderByIdDesc(@Param("chatRoomId") Long chatRoomId);

    @Query("""
        select count(message.id)
        from ChatMessage message
        join ChatMember mine
            on mine.chatRoom = message.chatRoom
           and mine.member.id = :memberId
        where message.chatRoom.id = :roomId
          and message.member.id <> :memberId
          and message.id > coalesce(mine.lastReadMessage.id, 0)
        """)
    long countUnreadMessages(@Param("roomId") Long roomId, @Param("memberId") Long memberId);
}
