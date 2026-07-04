package com.boro.domain.post.service.query;

import com.boro.domain.post.converter.PostConverter;
import com.boro.domain.post.dto.response.PostResponseDTO;
import com.boro.domain.post.entity.Post;
import com.boro.domain.post.entity.enums.ItemCategory;
import com.boro.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;

    public List<PostResponseDTO.PostSummary> getPostList(ItemCategory category, boolean onlyAvailable) {
        List<Post> posts = postRepository.findPostList(category, onlyAvailable);
        return posts.stream()
                .map(PostConverter::toPostSummary)
                .toList();
    }
}
