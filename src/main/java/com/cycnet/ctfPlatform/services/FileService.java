package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.file.FileRequestDto;
import com.cycnet.ctfPlatform.dto.file.FileResponseDto;
import com.cycnet.ctfPlatform.models.File;

public interface FileService extends CrudService<File, FileRequestDto, FileResponseDto> {

}
