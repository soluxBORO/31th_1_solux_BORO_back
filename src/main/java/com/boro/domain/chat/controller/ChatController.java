package com.boro.domain.chat.controller;

import com.boro.domain.chat.dto.request.ChatRequestDTO;
import com.boro.domain.chat.dto.response.ChatResponseDTO;
import com.boro.domain.chat.entity.enums.ChatRoomType;
import com.boro.domain.chat.service.command.ChatCommandService;
import com.boro.domain.chat.service.query.ChatQueryService;
import com.boro.global.error.ApiResponse;
import com.boro.global.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@RestController
@Tag(name = "채팅 API")
public class ChatController {

    private final ChatCommandService chatCommandService;
    private final ChatQueryService chatQueryService;

    @Operation(summary = "대여 요청 및 채팅방 생성 API", description = "채팅을 하는 순간 요청이 생성되는 API")
    @PostMapping
    public ApiResponse<Void> requestRental(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ChatRequestDTO.ChatRoom request
    ){
        chatCommandService.saveChatRoom(customUserDetails.getMemberId(), request.ownerId(), request);
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "채팅방 리스트 조회 API", description = "채팅방 리스트 조회하는 API")
    @GetMapping
    public ApiResponse<ChatResponseDTO.ChatRoomList> getChatRoomList(
            ChatRoomType chatRoomType,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        ChatResponseDTO.ChatRoomList chatRoomList = chatQueryService.getChatRoomList(customUserDetails.getMemberId(), chatRoomType);
        return ApiResponse.onSuccess(chatRoomList);
    }

    @Operation(summary = "채팅방 상세 조회 API", description = "채팅방 상세 조회하는 API")
    @GetMapping("/{chatRoomId}")
    public ApiResponse<ChatResponseDTO.ChatMessageList> getChatMessageList(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        ChatResponseDTO.ChatMessageList chatRoomDetail = chatQueryService.getChatRoomDetail(chatRoomId, customUserDetails.getMemberId());
        return ApiResponse.onSuccess(chatRoomDetail);
    }

}
