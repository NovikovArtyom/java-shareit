package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

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

        }
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemStorage.get(itemId);
    }

    @Override
    public Item updateItem(Long itemId, UpdatedItemDto updatedItemDto) {

    }

    private Long idGenerator() {
        itemId++;
        return itemId;
    }
}
