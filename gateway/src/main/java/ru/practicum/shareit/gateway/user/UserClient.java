package ru.practicum.shareit.gateway.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.user.dto.UpdatedUserDto;
import ru.practicum.shareit.gateway.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public Object getAllUsers() {
        return get("");
    }

    public Object getUserById(Long userId) {
        return get("/" + userId, userId);
    }


    public Object createUser(UserDto userDto) {
        return post("", userDto);
    }


    public Object updateUser(Long userId, UpdatedUserDto updatedUserDto) {
        return patch("/" + userId, updatedUserDto);
    }

    public Object deleteUser(Long userId) {
        return delete("/" + userId, userId);
    }
}
