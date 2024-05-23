package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    public ItemDto addItem(Long userID, ItemDto itemDto) {
        itemDto.setOwner(userRepository.getUserById(userID));
        Item addedItem = itemRepository.addItem(ItemMapper.toItemEntity(itemDto));
        return ItemMapper.toItemDto(addedItem);
    }

    public ItemDto getItemById(Long itemId) {
        return ItemMapper.toItemDto(itemRepository.getItemById(itemId));
    }

    public ItemDto updateItem(Long userId, Long itemId, UpdatedItemDto updatedItemDto) {
        if (userRepository.getUserById(userId) == itemRepository.getItemById(itemId).getOwner()) {
            Item updatedItem = itemRepository.updateItem(itemId, updatedItemDto);
            return ItemMapper.toItemDto(updatedItem);
        } else {
            throw new
        }
    }


}
