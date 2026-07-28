package com.boro.domain.chat.converter;

import com.boro.domain.chat.dto.request.ChatRequestDTO;
import com.boro.domain.chat.dto.response.ChatResponseDTO;
import com.boro.domain.chat.dto.response.ChatRoomPreview;
import com.boro.domain.chat.entity.ChatMember;
import com.boro.domain.chat.entity.ChatMessage;
import com.boro.domain.chat.entity.ChatMessageImage;
import com.boro.domain.chat.entity.ChatRoom;
import com.boro.domain.chat.entity.enums.ChatRoomType;
import com.boro.domain.member.entity.Member;
import com.boro.domain.post.entity.Post;
import com.boro.domain.post.entity.enums.PostCategory;

import java.time.LocalDateTime;
import java.util.List;

public class ChatConverter {

    public static ChatRoom toChatRoom(ChatRequestDTO.ChatRoom request){
        return ChatRoom.builder()
                .chatRoomType(request.chatRoomType())
                .build();
    }

    public static ChatRoom toChatRoomTest(ChatRequestDTO.ChatRoomTest request){
        return ChatRoom.builder()
                .chatRoomName(request.chatRoomName())
                .chatRoomType(request.chatRoomType())
                .build();
    }

    public static ChatMember toChatMember(ChatRoom chatRoom, Member member){
        return ChatMember.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
    }

    public static ChatMessage toChatMessage(ChatRequestDTO.ChatMessage request, Member member){
        return ChatMessage.builder()
                .content(request.content())
                .chatMessageType(request.chatMessageType())
                .member(member)
                .build();
    }

    public static ChatMessageImage toChatMessageImage(String imageUrl){
        return ChatMessageImage.builder()
                .imageUrl(imageUrl)
                .build();
    }

    public static List<String> toChatMessageImageUrls(List<ChatMessageImage> images){
        return images.stream()
                .map(ChatMessageImage::getImageUrl)
                .toList();
    }

    public static ChatResponseDTO.ChatMessage toChatMessageDTO(ChatMessage chatMessage){
        return ChatResponseDTO.ChatMessage.builder()
                .roomId(chatMessage.getChatRoom().getId())
                .chatMessageType(chatMessage.getChatMessageType())
                .memberId(chatMessage.getMember().getId())
                .content(chatMessage.getContent())
                .imageUrls(toChatMessageImageUrls(chatMessage.getChatMessageImageList()))
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }

    public static ChatResponseDTO.ChatMessageDetail toChatMessageDetail(ChatMessage chatMessage){
        return ChatResponseDTO.ChatMessageDetail.builder()
                .chatMessageType(chatMessage.getChatMessageType())
                .memberId(chatMessage.getMember().getId())
                .content(chatMessage.getContent())
                .imageUrls(toChatMessageImageUrls(chatMessage.getChatMessageImageList()))
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }

    public static ChatResponseDTO.ChatMessageList toChatMessageList(
            ChatRoom chatRoom, List<ChatMessage> chatMessageList, Member opponent
    ){
        List<ChatResponseDTO.ChatMessageDetail> chatRooms = chatMessageList.stream()
                .map(ChatConverter::toChatMessageDetail)
                .toList();

        String postName;
        Post post = chatRoom.getRentalRequest().getPost();
        if (post.getPostCategory() == PostCategory.EMPTY_SPOTS){
            postName = post.getEmptySpot().getLocation();
        } else {
            postName = post.getItem().getTitle();
        }

        return ChatResponseDTO.ChatMessageList.builder()
                .chatRoomName(opponent.getNickname())
                // TODO: 대여 요청 엔티티와 연결 필요
                .postName(postName)
                // TODO: S3 개발 후, 고도화 필요
                .profileUrl(opponent.getProfileUrl())
                .chatMessageList(chatRooms)
                .build();
    }

    public static ChatRoomPreview toChatRoomDTO(ChatRoom chatRoom){
        return ChatRoomPreview.builder()
                .chatRoomId(chatRoom.getId())
                .chatName(chatRoom.getChatRoomName())
                // TODO: S3 개발 후, 고도화 필요
                .profileUrl(null)
                .lastMessageContent(chatRoom.getLastMessageContent())
                .lastMessageAt(chatRoom.getLastMessageAt())
                .build();
    }

    public static ChatResponseDTO.ChatRoomList toChatRoomList(ChatRoomType chatRoomType, List<ChatRoom> chatRoomList){
        List<ChatRoomPreview> chatRooms = chatRoomList.stream()
                .map(ChatConverter::toChatRoomDTO)
                .toList();

        return ChatResponseDTO.ChatRoomList.builder()
                .chatRoomType(chatRoomType)
                .chatRoomList(chatRooms)
                .build();
    }

    public static ChatResponseDTO.ChatRoomList toChatRoomPreviewList(ChatRoomType chatRoomType, List<ChatRoomPreview> chatRoomList){

        return ChatResponseDTO.ChatRoomList.builder()
                .chatRoomType(chatRoomType)
                .chatRoomList(chatRoomList)
                .build();
    }

    public static ChatResponseDTO.ChatRoomUpdate toChatRoomUpdate(
        Long memberId, Long chatRoomId, String lastMessageContent, LocalDateTime lastMessageAt, Long unreadCount, Boolean isViewing
    ){
        return ChatResponseDTO.ChatRoomUpdate.builder()
                .memberId(memberId)
                .chatRoomId(chatRoomId)
                .lastMessageContent(lastMessageContent)
                .lastMessageAt(lastMessageAt)
                .unreadCount(isViewing? 0L : unreadCount)
                .build();
    }

    public static ChatResponseDTO.ChatMessageSentEvent toChatMessageSentEvent(
            ChatMessage chatMessage, Long receiverId
    ){
        return ChatResponseDTO.ChatMessageSentEvent.builder()
                .chatRoomId(chatMessage.getChatRoom().getId())
                .messageId(chatMessage.getId())
                .senderId(chatMessage.getMember().getId())
                .receiverId(receiverId)
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }

}
