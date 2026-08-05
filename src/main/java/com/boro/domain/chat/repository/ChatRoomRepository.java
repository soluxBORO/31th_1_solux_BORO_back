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
        count(unreadMessage.id),
        i.title,
        null,
        null,
        null,
        null,
        null,
        null
    )
    from ChatRoom cr
    join RentalRequest rr
        on cr.rentalRequest = rr
    join Post p
        on rr.post = p
    left join p.item i
    join ChatMember me
        on me.chatRoom = cr
    join ChatMember other
        on other.chatRoom = cr
    join Member opponent
        on opponent = other.member
    left join ChatMessage unreadMessage
        on unreadMessage.chatRoom = cr
        and unreadMessage.id > coalesce(me.lastReadMessage.id, 0)
        and unreadMessage.member.id <> :memberId
    where me.member.id = :memberId
      and other.member.id <> :memberId
      and cr.chatRoomType = com.boro.domain.chat.entity.enums.ChatRoomType.ITEM
    group by
        cr.id,
        opponent.nickname,
        opponent.profileUrl,
        cr.lastMessageContent,
        cr.lastMessageAt,
        p.postCategory,
        i.title
    order by cr.lastMessageAt desc
    """)
    List<ChatRoomPreview> findItemChatRoomList(
            Long memberId
    );

    @Query("""
    select new com.boro.domain.chat.dto.response.ChatRoomPreview(
        cr.id,
        opponent.nickname,
        null,
        null,
        null,
        null,
        null,
        es.location,
        es.floor,
        es.seatNumber,
        es.hasPowerOutlet,
        es.hasWindowSeat,
        es.expectedCheckoutTime
    )
    from ChatRoom cr
    join RentalRequest rr
        on cr.rentalRequest = rr
    join Post p
        on rr.post = p
    left join p.emptySpot es
    join ChatMember me
        on me.chatRoom = cr
    join ChatMember other
        on other.chatRoom = cr
    join Member opponent
        on opponent = other.member
    where me.member.id = :memberId
      and other.member.id <> :memberId
      and cr.chatRoomType = com.boro.domain.chat.entity.enums.ChatRoomType.EMPTY_SPOT
    order by cr.lastMessageAt desc
    """)
    List<ChatRoomPreview> findEmptySpotChatRoomList(
            Long memberId
    );


}
