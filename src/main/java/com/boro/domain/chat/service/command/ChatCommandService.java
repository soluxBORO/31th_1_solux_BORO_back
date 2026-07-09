package com.boro.domain.chat.service.command;

import com.boro.domain.chat.converter.ChatConverter;
import com.boro.domain.chat.dto.request.ChatRequestDTO;
import com.boro.domain.chat.dto.response.ChatResponseDTO;
import com.boro.domain.chat.entity.ChatMessage;
import com.boro.domain.chat.entity.ChatMessageImage;
import com.boro.domain.chat.entity.ChatRoom;
import com.boro.domain.chat.entity.enums.ChatMessageType;
import com.boro.domain.chat.repository.ChatMessageRepository;
import com.boro.domain.chat.repository.ChatRoomRepository;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.repository.MemberRepository;
import com.boro.domain.post.entity.Post;
import com.boro.domain.post.repository.PostRepository;
import com.boro.domain.rentalrequest.service.command.RentalRequestCommandService;
import com.boro.global.error.code.status.ChatErrorCode;
import com.boro.global.error.code.status.MemberErrorCode;
import com.boro.global.error.code.status.PostErrorCode;
import com.boro.global.error.exception.handler.ChatException;
import com.boro.global.error.exception.handler.MemberException;
import com.boro.global.error.exception.handler.PostException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final RentalRequestCommandService rentalRequestCommandService;
    private final PostRepository postRepository;

    // TODO: 요청 게시물과 연결 필요
    public void saveChatRoom(Long memberId, ChatRequestDTO.ChatRoom request){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        Member owner = memberRepository.findById(post.getMember().getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        ChatRoom chatRoom = ChatConverter.toChatRoom(request, post);

        chatRoom.addChatMember(ChatConverter.toChatMember(chatRoom, member));
        chatRoom.addChatMember(ChatConverter.toChatMember(chatRoom, owner));

        rentalRequestCommandService.createRentalRequest(post, member);
        chatRoomRepository.save(chatRoom);
    }

    public void saveChatRoomTest(Long memberId, ChatRequestDTO.ChatRoomTest request){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Member owner = memberRepository.findById(request.ownerId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        ChatRoom chatRoom = ChatConverter.toChatRoomTest(request);
        chatRoom.addChatMember(ChatConverter.toChatMember(chatRoom, member));
        chatRoom.addChatMember(ChatConverter.toChatMember(chatRoom, owner));

        chatRoomRepository.save(chatRoom);
    }

    public ChatResponseDTO.ChatMessage saveMessage(Long memberId, Long roomId, ChatRequestDTO.ChatMessage request){
        validateMessage(request);

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        ChatMessage chatMessage = ChatConverter.toChatMessage(request, member);
        chatRoom.addChatMessage(chatMessage);

        if (request.chatMessageType().equals(ChatMessageType.IMAGE) && request.imageUrls() != null && !request.imageUrls().isEmpty()){
            if (request.imageUrls().size() > 3) {
                throw new ChatException(ChatErrorCode.CHAT_IMAGE_LIMIT_EXCEEDED);
            }

            // TODO: S3 로직 추가 필요
            request.imageUrls().forEach(imageUrl -> {
                        ChatMessageImage chatMessageImage = ChatConverter.toChatMessageImage(imageUrl);
                        chatMessage.addChatMessageImage(chatMessageImage);
                    });
        }

        chatMessageRepository.save(chatMessage);
        chatRoom.updateLastMessageContent(chatMessage.getContent(), chatMessage.getCreatedAt());
        return ChatConverter.toChatMessageDTO(chatMessage);
    }


    private void validateMessage(ChatRequestDTO.ChatMessage request) {
        switch (request.chatMessageType()) {
            case TEXT -> {
                if (request.imageUrls() != null && !request.imageUrls().isEmpty()) {
                    throw new ChatException(ChatErrorCode.INVALID_TEXT_MESSAGE);
                }
            }

            case IMAGE -> {
                if (request.content() != null && !request.content().isBlank()) {
                    throw new ChatException(ChatErrorCode.INVALID_IMAGE_MESSAGE);
                }
            }
        }
    }

}
