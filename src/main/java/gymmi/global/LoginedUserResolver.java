package gymmi.global;

import gymmi.entity.User;
import gymmi.exceptionhandler.exception.AuthenticationFailException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.repository.UserRepository;
import gymmi.service.TokenProcessor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginedUserResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION_TYPE_BEARER = "Bearer ";
    private final TokenProcessor tokenProcessor;
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.withContainingClass(User.class)
                .hasParameterAnnotation(Logined.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        validate(authorizationHeader);
        String accessToken = authorizationHeader.substring(AUTHORIZATION_TYPE_BEARER.length());
        Long userId = tokenProcessor.parseAccessToken(accessToken);
        User user = userRepository.getByUserId(userId);
        return user;
    }

    private void validate(String authorizationHeader) {
        if (!StringUtils.hasText(authorizationHeader)) {
            throw new AuthenticationFailException(ErrorCode.MISSING_AUTHORIZATION_HEADER);
        }
        if (!authorizationHeader.startsWith(AUTHORIZATION_TYPE_BEARER)) {
            throw new AuthenticationFailException(ErrorCode.UNSUPPORTED_AUTHORIZATION_TYPE);
        }
    }
}
