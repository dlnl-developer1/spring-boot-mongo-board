
package com.example.board;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "posts")
public class Post {

    @Id
    private String id;
    private String title;
    private String content;
    private List<String> commentIds;  // 댓글 ID 리스트 (참조)
    private List<Comment> comments;  // 조인된 댓글들

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getters and Setters for commentIds
    public List<String> getCommentIds() {
        if (commentIds == null) {
            commentIds = new ArrayList<>();
        }
        return commentIds;
    }

    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
