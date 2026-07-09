package com.boro.domain.chat.repository;

import com.boro.domain.chat.dto.response.ChatRoomPreview;
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

    @Query("""
    select new com.boro.domain.chat.dto.response.ChatRoomPreview(
        cr.id,
        opponent.nickname,
        opponent.profileUrl,
        cr.lastMessageContent,
        cr.lastMessageAt,
        me.unreadCount
    )
    from ChatRoom cr
    join ChatMember me
        on me.chatRoom = cr
    join ChatMember other
        on other.chatRoom = cr
    join Member opponent
        on opponent = other.member
    where me.member.id = :memberId
      and other.member.id <> :memberId
      and cr.chatRoomType = :chatRoomType
    order by cr.lastMessageAt desc
    """)
    List<ChatRoomPreview> findChatRoomList(
            Long memberId, ChatRoomType chatRoomType
    );
}
