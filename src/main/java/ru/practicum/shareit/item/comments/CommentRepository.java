package ru.practicum.shareit.item.comments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByItem_Id(Long itemId);

    List<CommentEntity> findByItem_IdAndItem_Owner_Id(Long itemId, Long userId);
}
