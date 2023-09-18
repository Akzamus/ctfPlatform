package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.task.TaskRequestDto;
import com.cycnet.ctfPlatform.dto.task.TaskResponseDto;
import com.cycnet.ctfPlatform.models.Task;

public interface TaskService extends CrudService<Task, TaskRequestDto, TaskResponseDto> {

}
