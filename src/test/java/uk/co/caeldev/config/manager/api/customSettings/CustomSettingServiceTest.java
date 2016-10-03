package uk.co.caeldev.config.manager.api.customSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;

import static uk.org.fyodor.generators.RDG.list;
import static uk.org.fyodor.generators.RDG.string;
import static uk.org.fyodor.generators.RDG.value;

@RunWith(MockitoJUnitRunner.class)
public class CustomSettingServiceTest {

    private CustomSettingService customSettingService;

    @Before
    public void testee() {
        customSettingService = new CustomSettingService();
    }

}