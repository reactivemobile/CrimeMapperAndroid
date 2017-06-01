import com.reactivemobile.ukpoliceapp.ui.welcome.WelcomeFragment;
import com.reactivemobile.ukpoliceapp.ui.welcome.WelcomePresenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;

/**
 * Created by donalocallaghan on 01/06/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class WelcomePresenterTest {

    @Mock
    private WelcomeFragment welcomeFragment;

    @Test
    public void testDisclaimer() {
        WelcomePresenter presenter = new WelcomePresenter(welcomeFragment);
        presenter.setDisclaimerAccepted(true);
        verify(welcomeFragment).disclaimerAccepted(anyBoolean());
    }
}
