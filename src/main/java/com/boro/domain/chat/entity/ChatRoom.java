package com.boro.domain.chat.entity;

import com.boro.domain.chat.entity.enums.ChatRoomType;
import com.boro.domain.post.entity.Post;
import com.boro.domain.rentalrequest.entity.RentalRequest;
import com.boro.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chat_room")
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    private String chatRoomName;

    @Enumerated(EnumType.STRING)
    private ChatRoomType chatRoomType;

    private String lastMessageContent;

    private LocalDateTime lastMessageAt;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_request_id")
    private RentalRequest rentalRequest;

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMember> chatMemberList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    public void updateLastMessageContent(String lastMessageContent, LocalDateTime lastMessageAt) {
        this.lastMessageContent = lastMessageContent;
        this.lastMessageAt = lastMessageAt;
    }

    public void addChatMessage(ChatMessage chatMessage){
        chatMessageList.add(chatMessage);
        chatMessage.setChatRoom(this);
    }

    public void addChatMember(ChatMember chatMember){
        chatMemberList.add(chatMember);
        chatMember.setChatRoom(this);
    }

}
