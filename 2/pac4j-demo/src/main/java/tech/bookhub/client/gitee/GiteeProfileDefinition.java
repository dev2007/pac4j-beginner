package tech.bookhub.client.gitee;

import com.github.scribejava.core.model.Token;
import org.pac4j.core.profile.ProfileHelper;
import org.pac4j.core.profile.converter.Converters;
import org.pac4j.oauth.config.OAuthConfiguration;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.definition.OAuthProfileDefinition;

import java.util.Arrays;

import static org.pac4j.core.profile.AttributeLocation.PROFILE_ATTRIBUTE;

public class GiteeProfileDefinition extends OAuthProfileDefinition {
    public static final String URL = "url";
    public static final String FOLLOWING = "following";
    public static final String PUBLIC_REPOS = "public_repos";
    public static final String AVATAR_URL = "avatar_url";
    public static final String LOGIN = "login";
    public static final String NAME = "name";

    public GiteeProfileDefinition() {
        super(x -> new GiteeProfile());

        Arrays.asList(new String[]{
                NAME, LOGIN, URL, AVATAR_URL
        }).forEach(a -> primary(a, Converters.STRING));

        Arrays.asList(new String[]{
                FOLLOWING, PUBLIC_REPOS
        }).forEach(a -> primary(a, Converters.INTEGER));
    }

    @Override
    public String getProfileUrl(Token accessToken, OAuthConfiguration configuration) {
        return "https://gitee.com/api/v5/user";
    }

    @Override
    public GiteeProfile extractUserProfile(String body) {
        final var profile = (GiteeProfile) newProfile();
        final var json = JsonHelper.getFirstNode(body);
        if (json != null) {
            profile.setId(ProfileHelper.sanitizeIdentifier(JsonHelper.getElement(json, "id")));
            for (final var attribute : getPrimaryAttributes()) {
                convertAndAdd(profile, PROFILE_ATTRIBUTE, attribute, JsonHelper.getElement(json, attribute));
            }
        } else {
            raiseProfileExtractionJsonError(body);
        }
        return profile;
    }
}
