package uk.co.caeldev.config.manager.api.buildConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class BuildConfigController {

    public static final String SOURCE_ENV_PARAM = "sourceEnv";
    public static final String TARGET_ENV_PARAM = "targetEnv";
    private final BuildConfigService buildConfigService;

    @Autowired
    public BuildConfigController(final BuildConfigService buildConfigService) {
        this.buildConfigService = buildConfigService;
    }

    @RequestMapping(value = "/buildconfigs/{env}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<BuildConfig> getBuildConfig(@PathVariable String env) {
        Optional<BuildConfig> buildConfigOptional = buildConfigService.getOne(env);

        if (!buildConfigOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(buildConfigOptional.get());
    }

    @RequestMapping(value = "/buildconfigs/clone",
            method = {RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<BuildConfig> cloneBuildConfig(@RequestBody Map<String, String> request) {
        final String sourceEnv = request.get(SOURCE_ENV_PARAM);
        final String targetEnv = request.get(TARGET_ENV_PARAM);

        try {
            final BuildConfig result = buildConfigService.cloneBuildConfig(sourceEnv, targetEnv);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/buildconfigs", method = {RequestMethod.POST}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<BuildConfig> create(@RequestBody final BuildConfig buildConfig) {
        try {
            buildConfigService.create(buildConfig);
            return new ResponseEntity<>(buildConfig, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<BuildConfig>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
