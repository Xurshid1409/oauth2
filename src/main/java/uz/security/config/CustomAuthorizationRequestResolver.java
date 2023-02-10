package uz.security.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver delegatedRequestResolver;

    public CustomAuthorizationRequestResolver(
            ClientRegistrationRepository repo, String authorizationRequestBaseUri) {
            delegatedRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest req = delegatedRequestResolver.resolve(request);
        if(req != null) {
            req = customizeRequest(req);
        }
        return req;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest req = delegatedRequestResolver.resolve(request, clientRegistrationId);
        if(req != null) {
            req = customizeRequest(req);
        }
        return req;
    }

    private OAuth2AuthorizationRequest customizeRequest(OAuth2AuthorizationRequest request) {

        if (request == null) {
            return null;
        }
        Map< String, Object > params = new HashMap<>(request.getAdditionalParameters());
        params.put( OAuth2ParameterNames.RESPONSE_TYPE, "one_" + request.getResponseType().getValue());
        params.put(OAuth2ParameterNames.GRANT_TYPE, "one_" + AuthorizationGrantType.AUTHORIZATION_CODE);
        return OAuth2AuthorizationRequest
                .from(request)
                .additionalParameters(params)
                .build();
    }

}
