package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.RequestTaskDTO;
import org.example.dto.response.ResponseTaskDTO;
import org.example.entity.Family;
import org.example.entity.Task;
import org.example.entity.TaskStatus;
import org.example.entity.User;
import org.example.exception.extend.FamilyNotFoundException;
import org.example.exception.extend.TaskNotFoundException;
import org.example.exception.extend.UserNotFoundException;
import org.example.mapper.TaskMapper;
import org.example.repo.FamilyRepo;
import org.example.repo.TaskRepo;
import org.example.repo.UserRepo;
import org.example.sequrity.service.UserContext;
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

    public List<ResponseTaskDTO> getAllTasksForFamily(Long familyID) {
        Family family = familyRepo.findById(familyID).orElseThrow(() -> new FamilyNotFoundException("Family with id" + familyID + " not found"));
        List<ResponseTaskDTO> taskDTOList = new ArrayList<>();
        family.getTasks().forEach(task -> {
            taskDTOList.add(taskMapper.toResponseTaskDTO(task));
        });
        return taskDTOList;
    }

    public ResponseTaskDTO getTaskByID(Long taskID) {
        Task task = taskRepo.findById(taskID).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return taskMapper.toResponseTaskDTO(task);
    }

    public List<ResponseTaskDTO> getAllTasksForUser() {
        User user = userRepo.findUserByUsername(userContext.getUserDto().getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Task> tasks = userRepo.findTasksByUser(user);
        return tasks.stream().map(taskMapper::toResponseTaskDTO).collect(Collectors.toList());
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
        family.getTasks().add(task);
        familyRepo.save(family);
        return taskMapper.toResponseTaskDTO(task);
    }
}
