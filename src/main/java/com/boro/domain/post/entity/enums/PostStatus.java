package com.boro.domain.post.entity.enums;

public enum PostStatus {
    ACTIVE, // 대여가능
    RENTED, // 대여중
    COMPLETED, // 반납 완료, 양도 완료
    DELETED
}
