package eventFB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.*;

import org.json.JSONObject;

@Entity
@Table(name = "organisedevent")
public class OrganisedEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private float latitude, longitude;
	private String name, description, lieu, organisation, image, href, source, categorie, ville;
	private Date start_time;

	
	public OrganisedEvent()
	{
		latitude = longitude = 0;
		name = description = lieu = organisation = image = href = source = categorie = ville = "";
		start_time = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(Object name) {
		if(name != JSONObject.NULL)
			this.name = (String)name;
		else
			this.name = "";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(Object description) {
		if(description != JSONObject.NULL) {
			String descriptionStr = (String) description;
			descriptionStr = descriptionStr.substring(0, (descriptionStr.length() <= 250)? descriptionStr.length() : 250);
			this.description = descriptionStr;
		}
		else
			this.description = "";
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
		try {
			Date date = formatter.parse(start_time);
			this.start_time = date;
		} catch (ParseException e1) {
			e1.printStackTrace();

			this.start_time = null;
		}
	}

	public String getLieu() {
		return lieu;
	}

	public void setLieu(Object lieu) {
		if(lieu != JSONObject.NULL)
			this.lieu = (String)lieu;
		else
			this.lieu = "";
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Object organisation) {
		if(organisation != JSONObject.NULL)
			this.organisation = (String)organisation;
		else
			this.organisation = "";
	}

	public String getImage() {
		return image;
	}

	public void setImage(Object image) {
		if(image != JSONObject.NULL)
			this.image = (String)image;
		else
			this.image = "";
	}

	public String getHref() {
		return href;
	}

	public void setHref(Object href) {
		if(href != JSONObject.NULL)
			this.href = (String)href;
		else
			this.href = "";
	}

	public String getSource() {
		return source;
	}

	public void setSource(Object source) {
		if(source != JSONObject.NULL)
			this.source = (String)source;
		else
			this.source = "";
	}

	public String getVille() {
		return ville;
	}

	public void setVille(Object ville) {
		if(ville != JSONObject.NULL)
			this.ville = (String)ville;
		else
			this.ville = "";
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(Object categorie) {
		if(categorie != JSONObject.NULL)
			this.categorie = (String)categorie;
		else
			this.categorie = "";
	}
	
	

}
