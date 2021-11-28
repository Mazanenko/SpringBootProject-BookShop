package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.dao.ManagerDAO;
import com.mazanenko.petproject.bookshop.entity.Manager;
import com.mazanenko.petproject.bookshop.service.ManagerService;
import com.mazanenko.petproject.bookshop.service.RESTConsumerForManagerEmail;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl implements ManagerService {

    private ManagerDAO managerDAO;
    private RESTConsumerForManagerEmail restConsumerForManagerEmail;

    public ManagerServiceImpl() {
    }

    @Autowired
    public ManagerServiceImpl(ManagerDAO managerDAO, RESTConsumerForManagerEmail restConsumerForManagerEmail) {
        this.managerDAO = managerDAO;
        this.restConsumerForManagerEmail = restConsumerForManagerEmail;
    }

    @Override
    public String createManager(Manager manager) throws Exception {
        if (manager == null) {
            throw new Exception("manager is null");
        }

        String password = generatePassword();
        String cryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        manager.setPassword(cryptedPassword);

        String email = generateEmail(manager);
        manager.setEmail(email);

        String credentials = "Manager " + manager.getName() + " " + manager.getSurname() + " successfully added \n"
                + " Credentials: " + System.lineSeparator()
                + "\n Email: " + manager.getEmail()
                + "\n Password: " + password;

        try {
            managerDAO.create(manager);
        } catch (DataAccessException e) {
            if (e.getCause().getMessage().contains("email_unique")) {
                String newEmail = generateEmail(manager);
                manager.setEmail(newEmail);
                return credentials;
            }
        } finally {
            restConsumerForManagerEmail.createEmail(manager.getEmail(), password);
        }
        return credentials;
    }

    @Override
    public Manager getManagerById(int id) {
        if (id <= 0) {
            return null;
        }
        return managerDAO.read(id);
    }

    @Override
    public Manager getManagerByEmail(String email) {
        if (email == null) {
            return null;
        }
        return managerDAO.readByEmail(email);
    }

    @Override
    public List<Manager> getAllManagers() {
        List<Manager> list = managerDAO.readAll().stream().sorted(Comparator.comparing(Manager::getId))
                .collect(Collectors.toList());
        list.removeIf(manager -> manager.getEmail().equals("admin"));
        return list;
    }

    @Override
    public void updateManagerById(int id, Manager manager) throws Exception {
        if (!(manager.getPassword().equals(managerDAO.read(id).getPassword()))) {
            String cryptedPassword = BCrypt.hashpw(manager.getPassword(), BCrypt.gensalt());
            restConsumerForManagerEmail.changePassword(manager.getEmail(), manager.getPassword());
            manager.setPassword(cryptedPassword);
        }
        managerDAO.update(id, manager);
    }

    @Override
    public void deleteManagerById(int id) throws Exception {
        restConsumerForManagerEmail.deleteEmail(getManagerById(id).getEmail());
        managerDAO.delete(id);
    }

    private String generateEmail(Manager manager) {
        int random = (int) (Math.random() * 1000);
        return "manager_" + manager.getName().toLowerCase().charAt(0) + "."
                + manager.getSurname().toLowerCase() + "_" + random + "@booksland.shop";
    }

    private String generatePassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(4);

        return passwordGenerator.generatePassword(8, digitRule, upperCaseRule, lowerCaseRule);
    }
}
