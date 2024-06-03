package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserAccessException;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    public List<ItemEntity> getAllItems(Long userId) {
        userService.getUserById(userId);
        return itemRepository.findAllByOwner_Id(userId);
    }

    public ItemEntity getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Вещь c id = %d не зарегистрирован!", itemId)));
    }

    public List<ItemEntity> search(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemRepository.search(text);
        }
    }

    public ItemEntity addItem(Long userId, ItemEntity item) {
        item.setOwner(userService.getUserById(userId));
        return itemRepository.save(item);
    }


    public ItemEntity updateItem(Long userId, Long itemId, UpdatedItemDto updatedItemDto) {
        UserEntity user = userService.getUserById(userId);
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Вещь c id = %d не зарегистрирован!", itemId)));
        if (item.getOwner() == user) {
            if (updatedItemDto.getName() != null && !item.getName().equals(updatedItemDto.getName())) {
                item.setName(updatedItemDto.getName());
            }
            if (updatedItemDto.getDescription() != null && !item.getDescription().equals(updatedItemDto.getDescription())) {
                item.setDescription(updatedItemDto.getDescription());
            }
            if (updatedItemDto.getAvailable() != null && item.getAvailable() != updatedItemDto.getAvailable()) {
                item.setAvailable(updatedItemDto.getAvailable());
            }
            return itemRepository.save(item);
        } else {
            throw new UserAccessException("Редактировать информацию о вещи может только ее владелец!");
        }
    }


}
