package ru.practicum.shareit.item.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByItem_Id(Long itemId);

    List<CommentEntity> findByItem_IdAndItem_Owner_Id(Long itemId, Long userId);
}
