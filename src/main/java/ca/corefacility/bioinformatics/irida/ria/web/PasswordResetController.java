package ca.corefacility.bioinformatics.irida.ria.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.corefacility.bioinformatics.irida.exceptions.EntityNotFoundException;
import ca.corefacility.bioinformatics.irida.model.user.PasswordReset;
import ca.corefacility.bioinformatics.irida.model.user.Role;
import ca.corefacility.bioinformatics.irida.model.user.User;
import ca.corefacility.bioinformatics.irida.service.user.PasswordResetService;
import ca.corefacility.bioinformatics.irida.service.user.UserService;

import com.google.common.collect.ImmutableList;

/**
 * Controller for handling password reset flow
 * 
 * @author Thomas Matthews <thomas.matthews@phac-aspc.gc.ca>
 *
 */
@Controller
@RequestMapping(value = "/password_reset")
public class PasswordResetController {
	private final String PASSWORD_RESET_PAGE = "user/password_reset";
	private final String PASSWORD_RESET_SUCCESS = "user/password_reset_success";
	private final String CREATE_RESET_PAGE = "password/create_password_reset";
	private final String RESET_CREATED_PAGE = "password/reset_created";
	private final UserService userService;
	private final PasswordResetService passwordResetService;
	private final MessageSource messageSource;

	@Autowired
	public PasswordResetController(UserService userService, PasswordResetService passwordResetService,
			MessageSource messageSource) {
		this.userService = userService;
		this.passwordResetService = passwordResetService;
		this.messageSource = messageSource;
	}

	/**
	 * Get the password reset page
	 * 
	 * @param resetId
	 *            The ID of the {@link PasswordReset}
	 * @param model
	 *            A model for the page
	 * @return The string name of the page
	 */
	@RequestMapping(value = "/{resetId}", method = RequestMethod.GET)
	public String getResetPage(@PathVariable String resetId, Model model) {
		setAuthentication();

		PasswordReset passwordReset = passwordResetService.read(resetId);
		User user = passwordReset.getUser();

		model.addAttribute("user", user);
		model.addAttribute("passwordReset", passwordReset);
		if (!model.containsAttribute("errors")) {
			model.addAttribute("errors", new HashMap<>());
		}

		return PASSWORD_RESET_PAGE;
	}

	/**
	 * Send the new password for a given password reset
	 * 
	 * @param resetId
	 *            The ID of the {@link PasswordReset}
	 * @param password
	 *            The new password to set
	 * @param confirmPassword
	 *            Confirm the new password
	 * @param model
	 *            A model for the given page
	 * @param locale
	 *            The locale of the request
	 * @return The string name of the success view, or on failure the
	 *         getResetPage view
	 */
	@RequestMapping(value = "/{resetId}", method = RequestMethod.POST)
	public String sendNewPassword(@PathVariable String resetId, @RequestParam String password,
			@RequestParam String confirmPassword, Model model, Locale locale) {
		setAuthentication();
		Map<String, String> errors = new HashMap<>();

		// read the reset to verify it exists first
		PasswordReset passwordReset = passwordResetService.read(resetId);
		User user = passwordReset.getUser();

		if (!password.equals(confirmPassword)) {
			errors.put("password", messageSource.getMessage("user.edit.password.match", null, locale));
		}

		if (errors.isEmpty()) {
			// Set the user's authentication to update the password
			Authentication token = new UsernamePasswordAuthenticationToken(user, password, ImmutableList.of(user
					.getSystemRole()));
			SecurityContextHolder.getContext().setAuthentication(token);

			try {
				userService.changePassword(user.getId(), password);
			} catch (ConstraintViolationException ex) {
				errors.put("password", ex.getMessage());
			}
		}

		if (!errors.isEmpty()) {
			model.addAttribute("errors", errors);
			return getResetPage(resetId, model);
		} else {
			passwordResetService.delete(resetId);
			model.addAttribute("user", user);
			SecurityContextHolder.clearContext();
			return PASSWORD_RESET_SUCCESS;
		}
	}

	/**
	 * Get the reset password page
	 * 
	 * @param model
	 *            Model for this view
	 * @return The view name for the email entry page
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String noLoginResetPassword(Model model) {
		return CREATE_RESET_PAGE;
	}

	/**
	 * Create a password reset for the given email address
	 * 
	 * @param email
	 *            The email address to create a password reset for
	 * @param model
	 *            Model for the view
	 * @return Reset created page if the email exists in the system
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String submitEmail(@RequestParam String email, Model model) {
		setAuthentication();
		String page;

		model.addAttribute("email",email);

		try {
			userService.loadUserByEmail(email);
			page = RESET_CREATED_PAGE;
		} catch (EntityNotFoundException ex) {
			model.addAttribute("emailError", true);
			SecurityContextHolder.clearContext();
			page = noLoginResetPassword(model);
		}

		return page;
	}

	/**
	 * Create a new {@link PasswordReset} for the given {@link User}
	 * 
	 * @param userId
	 *            The ID of the {@link User}
	 */
	@RequestMapping("/ajax/create/{userId}")
	@ResponseBody
	public void createNewPasswordReset(@PathVariable Long userId) {
		User user = userService.read(userId);
		PasswordReset passwordReset = new PasswordReset(user);
		passwordResetService.create(passwordReset);
	}

	/**
	 * Set an anonymous authentication token
	 */
	private void setAuthentication() {
		AnonymousAuthenticationToken anonymousToken = new AnonymousAuthenticationToken("nobody", "nobody",
				ImmutableList.of(Role.ROLE_ANONYMOUS));
		SecurityContextHolder.getContext().setAuthentication(anonymousToken);
	}

}
