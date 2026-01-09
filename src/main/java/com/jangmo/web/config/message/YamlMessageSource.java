package com.jangmo.web.config.message;

import net.rakugakibox.util.YamlResourceBundle;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.lang.NonNull;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class YamlMessageSource extends ResourceBundleMessageSource {

	@NotNull
	@Override
	protected ResourceBundle doGetBundle(
		@NonNull String basename, @NonNull Locale locale
	) throws MissingResourceException {
		return ResourceBundle.getBundle(
			basename, locale, YamlResourceBundle.Control.INSTANCE
		);
	}
}
