package uk.co.caeldev.config.manager.api.customSettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class CustomSettingController {

    private final CustomSettingService customSettingService;

    @Autowired
    public CustomSettingController(final CustomSettingService customSettingService) {
        this.customSettingService = customSettingService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/env/{env}/customsettings/{key}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getOne(@PathVariable String env, @PathVariable String key) {
        Optional<String> value = customSettingService.getOne(env, key);

        if (!value.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(value.get());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/env/{env}/customsettings", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map> getCredentials(@PathVariable String env) {
        Optional<Map<String, String>> credentials = customSettingService.getCredentials(env);

        if (!credentials.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(credentials.get());
    }
}
