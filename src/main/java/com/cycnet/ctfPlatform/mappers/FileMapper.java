package com.cycnet.ctfPlatform.mappers;

import com.cycnet.ctfPlatform.dto.file.FileRequestDto;
import com.cycnet.ctfPlatform.dto.file.FileResponseDto;
import com.cycnet.ctfPlatform.models.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper extends Mappable<File, FileRequestDto, FileResponseDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "path", ignore = true)
    @Mapping(target = "task", ignore = true)
    File toEntity(FileRequestDto request);

    @Override
    @Mapping(target = "taskId", source = "task.id")
    FileResponseDto toDto(File entity);

}
