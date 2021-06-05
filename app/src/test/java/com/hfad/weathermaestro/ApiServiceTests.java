package com.hfad.weathermaestro;

import com.hfad.models.Location;
import com.hfad.models.CurrentWeather;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ApiServiceTests {

    @Test
    public void ShouldReturnWeather_WhenGivenValidLocation() {
        ApiService apiService = new ApiService();
        Location location = new Location();
        location.city = "Seattle";

        CurrentWeather weather = apiService.GetCurrentWeather(location);

        System.out.println(weather.weather.get(0).main);
        System.out.println(weather.weather.get(0).description);
        System.out.println(weather.main.temp);
        assertNotNull(weather);
    }
}