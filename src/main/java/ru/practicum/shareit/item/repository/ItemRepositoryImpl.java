package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DuplicateItemException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> itemStorage = new HashMap<>();
    private static Long itemId = 0L;

    @Override
    public Item addItem(Item itemEntity) {
        if (!itemStorage.containsValue(itemEntity)) {
            itemEntity.setId(idGenerator());
            itemStorage.put(itemEntity.getId(), itemEntity);
            return itemEntity;
        } else {
            throw new DuplicateItemException("Данная вещь уже зарегистрирована!");
        }
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemStorage.get(itemId);
    }

    @Override
    public Item updateItem(Long itemId, Item item) {
        Item updatedItem = itemStorage.get(itemId);
        if (updatedItem != null) {
            if (item.getName() != null && !item.getName().equals(updatedItem.getName())) {
                updatedItem.setName(item.getName());
            }
            if (item.getDescription() != null && !item.getDescription().equals(updatedItem.getDescription())) {
                updatedItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null && item.getAvailable() != updatedItem.getAvailable()) {
                updatedItem.setAvailable(item.getAvailable());
            }
            itemStorage.put(itemId, updatedItem);
            return updatedItem;
        } else {
            throw new ItemNotFoundException(String.format("Вещь с id = %d не найдена!", itemId));
        }
    }

    @Override
    public Collection<Item> getAllItems(Long userId) {
        return itemStorage.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemStorage.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    private Long idGenerator() {
        itemId++;
        return itemId;
    }
}
