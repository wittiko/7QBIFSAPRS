package at.htlv.serveradministration.service;

import at.htlv.serveradministration.domain.Postcode;
import at.htlv.serveradministration.repository.PostcodeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PostcodeServiceImpl implements PostcodeService {

	@Autowired
    PostcodeRepository postcodeRepository;

	public long countAllPostcodes() {
        return postcodeRepository.count();
    }

	public void deletePostcode(Postcode postcode) {
        postcodeRepository.delete(postcode);
    }

	public Postcode findPostcode(Long id) {
        return postcodeRepository.findOne(id);
    }

	public List<Postcode> findAllPostcodes() {
        return postcodeRepository.findAll();
    }

	public List<Postcode> findPostcodeEntries(int firstResult, int maxResults) {
        return postcodeRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void savePostcode(Postcode postcode) {
        postcodeRepository.save(postcode);
    }

	public Postcode updatePostcode(Postcode postcode) {
        return postcodeRepository.save(postcode);
    }
}
