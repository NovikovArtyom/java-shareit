package ru.practicum.shareit.server.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.booking.dto.BookingMapper;
import ru.practicum.shareit.server.booking.model.BookingEntity;
import ru.practicum.shareit.server.booking.model.BookingStatus;
import ru.practicum.shareit.server.booking.repository.BookingRepository;
import ru.practicum.shareit.server.exception.IncorrectCommentException;
import ru.practicum.shareit.server.exception.ItemNotFoundException;
import ru.practicum.shareit.server.exception.UserAccessException;
import ru.practicum.shareit.server.item.comments.CommentEntity;
import ru.practicum.shareit.server.item.comments.CommentMapper;
import ru.practicum.shareit.server.item.comments.CommentRepository;
import ru.practicum.shareit.server.item.comments.CommentResponseDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.dto.ItemMapper;
import ru.practicum.shareit.server.item.dto.UpdatedItemDto;
import ru.practicum.shareit.server.item.model.ItemEntity;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.request.service.ItemRequestService;
import ru.practicum.shareit.server.user.model.UserEntity;
import ru.practicum.shareit.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService,
                           ItemRequestService itemRequestService, BookingRepository bookingRepository,
                           CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.itemRequestService = itemRequestService;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllItems(Long userId, Integer form, Integer size) {
        log.info(String.format("Получение списка вещей для пользователя с id = %d", userId));
        UserEntity user = userService.getUserById(userId);
        Page<ItemEntity> items = itemRepository.findAllByOwner_IdOrderById(user.getId(), PageRequest.of(form, size));
        return items.stream()
                .map(item -> {
                    ItemDto itemDto = ItemMapper.toItemDto(item);
                    BookingEntity lastBooking = bookingRepository.findTop1ByItem_IdAndStartBeforeAndStatusEqualsOrderByStartDesc(itemDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
                    BookingEntity nextBooking = bookingRepository.findTop1ByItem_IdAndStartAfterAndStatusEqualsOrderByStart(itemDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
                    if (lastBooking != null && nextBooking != null) {
                        itemDto.setLastBooking(BookingMapper.toLastAndNextBookingDto(lastBooking));
                        itemDto.setNextBooking(BookingMapper.toLastAndNextBookingDto(nextBooking));
                    }
                    List<CommentResponseDto> comments = commentRepository.findByItem_IdAndItem_Owner_Id(item.getId(), userId).stream()
                            .map(CommentMapper::toDto)
                            .collect(Collectors.toList());
                    itemDto.setComments(comments);
                    return itemDto;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemById(Long userId, Long itemId) {
        log.info(String.format("Получение вещи с id = %d для пользователя с id = %d", itemId, userId));
        ItemDto itemDto = ItemMapper.toItemDto(itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Вещь c id = %d не зарегистрирован!", itemId))));
        BookingEntity lastBooking = bookingRepository.findTop1ByItem_IdAndStartBeforeAndStatusEqualsOrderByStartDesc(itemDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        BookingEntity nextBooking = bookingRepository.findTop1ByItem_IdAndStartAfterAndStatusEqualsOrderByStart(itemDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        if (itemDto.getOwner().getId().equals(userId)) {
            if (lastBooking != null) {
                itemDto.setLastBooking(BookingMapper.toLastAndNextBookingDto(lastBooking));
            }
            if (nextBooking != null) {
                itemDto.setNextBooking(BookingMapper.toLastAndNextBookingDto(nextBooking));
            }
        }
        List<CommentResponseDto> comments = commentRepository.findByItem_Id(itemDto.getId()).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
        itemDto.setComments(comments);
        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemEntity> search(Integer from, Integer size, String text) {
        log.info(String.format("Получение списка вещей подходящих по фильтрацию поиска. Текст для поиска: %s", text));
        if (!text.equals("")) {
            return itemRepository.search(text, PageRequest.of(from, size)).stream().collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ItemEntity addItem(Long userId, Long requestId, ItemEntity item) {
        log.info(String.format("Добавление новой вещи пользователем с id = %d", userId));
        if (requestId != null) {
            item.setRequest(itemRequestService.getItemRequestByRequestId(userId, requestId));
        }
        item.setOwner(userService.getUserById(userId));
        return itemRepository.save(item);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ItemEntity updateItem(Long userId, Long itemId, UpdatedItemDto updatedItemDto) {
        log.info(String.format("Обновление информации о вещи с id = %d пользователем с id = %d", itemId, userId));
        UserEntity user = userService.getUserById(userId);
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(String.format("Вещь c id = %d не зарегистрирован!", itemId)));
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CommentEntity addComment(Long userId, Long itemId, CommentEntity commentEntity) {
        log.info(String.format("Добавление комментария для вещи с id = %d пользователем с id = %d", itemId, userId));
        UserEntity user = userService.getUserById(userId);
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Данная вещь не найдена!"));
        if (bookingRepository.existsByItem_IdAndBooker_IdAndStatusEqualsAndStartBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now())) {
            commentEntity.setItem(item);
            commentEntity.setAuthor(user);
            commentEntity.setCreated(LocalDateTime.now());
            return commentRepository.save(commentEntity);
        } else {
            throw new IncorrectCommentException("Оставить комментарий невозможно!");
        }
    }
}
