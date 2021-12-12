package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.task.TaskDto;
import org.imgoing.api.mapper.TaskMapper;
import org.imgoing.api.repository.TaskRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public Task create(Task newTask){
        return taskRepository.save(newTask);
    }

    @Transactional
    public List<Task> createAll(User user, List<TaskDto> taskDtoList) {
        List<Task> newTaskList = new ArrayList<>();
        for (int i = 0; i < taskDtoList.size(); i++) {
            Task newTask = taskMapper.toEntity(taskDtoList.get(i));
            if(!newTask.getIsBookmarked()){
                newTask = Task.builder()
                        .name(newTask.getName())
                        .time(newTask.getTime())
                        .isBookmarked(newTask.getIsBookmarked())
                        .user(user)
                        .build();
                taskRepository.save(newTask);
            }
            newTaskList.add(newTask);
        }
        return newTaskList;
    }

    @Transactional(readOnly = true)
    public Task getById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "존재하지 않는 준비항목입니다."));
    }

    @Transactional(readOnly = true)
    public List<Task> getListById(List<Long> idList){
        return idList.stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Task> getListByUserId(Long userId){
        return taskRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Task> getListByUserIdAndIsBookmarked(Long userId){
        return taskRepository.findAllByUserIdAndIsBookmarked(userId);
    }

    @Transactional
    public void delete(Task task){
        taskRepository.delete(task);
    }

    @Transactional
    public Task modify(Task oldTask, Task newTask){
        oldTask.modifyTask(newTask);
        return taskRepository.save(oldTask);
    }

    @Transactional
    public List<Task> saveAll(List<Task> tasks) {
        return taskRepository.saveAll(tasks);
    }

    @Transactional
    public void deleteAll(List<Task> tasks) {
        taskRepository.deleteAll(tasks);
    }
}
