package com.gdg.jwt.service;

import com.gdg.jwt.domain.ToDo;
import com.gdg.jwt.dto.todo.ToDoCreateRequest;
import com.gdg.jwt.dto.todo.ToDoInfoResponse;
import com.gdg.jwt.dto.todo.ToDoUpdateRequest;
import com.gdg.jwt.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;
    private final com.gdg.jwt.service.UserService userService;

    @Transactional
    public ToDoInfoResponse createWork(Principal principal, ToDoCreateRequest toDoCreateRequest) {
        ToDo todo = toDoRepository.save(ToDo.builder()
                        .work(toDoCreateRequest.work())
                        .daysLeft(toDoCreateRequest.daysLeft())
                        .user(userService.getUserEntity(Long.parseLong(principal.getName())))
                        .build());

        return ToDoInfoResponse.fromEntity(todo);
    }

    @Transactional(readOnly = true)
    public ToDoInfoResponse getWorkInfo(Long workId) {
        return ToDoInfoResponse.fromEntity(getWork(workId));
    }

    @Transactional
    public ToDoInfoResponse updateWork(Principal principal, Long workId, ToDoUpdateRequest toDoUpdateRequest) {
        ToDo todo = getWork(workId);
        validateAuthor(principal, todo);

        todo.update(
                toDoUpdateRequest.work() == null ? todo.getWork() : toDoUpdateRequest.work(),
                toDoUpdateRequest.daysLeft() == null ? todo.getDaysLeft() : toDoUpdateRequest.daysLeft());
        return ToDoInfoResponse.fromEntity(todo);
    }

    @Transactional
    public void deleteWork(Principal principal, Long workId) {
        ToDo todo = getWork(workId);
        validateAuthor(principal, todo);

        toDoRepository.delete(todo);
    }

    private ToDo getWork(Long workId) {
        return toDoRepository.findById(workId)
                .orElseThrow(() -> new RuntimeException("요청하신 내용의 일정을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<ToDoInfoResponse> getAllWork() { //전체 일정 조회
        return toDoRepository.findAll()
                .stream()
                .map(ToDoInfoResponse::fromEntity)
                .sorted(Comparator.comparing(ToDoInfoResponse::daysLeft))
                .collect(Collectors.toList());
    } //남은 일수 기준 오름차순 정렬해 리스트로 만듦

    private void validateAuthor(Principal principal, ToDo todo) {
        if (!todo.getUser().getId().equals(Long.parseLong(principal.getName()))) {
            throw new RuntimeException("해당 데이터에 접근할 권한이 없습니다.");
        }
    }
}
