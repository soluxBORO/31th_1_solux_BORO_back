package com.boro.domain.post.converter;

import com.boro.domain.member.entity.Member;
import com.boro.domain.post.dto.request.PostRequestDTO;
import com.boro.domain.post.dto.response.PostResponseDTO;
import com.boro.domain.post.entity.Item;
import com.boro.domain.post.entity.ItemImage;
import com.boro.domain.post.entity.Post;

public class PostConverter {

    public static Post toPost(Member member) {
        return Post.builder()
                .member(member)
                .build();
    }

    public static Item toItem(PostRequestDTO.CreatePost request) {
        Item item = Item.builder()
                .category(request.category())
                .title(request.title())
                .description(request.description())
                .rentalStartTime(request.rentalStartTime())
                .rentalEndTime(request.rentalEndTime())
                .rentalPrice(request.rentalPrice())
                .build();

        request.imageUrlList().forEach(imageUrl ->
                item.addImage(ItemImage.builder().imageUrl(imageUrl).build())
        );

        return item;
    }

    public static PostResponseDTO.CreatePost toCreatePost(Post post) {
        return PostResponseDTO.CreatePost.builder()
                .postId(post.getId())
                .build();
    }
}
