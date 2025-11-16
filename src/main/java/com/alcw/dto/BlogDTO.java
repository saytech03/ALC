package com.alcw.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class BlogDTO {
    private String title;
    private String author;
    private List<SectionDTO> sections = new ArrayList<>();

    @Data
    public static class SectionDTO {
        private String heading;
        private String subHeading;
        private String body;
        private List<MultipartFile> images;
        private List<String> references;
    }
}
