package org.imgoing.push.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Push {
    @Id
    private String id;
    private Long userId;
    private Long notificationId;
    private String message;
}