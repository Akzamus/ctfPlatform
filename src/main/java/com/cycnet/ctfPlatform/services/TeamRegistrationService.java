package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.teamRegistration.TeamRegistrationRequestDto;
import com.cycnet.ctfPlatform.dto.teamRegistration.TeamRegistrationResponseDto;
import com.cycnet.ctfPlatform.models.TeamRegistration;

public interface TeamRegistrationService extends CrudService<TeamRegistration, TeamRegistrationRequestDto, TeamRegistrationResponseDto> {

}
