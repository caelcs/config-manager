package uk.co.caeldev.config.manager.api.customSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static uk.org.fyodor.generators.RDG.string;

@RunWith(MockitoJUnitRunner.class)
public class CustomSettingControllerTest {

    private CustomSettingController customSettingController;

    @Mock
    private CustomSettingService customSettingService;

    @Before
    public void testee() {
        customSettingController = new CustomSettingController(customSettingService);
    }
}