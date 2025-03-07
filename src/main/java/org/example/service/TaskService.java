package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.RequestTaskDTO;
import org.example.dto.request.RequestTaskStatusDTO;
import org.example.dto.response.ResponseTaskDTO;
import org.example.entity.Family;
import org.example.entity.Task;
import org.example.entity.TaskStatus;
import org.example.entity.User;
import org.example.exception.ForbiddenException;
import org.example.exception.extend.FamilyNotFoundException;
import org.example.exception.extend.TaskNotFoundException;
import org.example.exception.extend.UserNotFoundException;
import org.example.mapper.TaskMapper;
import org.example.repo.FamilyRepo;
import org.example.repo.TaskRepo;
import org.example.repo.UserRepo;
import org.example.sequrity.service.UserContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepo taskRepo;
    private final TaskMapper taskMapper;
    private final FamilyRepo familyRepo;
    private final UserRepo userRepo;
    private final UserContext userContext;

    public Page<ResponseTaskDTO> getAllTasksForFamily(Long familyID, Pageable pageable) {
        Family family = familyRepo.findById(familyID).orElseThrow(() -> new FamilyNotFoundException("Family with id" + familyID + " not found"));
        Page<Task> tasks = taskRepo.findAllByFamily(family, pageable);
        return tasks.map(taskMapper::toResponseTaskDTO);
    }

    public ResponseTaskDTO getTaskByID(Long taskID) {
        Task task = taskRepo.findById(taskID).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return taskMapper.toResponseTaskDTO(task);
    }

    public Page<ResponseTaskDTO> getAllTasksForUser(Pageable pageable) {
        User user = userRepo.findUserByUsername(userContext.getUserDto().getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Page<Task> tasks = taskRepo.findAllByCreator(user, pageable);
        return tasks.map(taskMapper::toResponseTaskDTO);
    }


    @Transactional
    public Task createTask(RequestTaskDTO requestTaskDTO) {
        Task task = new Task();
        User creator = userRepo.findUserByUsername(userContext.getUserDto().getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        task.setCreator(creator);
        creator.getTasks().add(task);
        task.setStatus(TaskStatus.OPEN);
        task.setDescription(requestTaskDTO.getDescription());
        task.setTitle(requestTaskDTO.getTitle());
        taskRepo.save(task);
        userRepo.save(creator);
        return task;
    }

    @Transactional
    public ResponseTaskDTO createTaskForYourself(RequestTaskDTO requestTaskDTO) {
        return taskMapper.toResponseTaskDTO(createTask(requestTaskDTO));
    }

    @Transactional
    public ResponseTaskDTO createTaskForFamily(RequestTaskDTO requestTaskDTO, Long familyID) {
        Task task = createTask(requestTaskDTO);
        Family family = familyRepo.findById(familyID).orElseThrow(() -> new FamilyNotFoundException("Family with id" + familyID + " not found"));
        task.setFamily(family);
        taskRepo.save(task);
        return taskMapper.toResponseTaskDTO(task);
    }

    @Transactional
    public ResponseTaskDTO updateTask(RequestTaskDTO requestTaskDTO, Long taskId) {
        User user = userRepo.findUserByUsername(userContext.getUserDto().getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Task task = taskRepo.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        if (!task.getCreator().equals(user)) {
            throw new ForbiddenException("You are not allowed to edit this task");
        }

        task.setTitle(requestTaskDTO.getTitle());
        task.setDescription(requestTaskDTO.getDescription());
        taskRepo.save(task);
        return taskMapper.toResponseTaskDTO(task);
    }

    public ResponseTaskDTO updateTaskStatus(RequestTaskStatusDTO taskStatusDTO, Long taskId){
        Task task = taskRepo.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        task.setStatus(taskStatusDTO.getTaskStatus());
        taskRepo.save(task);
        return taskMapper.toResponseTaskDTO(task);
    }

    public void deleteTask(Long taskId) {
        User user = userRepo.findUserByUsername(userContext.getUserDto().getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Task task = taskRepo.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        if (!task.getCreator().equals(user)) {
            throw new ForbiddenException("You are not allowed to delete this task");
        }
        taskRepo.delete(task);
    }
}
