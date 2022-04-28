package info.the_inside.assessment.dto;

import info.the_inside.assessment.model.Sender;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface SenderMapper {

    @Mappings({
            @Mapping(target = "name", source = "entity.name"),
            @Mapping(target = "password", source = "entity.password"),
    })
    SenderDto senderToDto(Sender entity);

    @Mappings({
            @Mapping(target = "name", source = "dto.name"),
            @Mapping(target = "password", source = "dto.password"),
            @Mapping(target = "messages", ignore = true),
            @Mapping(target = "token", ignore = true)
    })
    Sender dtoToSender(SenderDto dto);

}
