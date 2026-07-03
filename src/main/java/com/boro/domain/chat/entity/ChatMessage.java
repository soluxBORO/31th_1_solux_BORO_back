package com.boro.domain.chat.entity;

import com.boro.domain.chat.entity.enums.ChatMessageType;
import com.boro.domain.member.entity.Member;
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
@Table(name = "chat_message")
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    private String content;

    private LocalDateTime readAt;

    @Enumerated(EnumType.STRING)
    private ChatMessageType chatMessageType;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "chatMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageImage> chatMessageImageList = new ArrayList<>();

    public void addChatMessageImage(ChatMessageImage chatMessageImage){
        chatMessageImageList.add(chatMessageImage);
        chatMessageImage.setChatMessage(this);
    }
}

