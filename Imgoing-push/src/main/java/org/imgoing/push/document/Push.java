package org.imgoing.push.document;

import com.google.firebase.internal.NonNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "push")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Push {
    @Id
    private String id;

    @NonNull
    private Long userId;

    @NonNull
    private Long notificationId;

    @NonNull
    private String message;
}