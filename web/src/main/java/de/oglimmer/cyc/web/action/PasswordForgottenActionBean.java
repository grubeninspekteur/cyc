package de.oglimmer.cyc.web.action;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.WebContainerProperties;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

@DoesNotRequireLogin
public class PasswordForgottenActionBean extends BaseAction {
	private static final String VIEW = "/WEB-INF/jsp/passwordForgotten.jsp";

	private static Logger log = LoggerFactory.getLogger(PasswordForgottenActionBean.class);

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	@Validate(required = true)
	@Getter
	@Setter
	private String email;

	@DefaultHandler
	@DontValidate
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

	@ValidationMethod
	public void validateUser(ValidationErrors errors) {
		if (!getEmail().matches("(?i)^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$")) {
			errors.add("email", new SimpleError("The email address isn''t valid."));
		}
	}

	public Resolution sendPassword() {

		List<User> users = userDao.findByEmail(email.toLowerCase());
		for (User user : users) {
			String newPass = RandomStringUtils.random(10, true, true);
			String hashed = BCrypt.hashpw(newPass, BCrypt.gensalt());
			user.setPassword(hashed);
			userDao.update(user);
			try {
				Email simpleEmail = new SimpleEmail();
				simpleEmail.setHostName(WebContainerProperties.INSTANCE.getSmtpHost());
				if (WebContainerProperties.INSTANCE.getSmtpPort() > 0) {
					simpleEmail.setSmtpPort(WebContainerProperties.INSTANCE.getSmtpPort());
				}
				simpleEmail.setSSLOnConnect(WebContainerProperties.INSTANCE.getSmtpSSL());
				if (!WebContainerProperties.INSTANCE.getSmtpUser().isEmpty()) {
					simpleEmail.setAuthentication(WebContainerProperties.INSTANCE.getSmtpUser(),
							WebContainerProperties.INSTANCE.getSmtpPassword());
				}
				simpleEmail.setFrom(WebContainerProperties.INSTANCE.getSmtpFrom());
				simpleEmail.setSubject("New password");
				simpleEmail.setMsg("Your new password is : " + newPass);
				simpleEmail.addTo(user.getEmail());
				simpleEmail.send();
			} catch (EmailException e) {
				log.error("Failed to send password email", e);
			}
		}

		return new RedirectResolution(LandingActionBean.class);
	}
}