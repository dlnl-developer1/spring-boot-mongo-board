package com.example.board;

public interface CustomPostRepository {
    Post findPostWithComments(String postId);  // 특정 게시글의 댓글을 조회하는 메서드
}
