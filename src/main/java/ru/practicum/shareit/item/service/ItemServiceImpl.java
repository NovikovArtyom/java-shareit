package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserAccessException;
import ru.practicum.shareit.item.comments.CommentEntity;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService, BookingRepository bookingRepository,
                           CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }


    //TODO refactor))
    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllItems(Long userId) {
        UserEntity user = userService.getUserById(userId);
        List<ItemEntity> items = itemRepository.findAllByOwner_Id(user.getId());
        return items.stream()
                .map(item -> {
                    ItemDto itemDto = ItemMapper.toItemDto(item);
                    BookingEntity lastBooking = bookingRepository.findTop1ByItem_IdAndStartBeforeOrderByStartDesc
                            (itemDto.getId(), LocalDateTime.now());
                    BookingEntity nextBooking = bookingRepository.findTop1ByItem_IdAndStartAfterOrderByStart
                            (item.getId(), LocalDateTime.now());
                    if (lastBooking != null && nextBooking != null) {
                        itemDto.setLastBooking(BookingMapper.toLastAndNextBookingDto(lastBooking));
                        itemDto.setNextBooking(BookingMapper.toLastAndNextBookingDto(nextBooking));
                    }
                    return itemDto;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemEntity getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Вещь c id = %d не зарегистрирован!", itemId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemEntity> search(String text) {
        if (!text.equals("")) {
            return itemRepository.search(text);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ItemEntity addItem(Long userId, ItemEntity item) {
        item.setOwner(userService.getUserById(userId));
        return itemRepository.save(item);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
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

    @Override
    public CommentEntity addComment(Long userId, Long itemId, CommentEntity commentEntity) {
        UserEntity user = userService.getUserById(userId);
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Данная вещь не найдена!"));
        BookingEntity booking = bookingRepository.findByItem_IdAndBooker_Id(itemId, userId);
        if (booking != null) {
            commentEntity.setItem(item);
            commentEntity.setAuthor(user);
            commentEntity.setCreated(LocalDateTime.now());
        } else {
            throw new UserAccessException("Комментарии может создавать только арендатор вещи!");
        }
    }


}
