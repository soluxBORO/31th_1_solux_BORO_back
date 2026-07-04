package com.boro.domain.chat.repository;

import com.boro.domain.chat.entity.ChatRoom;
import com.boro.domain.chat.entity.enums.ChatRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("""
    SELECT DISTINCT cr
    FROM ChatRoom cr
    JOIN cr.chatMemberList crm
    WHERE crm.member.id = :memberId
      AND cr.chatRoomType = :chatRoomType
    ORDER BY cr.lastMessageAt DESC
    """)
    List<ChatRoom> findAllByMemberIdAndChatRoomType(
            @Param("memberId") Long memberId,
            @Param("chatRoomType") ChatRoomType chatRoomType
    );
}
