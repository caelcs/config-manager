package uk.co.caeldev.config.manager.api.customSettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomSettingController {

    private final CustomSettingService customSettingService;

    @Autowired
    public CustomSettingController(final CustomSettingService customSettingService) {
        this.customSettingService = customSettingService;
    }
}
