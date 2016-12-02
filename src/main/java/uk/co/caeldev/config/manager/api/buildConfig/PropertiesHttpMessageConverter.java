package uk.co.caeldev.config.manager.api.buildConfig;

import com.google.gson.Gson;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertiesHttpMessageConverter extends AbstractHttpMessageConverter<BuildConfig> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");


    private final List<Charset> availableCharsets;

    private boolean writeAcceptCharset = true;

    public PropertiesHttpMessageConverter() {
        this(DEFAULT_CHARSET);
    }

    /**
     * A constructor accepting a default charset to use if the requested content
     * type does not specify one.
     */
    public PropertiesHttpMessageConverter(Charset defaultCharset) {
        super(new MediaType("text", "properties", DEFAULT_CHARSET));
        this.availableCharsets = new ArrayList<>(Charset.availableCharsets().values());
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    public void setWriteAcceptCharset(boolean writeAcceptCharset) {
        this.writeAcceptCharset = writeAcceptCharset;
    }

    @Override
    protected BuildConfig readInternal(Class<? extends BuildConfig> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
        InputStream body = inputMessage.getBody();
        String valueAsString = StreamUtils.copyToString(body, charset);
        Gson gson = new Gson();
        return gson.fromJson(valueAsString, BuildConfig.class);
    }

    @Override
    protected void writeInternal(BuildConfig buildConfig, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (this.writeAcceptCharset) {
            outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
        }
        Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());

        final Properties properties = new Properties();
        properties.put("environment", buildConfig.getEnvironment());
        buildConfig.getAttributes().forEach((String key, String value) -> {
            properties.put(key, value);
        });

        StringWriter sw = new StringWriter();

        try {
            properties.store(sw, "build config");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        StreamUtils.copy(sw.toString(), charset, outputMessage.getBody());
    }

    private Charset getContentTypeCharset(MediaType contentType) {
        if (contentType != null && contentType.getCharset() != null) {
            return contentType.getCharset();
        }
        else {
            return DEFAULT_CHARSET;
        }
    }

    protected List<Charset> getAcceptedCharsets() {
        return this.availableCharsets;
    }
}
