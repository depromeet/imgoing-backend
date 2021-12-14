package org.imgoing.batch.publish;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PublishMessage {
    private Long userId;

    private Long planId;

    private String message;
}
