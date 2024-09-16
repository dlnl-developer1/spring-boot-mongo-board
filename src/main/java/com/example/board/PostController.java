
package com.example.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CustomPostRepository customPostRepository;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping("/posts/new")
    public String showNewPostForm(Model model) {
        Post post = new Post();
        model.addAttribute("post", post);
        return "form";
    }

    @PostMapping("/posts/save")
    public String savePost(@ModelAttribute("post") Post post) {
        postRepository.save(post);
        return "redirect:/";
    }

    @GetMapping("/posts/edit/{id}")
    public String showEditPostForm(@PathVariable String id, Model model) {
        Post post = postRepository.findById(id).get();
        model.addAttribute("post", post);
        return "form";
    }

    @GetMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable String id) {
        postRepository.deleteById(id);
        return "redirect:/";
    }

    // 댓글 추가 메서드
    @PostMapping("/posts/{postId}/comments")
    public String addComment(@PathVariable String postId, @ModelAttribute("comment") Comment comment) {
        comment.setPostId(postId);  // 댓글에 게시글 ID 설정
        commentRepository.save(comment);
        return "redirect:/posts/" + postId;
    }

    // 게시글 상세 페이지에 댓글 표시
    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable String id, Model model) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        List<Comment> comments = commentRepository.findByPostId(id);  // 게시글 ID로 댓글 조회
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return "post-details";
    }

    @GetMapping("/posts/{postId}/with-comments")
    public String viewPostWithComments(@PathVariable String postId, Model model) {
        Post post = customPostRepository.findPostWithComments(postId);
        model.addAttribute("post", post);
        return "post-with-comments";
    }
}
