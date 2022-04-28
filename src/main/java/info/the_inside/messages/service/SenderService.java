package info.the_inside.messages.service;

import info.the_inside.messages.model.Sender;

import javax.security.auth.login.AccountNotFoundException;

public interface SenderService {

    Sender getSenderByName(String name) throws AccountNotFoundException;

}
