package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.student.StudentRequestDto;
import com.cycnet.ctfPlatform.dto.student.StudentResponseDto;
import com.cycnet.ctfPlatform.models.Student;

public interface StudentService extends CrudService<Student, StudentRequestDto, StudentResponseDto> {

}
