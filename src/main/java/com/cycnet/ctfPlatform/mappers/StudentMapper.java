package com.cycnet.ctfPlatform.mappers;

import com.cycnet.ctfPlatform.dto.student.StudentRequestDto;
import com.cycnet.ctfPlatform.dto.student.StudentResponseDto;
import com.cycnet.ctfPlatform.models.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface StudentMapper extends Mappable<Student, StudentRequestDto, StudentResponseDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "teamMembers", ignore = true)
    Student toEntity(StudentRequestDto request);

}
