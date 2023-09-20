package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.teamTaskAssignment.TeamTaskAssignmentRequestDto;
import com.cycnet.ctfPlatform.dto.teamTaskAssignment.TeamTaskAssignmentResponseDto;
import com.cycnet.ctfPlatform.models.TeamTaskAssignment;

public interface TeamTaskAssignmentService extends CrudService<
        TeamTaskAssignment,
        TeamTaskAssignmentRequestDto,
        TeamTaskAssignmentResponseDto
> { }
