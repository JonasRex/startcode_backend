package dtos;

import entities.RenameMe;
import entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SignupDTO {
    private int id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private List<RenameMeDTO> renameMeDTOS = new ArrayList<>(); // List of IDs.


    public SignupDTO(User user) {
        if (user.getId() != 0)
            this.id = user.getId();
        this.userName = user.getUserName();
        this.password = user.getUserPass();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        for (RenameMe renameMe : user.getRenameMesList()) {
            this.renameMeDTOS.add(new RenameMeDTO(renameMe));
        }
    }


    public User getEntity() {
        User u = new User(this.userName, password, this.firstName, this.lastName, this.email);
        this.renameMeDTOS.forEach(renameMeDTO -> u.addRenameMe(renameMeDTO.getEntity()));
        return u;
    }

    public static List<UserDTO> toList(List<User> users) {
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RenameMeDTO> getRenameMeDTOS() {
        return renameMeDTOS;
    }



}
