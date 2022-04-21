package dtoFacades;

import dtos.ProfileDTO;
import entities.Profile;
import errorhandling.EntityNotFoundException;
import facades.IFacade;
import facades.ProfileFacade;
import utils.EMF_Creator;

import java.util.List;

public class ProfileDTOFacade implements IFacade<ProfileDTO> {
    private static IFacade<ProfileDTO> instance;
    private static IFacade<Profile> profileFacade;

    public ProfileDTOFacade() {
    }

    public static IFacade<ProfileDTO> getFacade() {
        if (instance == null) {
            profileFacade = ProfileFacade.getFacade(EMF_Creator.createEntityManagerFactory());
            instance = new ProfileDTOFacade();
        }
        return instance;
    }

    @Override
    public ProfileDTO create(ProfileDTO profileDTO) {
        Profile p = profileDTO.getEntity();
        p = profileFacade.create(p);
        return new ProfileDTO(p);
    }

    @Override
    public ProfileDTO getById(int id) throws EntityNotFoundException {
        return new ProfileDTO(profileFacade.getById(id));
    }

    @Override
    public List<ProfileDTO> getAll() {
        return ProfileDTO.toList(profileFacade.getAll());
    }

    @Override
    public ProfileDTO update(ProfileDTO profileDTO) throws EntityNotFoundException {
        Profile profile = profileDTO.getEntity();
        profile.setId(profileDTO.getId());
        Profile p = profileFacade.update(profile);
        return new ProfileDTO(p);
    }

    @Override
    public ProfileDTO delete(int id) throws EntityNotFoundException {
        return new ProfileDTO(profileFacade.delete(id));
    }

    public ProfileDTO addRelation(int id1, int id2) throws EntityNotFoundException {
        return new ProfileDTO(profileFacade.addRelation(id1, id2));
    }

    @Override
    public ProfileDTO removeRelation(int id1, int id2) throws EntityNotFoundException {
        return new ProfileDTO(profileFacade.removeRelation(id1, id2));
    }

    @Override
    public long getCount() {
        return profileFacade.getCount();
    }
}
