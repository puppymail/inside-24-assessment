package info.the_inside.assessment.controller;

import static info.the_inside.assessment.controller.ControllerConstants.ROOT_URL;
import static info.the_inside.assessment.exception.ErrorMessages.INVALID_TOKEN_EX;
import static java.lang.Integer.parseInt;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import info.the_inside.assessment.dto.MessageDto;
import info.the_inside.assessment.dto.MessageHistoryResponse;
import info.the_inside.assessment.dto.MessageMapper;
import info.the_inside.assessment.dto.MessageRequest;
import info.the_inside.assessment.model.Message;
import info.the_inside.assessment.model.Sender;
import info.the_inside.assessment.service.AuthService;
import info.the_inside.assessment.service.MessageService;
import info.the_inside.assessment.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping(ROOT_URL)
public class MessageControllerImpl implements MessageController {

    private static final String MESSAGE_URL = "/message";

    private static final String REGEX = "^history \\d+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX, CASE_INSENSITIVE);

    private final MessageService messageService;
    private final SenderService senderService;
    private final AuthService authService;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageControllerImpl(MessageService messageService,
                                 SenderService senderService,
                                 AuthService authService,
                                 MessageMapper messageMapper) {
        this.messageService = messageService;
        this.senderService = senderService;
        this.authService = authService;
        this.messageMapper = messageMapper;
    }

    @RequestMapping(value = MESSAGE_URL, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageHistoryResponse> handleMessage(@RequestBody MessageRequest request,
                                                                HttpServletRequest httpServletRequest)
            throws AccountNotFoundException, AuthException {
        String message = request.getMessage();
        Sender sender = senderService.getSenderByName(request.getName());
        if ( authService.validateBearerToken(httpServletRequest.getHeader("Authorization"), sender) ) {
            if ( PATTERN.matcher(message).matches() ) {
                int historySize = parseInt(message.substring(8));
                List<MessageDto> messages = convertMessagesToDtos( messageService.getLatestMessages(historySize) );

                return ResponseEntity.status(OK)
                        .contentType(APPLICATION_JSON)
                        .body( new MessageHistoryResponse(messages) );
            } else {
                messageService.createNewMessage(message, request.getName());

                return ResponseEntity.status(CREATED)
                        .build();
            }
        }

        throw new AuthException(INVALID_TOKEN_EX);
    }

    private List<MessageDto> convertMessagesToDtos(List<Message> messages) {
        return messages.stream()
                .map(messageMapper::messageToDto)
                .collect(toList());
    }

}
