package com.alcw.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "otps")
@Data
public class OTP {
    @Id
    private String id;
    private String email;
    private String code;
    private LocalDateTime expiryTime;
}
