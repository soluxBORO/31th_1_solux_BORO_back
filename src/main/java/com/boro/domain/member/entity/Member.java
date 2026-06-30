package com.boro.domain.member.entity;


import com.boro.domain.auth.entity.Social;
import com.boro.domain.member.dto.request.MemberRequestDTO;
import com.boro.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String studentNumber;

    private String nickname;

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private Integer point = 0;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Social> socialList = new ArrayList<>();

    public void changeMemberInfo(MemberRequestDTO.ChangeMemberInfo request){
        this.nickname = request.nickname();
    }

    public void addSocial(Social social){
        socialList.add(social);
        social.setMember(this);
    }
}
