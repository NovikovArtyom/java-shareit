package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserService userService) {
        this.itemRequestRepository = itemRequestRepository;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequest> getItemRequestByUserId(Long userId) {
        UserEntity user = userService.getUserById(userId);
        if (user != null) {
            return itemRequestRepository.getAllByRequester_Id(userId);
        } else {
            throw new UserNotFoundException("Пользователь не зарегистрирован!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequest> getAllItemRequest(Long userId, Integer from, Integer size) {
        UserEntity user = userService.getUserById(userId);
        if (user != null) {
            return itemRequestRepository.getAllByRequester_IdNotOrderByCreatedAsc(userId, PageRequest.of(from, size)).stream()
                    .collect(Collectors.toList());
        } else {
            throw new UserNotFoundException("Пользователь не зарегистрирован!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequest getItemRequestByRequestId(Long userId, Long requestId) {
        UserEntity user = userService.getUserById(userId);
        if (user != null) {
            return itemRequestRepository.findById(requestId).orElseThrow(() ->
                    new ItemRequestNotFoundException("Информация о заявке не найдена!"));
        } else {
            throw new UserNotFoundException("Пользователь не зарегистрирован!");
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ItemRequest createItemRequest(Long userId, ItemRequest itemRequestEntity) {
        UserEntity user = userService.getUserById(userId);
        if (user != null) {
            itemRequestEntity.setRequester(user);
            itemRequestEntity.setCreated(LocalDateTime.now());
            return itemRequestRepository.save(itemRequestEntity);
        } else {
            throw new UserNotFoundException("Пользователь не зарегистрирован!");
        }
    }
}
