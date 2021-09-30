package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.entity.Task;
import org.imgoing.api.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    @Transactional
    public Task create(Task newTask){
        return taskRepository.save(newTask);
    }

    @Transactional(readOnly = true)
    public Task getById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 일정입니다."));
    }

    @Transactional
    public void delete(Task task){
        taskRepository.delete(task);
    }

    @Transactional
    public Task update(Task newTask){
        Long id = newTask.getId();
        Task oldTask = getById(id);

        oldTask.modifyTask(newTask);
        return taskRepository.save(oldTask);
    }
}
