package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.entity.Manager;
import com.mazanenko.petproject.bookshop.repository.ManagerRepository;
import com.mazanenko.petproject.bookshop.service.ManagerService;
import com.mazanenko.petproject.bookshop.service.RESTConsumerForManagerEmail;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {

    private ManagerRepository managerRepo;
    private RESTConsumerForManagerEmail restConsumerForManagerEmail;

    public ManagerServiceImpl() {
    }

    @Autowired
    public ManagerServiceImpl(ManagerRepository managerRepo, RESTConsumerForManagerEmail restConsumerForManagerEmail) {
        this.managerRepo = managerRepo;
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
            //managerRepo.save(manager);
            managerRepo.saveAndFlush(manager);
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
    public Manager getManagerById(Long id) {
        if (id <= 0) {
            return null;
        }
        Manager manager = managerRepo.findById(id).orElse(null);
        if (manager != null) {
            setRole(manager);
        }
        return manager;
    }

    @Override
    public Manager getManagerByEmail(String email) {
        if (email == null) {
            return null;
        }
        Manager manager = managerRepo.findByEmail(email).orElse(null);
        if (manager != null) {
            setRole(manager);
        }
        return manager;
    }

    @Override
    public List<Manager> getAllManagers() {
        List<Manager> list = managerRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        list.removeIf(manager -> manager.getEmail().equals("admin"));
        return list;
    }

    @Override
    public void updateManagerById(Long id, Manager manager) throws Exception {
        if (id <= 0) {
            return;
        }

        Manager managerFromDatabase = managerRepo.findById(id).orElse(null);
        if (managerFromDatabase == null || manager == null) {
            return;
        }

        if (!(manager.getPassword().equals(managerFromDatabase.getPassword()))) {
            String cryptedPassword = BCrypt.hashpw(manager.getPassword(), BCrypt.gensalt());
            restConsumerForManagerEmail.changePassword(manager.getEmail(), manager.getPassword());
            manager.setPassword(cryptedPassword);
            manager.setId(managerFromDatabase.getId());
        }
        managerRepo.save(manager);
    }

    @Override
    public void deleteManagerById(Long id) throws Exception {
        if (id <= 0) {
            return;
        }
        restConsumerForManagerEmail.deleteEmail(getManagerById(id).getEmail());
        managerRepo.deleteById(id);
    }

    private String generateEmail(Manager manager) {
        if(manager == null) {
            return null;
        }
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

    private void setRole(Manager manager) {
        if (manager == null) {
            return;
        }
        if (manager.getEmail().equals("admin")) {
            manager.setRole("ROLE_ADMIN");
        } else manager.setRole("ROLE_MANAGER");
    }
}
