package com.alcw.service;

import com.alcw.dto.BlogDTO;
import com.alcw.model.Blog;
import com.alcw.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final CloudinaryService cloudinaryService;

    public Blog createBlog(BlogDTO blogDTO) {
        Blog blog = new Blog();
        mapDtoToBlog(blogDTO, blog);
        return blogRepository.save(blog);
    }


    public Blog updateBlog(String id, BlogDTO blogDTO) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        mapDtoToBlog(blogDTO, blog);
        blog.setUpdatedAt(LocalDateTime.now());
        return blogRepository.save(blog);
    }

    public void deleteBlog(String id) {
        blogRepository.deleteById(id);
    }

    public Blog getBlogById(String id) {
        return blogRepository.findById(id).orElse(null);
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    private void mapDtoToBlog(BlogDTO dto, Blog blog) {
        blog.setTitle(dto.getTitle());
        blog.setAuthor(dto.getAuthor());

        blog.getSections().clear();
        for (BlogDTO.SectionDTO sectionDto : dto.getSections()) {
            Blog.Section section = new Blog.Section();
            section.setHeading(sectionDto.getHeading());
            section.setSubHeading(sectionDto.getSubHeading());
            section.setBody(sectionDto.getBody());
            section.setReferences(sectionDto.getReferences());

            // Upload images to Cloudinary
            if (sectionDto.getImages() != null) {
                List<String> imageUrls = sectionDto.getImages().stream()
                        .map(cloudinaryService::uploadFile)
                        .collect(Collectors.toList());
                section.setImages(imageUrls);
            }

            blog.getSections().add(section);
        }
    }
}