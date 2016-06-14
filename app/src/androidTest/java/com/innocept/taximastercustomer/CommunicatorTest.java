package com.innocept.taximastercustomer;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.innocept.taximastercustomer.model.foundation.Location;
import com.innocept.taximastercustomer.model.foundation.Taxi;
import com.innocept.taximastercustomer.model.foundation.TaxiType;
import com.innocept.taximastercustomer.model.network.Communicator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;


/**
 * Created by Dulaj on 6/12/2016.
 */
public class CommunicatorTest {

    Communicator communicator;

    @Before
    public void testInit() throws Exception {
        communicator = new Communicator();
    }

    @Test
    public void testGetAvailableTaxis() {
        List<Taxi> result = communicator.getAvailableTaxis(new Location(7.8731, 80.7718), TaxiType.NANO);
        if(result.size()>0){

        }
    }
}
