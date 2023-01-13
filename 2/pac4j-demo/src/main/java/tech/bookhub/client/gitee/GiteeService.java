package tech.bookhub.client.gitee;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.AccessTokenRequestParams;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.util.Map;

public class GiteeService extends OAuth20Service {
    public GiteeService(DefaultApi20 api, String apiKey, String apiSecret, String callback, String defaultScope,
                        String responseType, String userAgent, HttpClientConfig httpClientConfig, HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, defaultScope, responseType, null, userAgent, httpClientConfig, httpClient);
    }

    @Override
    protected OAuthRequest createAccessTokenRequest(AccessTokenRequestParams params) {
        final OAuthRequest request = new OAuthRequest(getApi().getAccessTokenVerb(), getApi().getAccessTokenEndpoint());

        getApi().getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

        request.addQuerystringParameter(OAuthConstants.CODE, params.getCode());
        final String callback = getCallback();
        if (callback != null) {
            request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, callback);
        }
        final String scope = params.getScope();
        if (scope != null) {
            request.addQuerystringParameter(OAuthConstants.SCOPE, scope);
        } else if (getDefaultScope() != null) {
            request.addQuerystringParameter(OAuthConstants.SCOPE, getDefaultScope());
        }
        request.addQuerystringParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);


        final Map<String, String> extraParameters = params.getExtraParameters();
        if (extraParameters != null && !extraParameters.isEmpty()) {
            for (Map.Entry<String, String> extraParameter : extraParameters.entrySet()) {
                request.addQuerystringParameter(extraParameter.getKey(), extraParameter.getValue());
            }
        }

        logRequestWithParams("access token", request);
        return request;
    }
}
