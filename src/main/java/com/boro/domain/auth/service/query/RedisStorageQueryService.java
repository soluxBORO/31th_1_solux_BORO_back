package com.boro.domain.auth.service.query;

import com.boro.domain.auth.constants.TokenStorageConstants;
import com.boro.global.security.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisStorageQueryService {
    private final RedisUtil redisUtil;

    public boolean isBlackList(String token) {
        return Boolean.TRUE.equals(redisUtil.has(TokenStorageConstants.BLACKLIST_PREFIX + token));
    }

    public String getRefreshToken(Long memberId) {
        return redisUtil.get(TokenStorageConstants.REFRESH_TOKEN_PREFIX + memberId, String.class);
    }
}
