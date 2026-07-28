package com.boro.domain.chat.repository;

import com.boro.domain.chat.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

    @Query("""
        select cm.member.id
        from ChatMember cm
        where cm.chatRoom.id = :roomId
          and cm.member.id <> :senderId
        """)
    Optional<Long> findOpponentId(@Param("roomId") Long roomId, @Param("senderId") Long senderId);


    Optional<ChatMember> findByChatRoom_IdAndMember_Id(Long chatRoomId, Long memberId);
}
