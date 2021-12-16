package org.imgoing.batch.publish;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class CustomMessage {
    private Long userId;

    private Long planId;

    private String message;
}
