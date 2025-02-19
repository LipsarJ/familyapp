package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.RequestTaskDTO;
import org.example.dto.response.ResponseTaskDTO;
import org.example.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("newAPI/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("{familyId}")
    public ResponseEntity<List<ResponseTaskDTO>> getTasksForFamily(@PathVariable("familyId") Long familyId) {
        return ResponseEntity.ok(taskService.getAllTasksForFamily(familyId));
    }

    @GetMapping("{taskID}")
    public ResponseEntity<ResponseTaskDTO> getTaskByID(@PathVariable("taskID") Long taskID) {
        return ResponseEntity.ok(taskService.getTaskByID(taskID));
    }

    @GetMapping("solo")
    public ResponseEntity<List<ResponseTaskDTO>> getAllTasksForUser() {
        return ResponseEntity.ok(taskService.getAllTasksForUser());
    }

    @PostMapping("create/{familyID}")
    public ResponseEntity<ResponseTaskDTO> createTaskForFamily(@PathVariable("familyID") Long familyID, @RequestBody RequestTaskDTO requestTaskDTO) {
        return ResponseEntity.ok(taskService.createTaskForFamily(requestTaskDTO, familyID));
    }

    @PostMapping("create")
    public ResponseEntity<ResponseTaskDTO> createTaskForYourself(@RequestBody RequestTaskDTO requestTaskDTO) {
        return ResponseEntity.ok(taskService.createTaskForYourself(requestTaskDTO));
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
