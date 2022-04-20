package dtos;

import entities.Profile;
import entities.RenameMe;
import entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private List<RenameMeDTO> renameMeDTOS = new ArrayList<>(); // List of IDs.


    public ProfileDTO(Profile profile) {
        if (profile.getId() != 0)
            this.id = profile.getId();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.email = profile.getEmail();
        for (RenameMe renameMe : profile.getRenameMesList()) {
            this.renameMeDTOS.add(new RenameMeDTO(renameMe));
        }
    }


    public Profile getEntity() {
        Profile p = new Profile(this.firstName, this.lastName, this.email);
        this.renameMeDTOS.forEach(renameMeDTO -> p.addRenameMe(renameMeDTO.getEntity()));
        return p;
    }

    public static List<ProfileDTO> toList(List<Profile> profiles) {
        return profiles.stream().map(ProfileDTO::new).collect(Collectors.toList());
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "ProfileDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", renameMeDTOS=" + renameMeDTOS +
                '}';
    }
}
