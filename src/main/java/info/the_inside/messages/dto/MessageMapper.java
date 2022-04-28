package info.the_inside.messages.dto;

import info.the_inside.messages.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mappings({
            @Mapping(target = "text", source = "entity.text"),
            @Mapping(target = "createdAt", source = "entity.createdAt", dateFormat = "YYYY-MM-dd HH:mm:ss"),
            @Mapping(target = "sender", source = "entity.sender.name")
    })
    MessageDto messageToDto(Message entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "text", source = "dto.text"),
            @Mapping(target = "createdAt", source = "dto.createdAt", dateFormat = "YYYY-MM-dd HH:mm:ss"),
            @Mapping(target = "sender",
                     expression = "java(new info.the_inside.messages.model.Sender(dto.getSender(), \"\"))")
    })
    Message dtoToMessage(MessageDto dto);

}
