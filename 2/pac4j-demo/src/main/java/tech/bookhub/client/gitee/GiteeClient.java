package tech.bookhub.client.gitee;

import org.pac4j.core.util.HttpActionHelper;
import org.pac4j.oauth.client.OAuth20Client;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class GiteeClient extends OAuth20Client {
    public static final String DEFAULT_SCOPE = "user_info";

    public GiteeClient() {
        setScope(DEFAULT_SCOPE);
    }

    public GiteeClient(String key, String secret) {
        setScope(DEFAULT_SCOPE);
        setKey(key);
        setSecret(secret);
    }

    public GiteeClient(String key, String secret, String scope) {
        setScope(DEFAULT_SCOPE);
        setKey(key);
        setSecret(secret);
        setScope(scope);
    }

    @Override
    protected void internalInit(boolean forceReinit) {
        configuration.setApi(GiteeApi.instance());
        configuration.setProfileDefinition(new GiteeProfileDefinition());
        configuration.setTokenAsHeader(true);
        defaultLogoutActionBuilder((ctx, session, profile, targetUrl) ->
                Optional.of(HttpActionHelper.buildRedirectUrlAction(ctx, "https://gitee.com/logout")));

        super.internalInit(forceReinit);
    }

    public String getScope() {
        return getConfiguration().getScope();
    }

    public void setScope(String scope) {
        getConfiguration().setScope(StringUtils.hasText(scope) ? scope : DEFAULT_SCOPE);
    }
}
