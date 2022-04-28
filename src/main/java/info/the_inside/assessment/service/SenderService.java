package info.the_inside.assessment.service;

import info.the_inside.assessment.model.Sender;

import javax.security.auth.login.AccountNotFoundException;

public interface SenderService {

    Sender getSenderByName(String name) throws AccountNotFoundException;

}
