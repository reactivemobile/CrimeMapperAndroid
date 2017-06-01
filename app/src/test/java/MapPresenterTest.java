import com.reactivemobile.ukpoliceapp.objects.CrimeCategories;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelAvailabilityDate;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelAvailabilityDates;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelCrime;
import com.reactivemobile.ukpoliceapp.rest.RestInterface;
import com.reactivemobile.ukpoliceapp.ui.map.MapFragment;
import com.reactivemobile.ukpoliceapp.ui.map.MapPresenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by donalocallaghan on 01/06/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class MapPresenterTest {

    @Mock
    private MapFragment mapFragment;

    @Mock
    private RestInterface restInterface;

    @Rule
    public RxImmediateSchedulerRule scheduler = new RxImmediateSchedulerRule();

    private MapPresenter presenter;


    @Before
    public void setup() {
        StreetLevelAvailabilityDates streetLevelAvailabilityDates = new StreetLevelAvailabilityDates();
        streetLevelAvailabilityDates.add(new StreetLevelAvailabilityDate());

        when(restInterface.getStreetLevelAvailability()).thenReturn(Observable.just(streetLevelAvailabilityDates));
        when(restInterface.getCrimeCategories(any())).thenReturn(Observable.just(new CrimeCategories()));
        when(restInterface.getStreetLevelCrimes(any(), any(), any())).thenReturn(Observable.just(new ArrayList<>()));

        presenter = new MapPresenter(mapFragment, restInterface);
    }

    @Test
    public void testAvailability() {
        presenter.viewIsReady();
        verify(mapFragment).streetLevelAvailabilityDatesRetrievedOk(any());
    }

    @Test
    public void testCategories() {
        presenter.viewIsReady();
        verify(mapFragment).crimeCategoriesRetrievedOk();
    }

    @Test
    public void testGetCrimesForThisLocation() {
        presenter.viewIsReady();
        presenter.getCrimesForLocationAndDate(0, 0, null);
        verify(mapFragment).streetLevelAvailabilityDatesRetrievedOk(any());
    }
}
