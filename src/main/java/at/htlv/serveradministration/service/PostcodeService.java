package at.htlv.serveradministration.service;

import at.htlv.serveradministration.domain.Postcode;
import java.util.List;

public interface PostcodeService {

	public abstract long countAllPostcodes();


	public abstract void deletePostcode(Postcode postcode);


	public abstract Postcode findPostcode(Long id);


	public abstract List<Postcode> findAllPostcodes();


	public abstract List<Postcode> findPostcodeEntries(int firstResult, int maxResults);


	public abstract void savePostcode(Postcode postcode);


	public abstract Postcode updatePostcode(Postcode postcode);

}
