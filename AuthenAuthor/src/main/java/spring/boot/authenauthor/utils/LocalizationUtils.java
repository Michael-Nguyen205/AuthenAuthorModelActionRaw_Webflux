package spring.boot.authenauthor.utils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;
import java.util.Locale;

@RequiredArgsConstructor
@Component
public class LocalizationUtils {
    private final MessageSource messageSource;
    private final LocaleContextResolver localeResolver;

    public String getLocalizedMessage(String messageKey, ServerWebExchange exchange, Object... params) {
        Locale locale = localeResolver.resolveLocaleContext(exchange).getLocale();

        return messageSource.getMessage(messageKey, params, locale);
    }
}
