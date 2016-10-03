package uk.co.caeldev.config.manager.api.buildConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BuildConfigController {

    private final BuildConfigService buildConfigService;

    @Autowired
    public BuildConfigController(final BuildConfigService buildConfigService) {
        this.buildConfigService = buildConfigService;
    }

    @RequestMapping(path = "/buildconfigs/{env}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<BuildConfig> getBuildConfig(@PathVariable String env) {
        Optional<BuildConfig> buildConfigOptional = buildConfigService.getOne(env);

        if (!buildConfigOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(buildConfigOptional.get());
    }
}
