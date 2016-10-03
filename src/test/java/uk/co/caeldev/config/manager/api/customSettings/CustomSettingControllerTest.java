package uk.co.caeldev.config.manager.api.customSettings;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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