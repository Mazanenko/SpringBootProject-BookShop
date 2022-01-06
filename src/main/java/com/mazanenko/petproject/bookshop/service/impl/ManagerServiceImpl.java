package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.annotation.LogException;
import com.mazanenko.petproject.bookshop.entity.Manager;
import com.mazanenko.petproject.bookshop.repository.ManagerRepository;
import com.mazanenko.petproject.bookshop.service.ManagerService;
import com.mazanenko.petproject.bookshop.service.RESTConsumerForManagerEmail;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {

    private ManagerRepository managerRepo;
    private RESTConsumerForManagerEmail restConsumerForManagerEmail;
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerServiceImpl.class);

    public ManagerServiceImpl() {
    }

    @Autowired
    public ManagerServiceImpl(ManagerRepository managerRepo, RESTConsumerForManagerEmail restConsumerForManagerEmail) {
        this.managerRepo = managerRepo;
        this.restConsumerForManagerEmail = restConsumerForManagerEmail;
    }

    @Override
    @LogException
    public String createManager(Manager manager) throws Exception {
        if (manager == null) {
            throw new Exception("manager is null");
        }
        String password = generatePassword();
        String cryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        manager.setPassword(cryptedPassword);

        String email = generateEmail(manager);
        manager.setEmail(email);

        if (restConsumerForManagerEmail.createEmail(manager.getEmail(), password)) {
            try {
                managerRepo.saveAndFlush(manager);
            } catch (DataAccessException e) {
                LOGGER.error("Exception \"{}\" while trying save manager: {} {} with email: {}",
                        e.getCause().getMessage(), manager.getName(), manager.getSurname(), manager.getEmail());

                if (e.getCause().getMessage().contains("email_unique")) {
                    createManager(manager);
                    return null;
                }
            }
            LOGGER.info("Manager: {} {} with email: {} successfully created", manager.getName(), manager.getSurname(),
                    manager.getEmail());

            return "Manager " + manager.getName() + " " + manager.getSurname() + " successfully added \n"
                    + " Credentials: \n"
                    + "\n Email: " + manager.getEmail()
                    + "\n Password: " + password;

        } else createManager(manager);

        return null;
    }

    @Override
    public Manager getManagerById(Long id) {
        if (id <= 0) {
            return null;
        }
        Manager manager = managerRepo.findById(id).orElse(null);
        if (manager == null || manager.getEmail() == null) {
            return null;
        }

        setRole(manager);
        return manager;
    }

    @Override
    public Manager getManagerByEmail(String email) {
        if (email == null) {
            return null;
        }
        Manager manager = managerRepo.findByEmail(email).orElse(null);
        if (manager == null) {
            return null;
        }

        setRole(manager);
        return manager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Manager> getAllManagers() {
        List<Manager> list = managerRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        list.removeIf(manager -> manager.getEmail().equals("admin"));
        return list;
    }

    @Override
    public void updateManagerById(Long id, Manager updatedManager) throws Exception {
        if (id <= 0 || updatedManager == null) {
            return;
        }

        Manager managerFromDatabase = managerRepo.findById(id).orElse(null);
        if (managerFromDatabase == null) {
            return;
        }

        updatedManager.setId(id);
        updatePassword(updatedManager, managerFromDatabase);
        managerRepo.save(updatedManager);

        if (!managerFromDatabase.equals(updatedManager)) {
            LOGGER.info("The manager's profile {} {} with ID {} and email {} was updated. Now it is {}",
                    managerFromDatabase.getName(), managerFromDatabase.getSurname(), managerFromDatabase.getId(),
                    managerFromDatabase.getEmail(), updatedManager);
        }
    }

    @Override
    public void deleteManagerById(Long id) throws Exception {
        if (id <= 0) {
            return;
        }
        Manager manager = getManagerById(id);
        if (manager == null || manager.getEmail() == null) {
            return;
        }
        restConsumerForManagerEmail.deleteEmail(manager.getEmail());
        managerRepo.deleteById(id);

        LOGGER.info("The manager's profile {} {} with ID {} and email {} was deleted", manager.getName(),
                manager.getSurname(), id, manager.getEmail());
    }


    private String generateEmail(Manager manager) {
        if (manager == null) {
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

    private void updatePassword(Manager updatedManager, Manager managerFromDatabase) throws Exception {
        if (updatedManager == null || managerFromDatabase == null) {
            return;
        }
        if (updatedManager.getPassword().equals(managerFromDatabase.getPassword())) {
            LOGGER.info("Password for manager {} {} with ID {} and email {} do not updated, because current pass and " +
                            "new pass are equals",
                    managerFromDatabase.getName(), managerFromDatabase.getSurname(), managerFromDatabase.getId(),
                    managerFromDatabase.getEmail());
            return;
        }

        String cryptedPassword = BCrypt.hashpw(updatedManager.getPassword(), BCrypt.gensalt());

        if (restConsumerForManagerEmail.changePassword(updatedManager.getEmail(), updatedManager.getPassword())) {
            updatedManager.setPassword(cryptedPassword);
            updatedManager.setId(managerFromDatabase.getId());

            LOGGER.info("Password for manager {} {} with ID {} and email {} was updated",
                    managerFromDatabase.getName(), managerFromDatabase.getSurname(), managerFromDatabase.getId(),
                    managerFromDatabase.getEmail());
        }
    }
}
