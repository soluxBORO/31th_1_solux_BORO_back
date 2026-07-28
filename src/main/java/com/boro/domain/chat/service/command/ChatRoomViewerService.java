package com.boro.domain.chat.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatRoomViewerService {

    private static final String KEY_PREFIX = "room:viewers:";
    private final RedisTemplate<String, Object> redisTemplate;

    public void enterRoom(Long roomId, Long memberId) {
        String key = generateKey(roomId);
        redisTemplate.opsForSet().add(key, memberId.toString());
    }

    public void leaveRoom(Long roomId, Long memberId) {
        String key = generateKey(roomId);
        redisTemplate.opsForSet().remove(key, memberId.toString());
        Long viewerCount = redisTemplate.opsForSet().size(key);

        if (viewerCount != null && viewerCount == 0) {
            redisTemplate.delete(key);
        }
    }

    public boolean isViewingRoom(Long roomId, Long memberId) {
        String key = generateKey(roomId);
        Boolean isMember = redisTemplate.opsForSet().isMember(key, memberId.toString());
        return Boolean.TRUE.equals(isMember);
    }

    public Set<Object> getViewers(Long roomId) {
        return redisTemplate.opsForSet().members(generateKey(roomId));
    }

    public long getViewerCount(Long roomId) {
        Long count = redisTemplate.opsForSet().size(generateKey(roomId));
        return count == null ? 0 : count;
    }

    private String generateKey(Long roomId) {
        return KEY_PREFIX + roomId;
    }
}
