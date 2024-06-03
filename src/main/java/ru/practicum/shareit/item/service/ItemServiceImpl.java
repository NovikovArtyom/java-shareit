package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserAccessException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    public ItemDto addItem(Long userID, ItemDto itemDto) {
        if (userRepository.getUserById(userID) != null) {
            itemDto.setOwner(userRepository.getUserById(userID));
            Item addedItem = itemRepository.addItem(ItemMapper.fromItemDtoToItemEntity(itemDto));
            return ItemMapper.toItemDto(addedItem);
        } else {
            throw new UserNotFoundException("Данный пользователь не зарегистрирован!");
        }
    }

    public ItemDto getItemById(Long itemId) {
        return ItemMapper.toItemDto(itemRepository.getItemById(itemId));
    }

    public ItemDto updateItem(Long userId, Long itemId, UpdatedItemDto updatedItemDto) {
        if (userRepository.getUserById(userId) == itemRepository.getItemById(itemId).getOwner()) {
            Item updatedItem = itemRepository.updateItem(itemId, ItemMapper.fromUpdatedItemDtoToItemEntity(updatedItemDto));
            return ItemMapper.toItemDto(updatedItem);
        } else {
            throw new UserAccessException("Запрос на обновление данных может отправить только владелец вещи!");
        }
    }

    public Collection<ItemDto> getAllItems(Long userId) {
        User user = userRepository.getUserById(userId);
        if (user != null) {
            return itemRepository.getAllItems(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        } else {
            throw new UserNotFoundException(String.format("Пользователь c id = %d не зарегистрирован!", userId));
        }
    }

    public Collection<ItemDto> search(String text) {
        return itemRepository.search(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
