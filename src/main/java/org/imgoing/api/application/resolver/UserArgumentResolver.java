package org.imgoing.api.application.resolver;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.CertificateAuthority;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.domain.vo.TokenPayload;
import org.imgoing.api.repository.UserRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private final CertificateAuthority ca;
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ){
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        String token = req.getHeader("x-access-token");
        if (!StringUtils.hasLength(token)) throw new ImgoingException(ImgoingError.NOT_LOGIN);
        TokenPayload payload = ca.decrypt(token);

        User user = userRepository.findById(payload.getId()).orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "Token으로 유저를 찾을 수 없습니다."));
        return user;
    }
}