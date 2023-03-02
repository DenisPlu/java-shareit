package ru.practicum.shareit.item.comment;

public final class CommentMapper {

    public static CommentDto toCommentDto(Comment comment, String userName) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                userName,
                comment.getCreatedDate()
        );
    }

    public static Comment fromDtoToComment(CommentDto commentDto, Long itemId, Long userId) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                itemId,
                userId,
                commentDto.getCreatedDate()
        );
    }
}
