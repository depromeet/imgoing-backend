package org.imgoing.api.application.interceptor;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.CertificateAuthority;
import org.imgoing.api.domain.entity.Routine;
import org.imgoing.api.domain.vo.TokenPayload;
import org.imgoing.api.repository.RoutineRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RoutinePermissionInterceptor implements HandlerInterceptor {
    private final RoutineRepository routineRepository;
    private final CertificateAuthority certificateAuthority;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestHttpMethod = request.getMethod();
        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if( (HttpMethod.GET.matches(requestHttpMethod) && !pathVariables.isEmpty())
                || HttpMethod.PUT.matches(requestHttpMethod)
                || HttpMethod.DELETE.matches(requestHttpMethod) ) {
            TokenPayload payload = certificateAuthority.decrypt(request.getHeader("x-access-token"));

            Long id = Long.parseLong((String)pathVariables.get("routineId"));

            Routine data = routineRepository.findById(id)
                    .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "존재하지 않는 루틴입니다."));

            if(data.getUser().getId() == payload.getId()) {
                return true;
            }
            throw new ImgoingException(ImgoingError.BAD_REQUEST, "접근할 수 없는 컨텐츠 입니다.");
        } else return true;
    }
}
