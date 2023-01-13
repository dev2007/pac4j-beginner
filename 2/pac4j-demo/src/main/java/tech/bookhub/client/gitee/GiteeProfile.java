package tech.bookhub.client.gitee;

import org.pac4j.oauth.profile.OAuth20Profile;

import java.net.URI;

public class GiteeProfile extends OAuth20Profile {
    @Override
    public String getDisplayName() {
        return (String) getAttribute(GiteeProfileDefinition.NAME);
    }

    @Override
    public String getUsername() {
        return (String) getAttribute(GiteeProfileDefinition.LOGIN);
    }

    public Integer getFollowing() {
        return (Integer) getAttribute(GiteeProfileDefinition.FOLLOWING);
    }

    public Integer getPublicRepos() {
        return (Integer) getAttribute(GiteeProfileDefinition.PUBLIC_REPOS);
    }
}
