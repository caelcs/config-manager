package uk.co.caeldev.config.manager.api.buildConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BuildConfigController {

    private final BuildConfigService buildConfigService;

    @Autowired
    public BuildConfigController(final BuildConfigService buildConfigService) {
        this.buildConfigService = buildConfigService;
    }

    public ResponseEntity<BuildConfig> getBuildConfig(String env) {

        Optional<BuildConfig> buildConfigOptional = buildConfigService.getOne(env);

        if (!buildConfigOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(buildConfigOptional.get());
        
    }
}
