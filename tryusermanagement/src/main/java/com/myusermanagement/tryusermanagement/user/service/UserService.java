package com.myusermanagement.tryusermanagement.user.service;

import com.myusermanagement.tryusermanagement.user.dto.UserDto;
import com.myusermanagement.tryusermanagement.user.dto.requests.CreateOrUpdateUserDto;
import com.myusermanagement.tryusermanagement.user.dto.requests.RegisterUserAccountDto;
import com.myusermanagement.tryusermanagement.user.entities.*;
import com.myusermanagement.tryusermanagement.user.exception.*;
import com.myusermanagement.tryusermanagement.user.repository.BankBranchAddressRepository;
import com.myusermanagement.tryusermanagement.user.repository.BankContactRepository;
import com.myusermanagement.tryusermanagement.user.repository.RoleRepository;
import com.myusermanagement.tryusermanagement.user.repository.UserRepository;
import com.google.common.base.Strings;
import com.myusermanagement.tryusermanagement.user.service.validation.EmailValidator;
import com.myusermanagement.tryusermanagement.user.service.validation.PasswordValidator;
import com.myusermanagement.tryusermanagement.user.service.validation.PhoneValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankContactRepository bankContactRepository;

    @Autowired
    private BankBranchAddressRepository bankBranchAddressRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${microservice.security.salt}")
    private String salt;



    private PasswordValidator passwordValidator;
    private EmailValidator emailValidator;
    private PhoneValidator phoneValidator;

    public UserService() {
        passwordValidator = new PasswordValidator();
        emailValidator = new EmailValidator();
        phoneValidator = new PhoneValidator();
    }

    public List<UserDto> getUserPresentationList() {
        ArrayList<UserDto> listDto = new ArrayList<>();
        Iterable<User> list = getUserList();
        list.forEach(e -> listDto.add(new UserDto(e)));
        return listDto;
    }

    public User getUserById(Long id) {
        if (id == null) {
            throw new InvalidUserIdentifierException("User Id cannot be null");
        }
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new UserNotFoundException(String.format("User not found for Id = %s", id));
    }

    public User getUserByUsername(String username) {
        if (username == null) {
            throw new InvalidUsernameException("username cannot be null");
        }
        return userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email) {
        if (email == null) {
            throw new InvalidEmailException("email cannot be null");
        }
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User registerUserAccount(RegisterUserAccountDto registerUserAccountDto) {
        if (registerUserAccountDto == null) {
            throw new InvalidUserDataException("User account data cannot be null");
        }

        checkIfUsernameNotUsed(registerUserAccountDto.getUsername());
        passwordValidator.checkPassword(registerUserAccountDto.getPassword());
        emailValidator.checkEmail(registerUserAccountDto.getEmail());

        checkIfEmailNotUsed(registerUserAccountDto.getEmail());

        // create the new user account: not all the user information required
        User user = new User();
        user.setUsername(registerUserAccountDto.getUsername());
        user.setPassword(EncryptionService.encrypt(registerUserAccountDto.getPassword(), salt));

        user.setNik(registerUserAccountDto.getUsername());
       // user.setEnabled(true);
        user.setSecured(false);

        // set gender
        AdminLevel adminLevel = AdminLevel.getValidAdminLevel(registerUserAccountDto.getAdminLevel());
        user.setAdminLevel(adminLevel);

        addUserRole(user, Role.SUPER_USER);
        user.setCreationDt(LocalDateTime.now());

        User userCreated = userRepository.save(user);

        // set contact
        BankContact bankContact = new BankContact();
        bankContact.setEmail(registerUserAccountDto.getEmail());

        addBankContactOnUser(userCreated, bankContact);

        // set empty address
        addBranchAddressOnUser(userCreated, new BankBranchAddress());

        userCreated = userRepository.save(userCreated);

        log.info(String.format("User %s has been created.", userCreated.getId()));
        return userCreated;
    }

    // check if the username has not been registered
    public void checkIfUsernameNotUsed(String username) {
        User userByUsername = getUserByUsername(username);
        if (userByUsername != null) {
            String msg = String.format("The username %s it's already in use from another user with ID = %s",
                    userByUsername.getUsername(), userByUsername.getId());
            log.error(msg);
            throw new InvalidUserDataException(msg);
        }
    }

    // check if the email has not been registered
    public void checkIfEmailNotUsed(String email) {
        User userByEmail = getUserByEmail(email);
        if (userByEmail != null) {
            String msg = String.format("The email %s it's already in use from another user with ID = %s",
                    userByEmail.getBankContact().getEmail(), userByEmail.getId());
            log.error(msg);
            throw new InvalidUserDataException(String.format("This email %s it's already in use.",
                    userByEmail.getBankContact().getEmail()));
        }
    }

    @Transactional
    public User createUser(CreateOrUpdateUserDto createOrUpdateUserDto) {
        if (createOrUpdateUserDto == null) {
            throw new InvalidUserDataException("User account data cannot be null");
        }

        checkIfUsernameNotUsed(createOrUpdateUserDto.getUsername());
        checkIfEmailNotUsed(createOrUpdateUserDto.getEmail());
        passwordValidator.checkPassword(createOrUpdateUserDto.getPassword());
        emailValidator.checkEmail(createOrUpdateUserDto.getEmail());
        phoneValidator.checkPhone(createOrUpdateUserDto.getPhone());

        // create the user
        User user = new User();
        user.setUsername(createOrUpdateUserDto.getUsername());
        user.setPassword(EncryptionService.encrypt(createOrUpdateUserDto.getPassword(), salt));

        user.setNik(createOrUpdateUserDto.getNik());

        // set gender
        AdminLevel adminLevel = AdminLevel.getValidAdminLevel(createOrUpdateUserDto.getAdminLevel());
        user.setAdminLevel(adminLevel);
/*
        // date of birth
        user.setBirthDate(createUserDTO.getBirthDate());

        user.setEnabled(true);
        user.setSecured(createUserDTO.isSecured());

        user.setNote(createUserDTO.getNote());
        user.setCreationDt(LocalDateTime.now());

 */

        // set default user the role
        addUserRole(user, Role.SUPER_USER);

        User userCreated = userRepository.save(user);

        // set contact
        BankContact contact = new BankContact();
        contact.setPhone(createOrUpdateUserDto.getPhone());
        contact.setEmail(createOrUpdateUserDto.getEmail());
        contact.setOfficial_website(createOrUpdateUserDto.getOfficial_website());

        addBankContactOnUser(userCreated, contact);

        // set address
        BankBranchAddress address = new BankBranchAddress();
        address.setProvince(createOrUpdateUserDto.getProvince());
        address.setCity(createOrUpdateUserDto.getCity());
        address.setStreet(createOrUpdateUserDto.getStreet());
        address.setPostal_code(createOrUpdateUserDto.getPostal_code());

        addBranchAddressOnUser(userCreated, address);

        userCreated = userRepository.save(userCreated);

        log.info(String.format("User %s has been created.", userCreated.getId()));
        return userCreated;
    }

    public void addBankContactOnUser(User user, BankContact bankContact) {
        bankContact.setUser(user);
        user.setBankContact(bankContact);

        log.debug(String.format("Contact information set on User %s .", user.getId()));
    }

    public void addBranchAddressOnUser(User user, BankBranchAddress bankBranchAddress) {
        bankBranchAddress.setUser(user);
        user.setBankBranchAddress(bankBranchAddress);

        log.debug(String.format("Address information set on User %s .", user.getId()));
    }

    public void addUserRole(User user, long roleId) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RoleNotFoundException("Role cannot be null");
        }
        user.getRoles().add(roleOpt.get());
    }

    @Transactional
    public User updateUser(Long id, CreateOrUpdateUserDto updateUserDTO) {
        if (id == null) {
            throw new InvalidUserIdentifierException("Id cannot be null");
        }
        if (updateUserDTO == null) {
            throw new InvalidUserDataException("User account data cannot be null");
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("The user with Id = %s doesn't exists", id));
        }
        User user = userOpt.get();

        // check if the username has not been registered
        User userByUsername = getUserByUsername(updateUserDTO.getUsername());
        if (userByUsername != null) {
            // check if the user's id is different than the actual user
            if (!user.getId().equals(userByUsername.getId())) {
                String msg = String.format("The username %s it's already in use from another user with ID = %s",
                        updateUserDTO.getUsername(), userByUsername.getId());
                log.error(msg);
                throw new InvalidUserDataException(msg);
            }
        }

        passwordValidator.checkPassword(updateUserDTO.getPassword());
        emailValidator.checkEmail(updateUserDTO.getEmail());
        phoneValidator.checkPhone(updateUserDTO.getPhone());

        // check if the new email has not been registered yet
        User userEmail = getUserByEmail(updateUserDTO.getEmail());
        if (userEmail != null) {
            // check if the user's email is different than the actual user
            if (!user.getId().equals(userEmail.getId())) {
                String msg = String.format("The email %s it's already in use from another user with ID = %s",
                        updateUserDTO.getEmail(), userEmail.getId());
                log.error(msg);
                throw new InvalidUserDataException(msg);
            }
        }

        // update the user
        user.setUsername(updateUserDTO.getUsername());

        // using the user's salt to secure the new validated password
        user.setPassword(EncryptionService.encrypt(updateUserDTO.getPassword(), salt));
        user.setUsername(updateUserDTO.getUsername());
        user.setNik(updateUserDTO.getNik());

        // set gender
        AdminLevel adminLevel = AdminLevel.getValidAdminLevel(updateUserDTO.getAdminLevel());
        user.setAdminLevel(adminLevel);
/*
        // date of birth
        user.setBirthDate(updateUserDTO.getBirthDate());

        user.setEnabled(updateUserDTO.isEnabled());
        user.setNote(updateUserDTO.getNote());

 */

        // set contact: entity always present
        BankContact contact = user.getBankContact();
        contact.setPhone(updateUserDTO.getPhone());
        contact.setEmail(updateUserDTO.getEmail());
        contact.setOfficial_website(updateUserDTO.getOfficial_website());

        user.setUpdatedDt(LocalDateTime.now());

        // set address
        BankBranchAddress address = user.getBankBranchAddress();
        if (address == null) {
            address = new BankBranchAddress();
        }
        address.setProvince(updateUserDTO.getProvince());
        address.setCity(updateUserDTO.getCity());
        address.setStreet(updateUserDTO.getStreet());
        address.setPostal_code(updateUserDTO.getPostal_code());

        addBranchAddressOnUser(user, address);

        User userUpdated = userRepository.save(user);
        log.info(String.format("User %s has been updated.", user.getId()));

        return userUpdated;
    }

    public Iterable<User> getUserList() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (id == null) {
            throw new InvalidUserIdentifierException("Id cannot be null");
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", id));
        }

        // only not secured users can be deleted
        User user = userOpt.get();
        if (user.isSecured()) {
            throw new UserIsSecuredException(String.format("User %s is secured and cannot be deleted.", id));
        }

        userRepository.deleteById(id);
        log.info(String.format("User %s has been deleted.", id));
    }

    @Transactional
    public User login(String username, String password) {
        if ((Strings.isNullOrEmpty(username)) || (Strings.isNullOrEmpty(password))) {
            throw new InvalidLoginException("Username or Password cannot be null or empty");
        }

        User user = getUserByUsername(username);
        if (user == null) {
            // invalid username
            throw new InvalidLoginException("Invalid username or password");
        }

        log.info(String.format("Login request from %s", username));

        // check the password
        if (EncryptionService.isPasswordValid(password, user.getPassword(), salt)) {
            // check if the user is enabled
            if (!user.isSecured()) {
                // not enabled
                throw new InvalidLoginException("User is not enabled");
            }

            // update the last login timestamp
            user.setLoginDt(LocalDateTime.now());
            userRepository.save(user);

            log.info(String.format("Valid login for %s", username));
        } else {
            throw new InvalidLoginException("Invalid username or password");
        }
        return user;
    }

    // add or remove a role on user

    @Transactional
    public User addRole(Long id, Long roleId) {
        // check user
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", id));
        }
        User user = userOpt.get();

        // check role
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }

        Role role = roleOpt.get();

        user.getRoles().add(role);
        user.setUpdatedDt(LocalDateTime.now());

        userRepository.save(user);
        log.info(String.format("Added role %s on user id = %s", role.getRole(), user.getId()));

        return user;
    }

    @Transactional
    public User removeRole(Long id, Long roleId) {
        // check user
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", id));
        }
        User user = userOpt.get();

        // check role
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }

        Role role = roleOpt.get();

        user.getRoles().remove(role);
        user.setUpdatedDt(LocalDateTime.now());

        userRepository.save(user);
        log.info(String.format("Removed role %s on user id = %s", role.getRole(), user.getId()));

        return user;
    }

}

