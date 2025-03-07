package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.RequestTaskDTO;
import org.example.dto.request.RequestTaskStatusDTO;
import org.example.dto.response.PageDTO;
import org.example.dto.response.ResponseTaskDTO;
import org.example.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("newAPI/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("family/{familyId}")
    public PageDTO<ResponseTaskDTO> getTasksForFamily(@PathVariable("familyId") Long familyId, Pageable pageable) {
        Page<ResponseTaskDTO> taskInfoPage = taskService.getAllTasksForFamily(familyId, pageable);
        return new PageDTO<>(
                taskInfoPage.getContent(),
                taskInfoPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @GetMapping("{taskID}")
    public ResponseEntity<ResponseTaskDTO> getTaskByID(@PathVariable("taskID") Long taskID) {
        return ResponseEntity.ok(taskService.getTaskByID(taskID));
    }

    @GetMapping("solo")
    public PageDTO<ResponseTaskDTO> getAllTasksForUser(Pageable pageable) {
        Page<ResponseTaskDTO> taskInfoPage = taskService.getAllTasksForUser(pageable);
        return new PageDTO<>(
                taskInfoPage.getContent(),
                taskInfoPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @PostMapping("{familyID}")
    public ResponseEntity<ResponseTaskDTO> createTaskForFamily(@PathVariable("familyID") Long familyID, @RequestBody RequestTaskDTO requestTaskDTO) {
        return ResponseEntity.ok(taskService.createTaskForFamily(requestTaskDTO, familyID));
    }

    @PostMapping
    public ResponseEntity<ResponseTaskDTO> createTaskForYourself(@RequestBody RequestTaskDTO requestTaskDTO) {
        return ResponseEntity.ok(taskService.createTaskForYourself(requestTaskDTO));
    }

    @PutMapping("{taskId}")
    public ResponseEntity<ResponseTaskDTO> updateTask(@PathVariable("taskId") Long taskId, @RequestBody RequestTaskDTO requestTaskDTO) {
        return ResponseEntity.ok(taskService.updateTask(requestTaskDTO, taskId));
    }

    @PutMapping("{taskId}/status")
    public ResponseEntity<ResponseTaskDTO> updateTaskStatus(@PathVariable("taskId") Long taskId, @RequestBody RequestTaskStatusDTO status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(status, taskId));
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
