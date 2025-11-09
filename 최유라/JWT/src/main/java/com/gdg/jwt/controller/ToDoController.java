package com.gdg.jwt.controller;

import com.gdg.jwt.dto.todo.ToDoCreateRequest;
import com.gdg.jwt.dto.todo.ToDoInfoResponse;
import com.gdg.jwt.dto.todo.ToDoUpdateRequest;
import com.gdg.jwt.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class ToDoController {

    private final ToDoService toDoService;

    @PostMapping
    public ResponseEntity<ToDoInfoResponse> createWork(Principal principal, @RequestBody ToDoCreateRequest toDoCreateRequest) {
        return ResponseEntity.created(URI.create("/todo/")).body(toDoService.createWork(principal, toDoCreateRequest));
    }

    @GetMapping("/{workId}")
    public ResponseEntity<ToDoInfoResponse> getWork(@PathVariable Long workId) {
        return ResponseEntity.ok(toDoService.getWorkInfo(workId));
    }

    @PatchMapping("/{workId}")
    public ResponseEntity<ToDoInfoResponse> updateWork(Principal principal, @PathVariable Long workId, @RequestBody ToDoUpdateRequest toDoUpdateRequest) {
        return ResponseEntity.ok(toDoService.updateWork(principal, workId, toDoUpdateRequest));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteWork(Principal principal, @PathVariable Long postId) {
        toDoService.deleteWork(principal, postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ToDoInfoResponse>> getAllWork() {
        return ResponseEntity.ok(toDoService.getAllWork());
    }
}
