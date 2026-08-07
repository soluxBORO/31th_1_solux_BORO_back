package com.boro.domain.post.converter;

import com.boro.domain.member.entity.Member;
import com.boro.domain.post.dto.request.PostRequestDTO;
import com.boro.domain.post.dto.response.PostResponseDTO;
import com.boro.domain.post.entity.Item;
import com.boro.domain.post.entity.ItemImage;
import com.boro.domain.post.entity.Post;
import com.boro.domain.post.entity.enums.PostCategory;

public class PostConverter {

    public static Post toPost(Member member, PostCategory postCategory) {
        return Post.builder()
                .member(member)
                .postCategory(postCategory)
                .build();
    }

    public static Item toItem(PostRequestDTO.CreatePost request) {
        Item item = Item.builder()
                .title(request.title())
                .description(request.description())
                .rentalStartTime(request.rentalStartTime())
                .rentalEndTime(request.rentalEndTime())
                .rentalPrice(request.rentalPrice())
                .rentalPriceUnit(request.rentalPriceUnit())
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

    public static void updateItem(Item item, PostRequestDTO.EditPost request) {
        item.updateInfo(
                request.title(),
                request.description(),
                request.rentalStartTime(),
                request.rentalEndTime(),
                request.rentalPrice(),
                request.rentalPriceUnit()
        );
        item.updateImages(request.imageUrlList());
    }

    public static PostResponseDTO.PostSummary toPostSummary(Post post, long likeCount, boolean liked) {
        Item item = post.getItem();
        return PostResponseDTO.PostSummary.builder()
                .postId(post.getId())
                .status(post.getStatus())
                .imageUrlList(item.getItemImages().stream().map(ItemImage::getImageUrl).toList())
                .category(post.getPostCategory())
                .title(item.getTitle())
                .description(item.getDescription())
                .rentalStartTime(item.getRentalStartTime())
                .rentalEndTime(item.getRentalEndTime())
                .profileUrl(post.getMember().getProfileUrl())
                .rentalPrice(item.getRentalPrice())
                .rentalPriceUnit(item.getRentalPriceUnit())
                .authorNickname(post.getMember().getNickname())
                .likeCount(likeCount)
                .liked(liked)
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static PostResponseDTO.PostDetail toPostDetail(Post post, long likeCount, boolean liked) {
        Item item = post.getItem();
        return PostResponseDTO.PostDetail.builder()
                .postId(post.getId())
                .status(post.getStatus())
                .imageUrlList(item.getItemImages().stream().map(ItemImage::getImageUrl).toList())
                .category(post.getPostCategory())
                .title(item.getTitle())
                .description(item.getDescription())
                .rentalStartTime(item.getRentalStartTime())
                .rentalEndTime(item.getRentalEndTime())
                .profileUrl(post.getMember().getProfileUrl())
                .rentalPrice(item.getRentalPrice())
                .rentalPriceUnit(item.getRentalPriceUnit())
                .authorNickname(post.getMember().getNickname())
                .likeCount(likeCount)
                .liked(liked)
                .createdAt(post.getCreatedAt())
                .build();
    }
}
