package lt.vilniustech.bps.dao;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Sort;
import dev.morphia.query.experimental.filters.Filters;
import lt.vilniustech.bps.dto.Route;
import lt.vilniustech.bps.dto.Station;
import lt.vilniustech.bps.dto.StopTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InitialDataTest {
    private Datastore datastore;

    @BeforeEach
    public void before() {
        datastore = createDatastore();
    }

    @Test
    public void createInitialData() {
        Route vilniusKaunasRoute = datastore.save(new Route("Vilnius - Kaunas"));
        Route kaunasVilniusRoute = datastore.save(new Route("Kaunas - Vilnius"));

        createAndSaveVilniusKaunasStations(vilniusKaunasRoute);
        createAndSaveKaunasVilniusStations(kaunasVilniusRoute);
    }

    @Test
    public void checkInitialData() {
        List<Route> routes = datastore.find(Route.class).stream().toList();
        assertEquals(2, routes.size());

        Route vilniusKaunas = datastore.find(Route.class)
                .filter(Filters.eq("name", "Vilnius - Kaunas"))
                .first();
        assertNotNull(vilniusKaunas);
        List<Station> vilniusKaunasStations = datastore.find(Station.class)
                .filter(Filters.eq("route", vilniusKaunas))
                .iterator(new FindOptions()
                        .sort(Sort.ascending("stopOrderNo")))
                .toList();
        assertEquals(15, vilniusKaunasStations.size());
    }

    private Datastore createDatastore() {
        return Morphia.createDatastore("bps");
    }

    private void createAndSaveVilniusKaunasStations(Route route) {
        Station vilnius = datastore.save(new Station(route, "Vilnius", 1));
        Station paneriai = datastore.save(new Station(route, "Paneriai", 2));
        Station voke = datastore.save(new Station(route, "Vokė", 3));
        Station lentvaris = datastore.save(new Station(route, "Lentvaris", 4));
        Station kariotiskes = datastore.save(new Station(route, "Kariotiškės", 5));
        Station rykantai = datastore.save(new Station(route, "Rykantai", 6));
        Station lazdenai = datastore.save(new Station(route, "Lazdėnai", 7));
        Station vievis = datastore.save(new Station(route, "Vievis", 8));
        Station kaugonys = datastore.save(new Station(route, "Kaugonys", 9));
        Station zasliai = datastore.save(new Station(route, "Žasliai", 10));
        Station kaisiadorys = datastore.save(new Station(route, "Kaišiadorys", 11));
        Station pravieniskes = datastore.save(new Station(route, "Pravieniškės", 12));
        Station karciupis = datastore.save(new Station(route, "Karčiupis", 13));
        Station palemonas = datastore.save(new Station(route, "Palemonas", 14));
        Station kaunas = datastore.save(new Station(route, "Kaunas", 15));

        List<StopTime> vilniusStopTimes = createStopTimeList(vilnius, LocalTime.of(6, 50));
        datastore.save(vilniusStopTimes);
        List<StopTime> paneriaiStopTimes = createStopTimeList(paneriai, LocalTime.of(6, 58));
        datastore.save(paneriaiStopTimes);
        List<StopTime> vokeStopTimes = createStopTimeList(voke, LocalTime.of(6, 54));
        datastore.save(vokeStopTimes);
        List<StopTime> lentvarisStopTimes = createStopTimeList(lentvaris, LocalTime.of(7, 5));
        datastore.save(lentvarisStopTimes);
        List<StopTime> kariotiskesStopTimes = createStopTimeList(kariotiskes, LocalTime.of(7, 9));
        datastore.save(kariotiskesStopTimes);
        List<StopTime> rykantaiStopTimes = createStopTimeList(rykantai, LocalTime.of(7, 14));
        datastore.save(rykantaiStopTimes);
        List<StopTime> lazdenaiStopTimes = createStopTimeList(lazdenai, LocalTime.of(7, 18));
        datastore.save(lazdenaiStopTimes);
        List<StopTime> vievisStopTimes = createStopTimeList(vievis, LocalTime.of(7, 27));
        datastore.save(vievisStopTimes);
        List<StopTime> kaugonysStopTimes = createStopTimeList(kaugonys, LocalTime.of(7, 31));
        datastore.save(kaugonysStopTimes);
        List<StopTime> zasliaiStopTimes = createStopTimeList(zasliai, LocalTime.of(7, 37));
        datastore.save(zasliaiStopTimes);
        List<StopTime> kaisiadorysStopTimes = createStopTimeList(kaisiadorys, LocalTime.of(7, 44));
        datastore.save(kaisiadorysStopTimes);
        List<StopTime> pravieniskesStopTimes = createStopTimeList(pravieniskes, LocalTime.of(7, 56));
        datastore.save(pravieniskesStopTimes);
        List<StopTime> karciupisStopTimes = createStopTimeList(karciupis, LocalTime.of(8, 1));
        datastore.save(karciupisStopTimes);
        List<StopTime> palemonasStopTimes = createStopTimeList(palemonas, LocalTime.of(8, 6));
        datastore.save(palemonasStopTimes);
        List<StopTime> kaunasStopTimes = createStopTimeList(kaunas, LocalTime.of(8, 16));
        datastore.save(kaunasStopTimes);
    }

    private void createAndSaveKaunasVilniusStations(Route route) {
        Station kaunas = datastore.save(new Station(route, "Kaunas", 1));
        Station palemonas = datastore.save(new Station(route, "Palemonas", 2));
        Station karciupis = datastore.save(new Station(route, "Karčiupis", 3));
        Station pravieniskes = datastore.save(new Station(route, "Pravieniškės", 4));
        Station kaisiadorys = datastore.save(new Station(route, "Kaišiadorys", 5));
        Station zasliai = datastore.save(new Station(route, "Žasliai", 6));
        Station kaugonys = datastore.save(new Station(route, "Kaugonys", 7));
        Station vievis = datastore.save(new Station(route, "Vievis", 8));
        Station lazdenai = datastore.save(new Station(route, "Lazdėnai", 9));
        Station rykantai = datastore.save(new Station(route, "Rykantai", 10));
        Station kariotiskes = datastore.save(new Station(route, "Kariotiškės", 11));
        Station lentvaris = datastore.save(new Station(route, "Lentvaris", 12));
        Station voke = datastore.save(new Station(route, "Vokė", 13));
        Station paneriai = datastore.save(new Station(route, "Paneriai", 14));
        Station vilnius = datastore.save(new Station(route, "Vilnius", 15));

        List<StopTime> kaunasStopTimes = createStopTimeList(kaunas, LocalTime.of(7, 0));
        datastore.save(kaunasStopTimes);
        List<StopTime> palemonasStopTimes = createStopTimeList(palemonas, LocalTime.of(7, 10));
        datastore.save(palemonasStopTimes);
        List<StopTime> karciupisStopTimes = createStopTimeList(karciupis, LocalTime.of(7, 15));
        datastore.save(karciupisStopTimes);
        List<StopTime> pravieniskesStopTimes = createStopTimeList(pravieniskes, LocalTime.of(7, 20));
        datastore.save(pravieniskesStopTimes);
        List<StopTime> kaisiadorysStopTimes = createStopTimeList(kaisiadorys, LocalTime.of(7, 32));
        datastore.save(kaisiadorysStopTimes);
        List<StopTime> zasliaiStopTimes = createStopTimeList(zasliai, LocalTime.of(7, 39));
        datastore.save(zasliaiStopTimes);
        List<StopTime> kaugonysStopTimes = createStopTimeList(kaugonys, LocalTime.of(7, 44));
        datastore.save(kaugonysStopTimes);
        List<StopTime> vievisStopTimes = createStopTimeList(vievis, LocalTime.of(7, 50));
        datastore.save(vievisStopTimes);
        List<StopTime> lazdenaiStopTimes = createStopTimeList(lazdenai, LocalTime.of(7, 58));
        datastore.save(lazdenaiStopTimes);
        List<StopTime> rykantaiStopTimes = createStopTimeList(rykantai, LocalTime.of(8, 2));
        datastore.save(rykantaiStopTimes);
        List<StopTime> kariotiskesStopTimes = createStopTimeList(kariotiskes, LocalTime.of(8, 7));
        datastore.save(kariotiskesStopTimes);
        List<StopTime> lentvarisStopTimes = createStopTimeList(lentvaris, LocalTime.of(8, 12));
        datastore.save(lentvarisStopTimes);
        List<StopTime> vokeStopTimes = createStopTimeList(voke, LocalTime.of(8, 16));
        datastore.save(vokeStopTimes);
        List<StopTime> paneriaiStopTimes = createStopTimeList(paneriai, LocalTime.of(8, 20));
        datastore.save(paneriaiStopTimes);
        List<StopTime> vilniusStopTimes = createStopTimeList(vilnius, LocalTime.of(8, 31));
        datastore.save(vilniusStopTimes);
    }

    private List<StopTime> createStopTimeList(Station station, LocalTime firstTime) {
        return List.of(
                new StopTime(station, firstTime),                               // e.g. 06:50
                new StopTime(station, firstTime.plusMinutes(30)),   // e.g. 07:20
                new StopTime(station, firstTime.plusMinutes(105)),  // e.g. 08:15
                new StopTime(station, firstTime.plusMinutes(255)),  // e.g. 10:45
                new StopTime(station, firstTime.plusMinutes(400)),  // e.g. 13:10
                new StopTime(station, firstTime.plusMinutes(540)),  // e.g. 15:30
                new StopTime(station, firstTime.plusMinutes(705)),  // e.g. 18:15
                new StopTime(station, firstTime.plusMinutes(830)),  // e.g. 20:20
                new StopTime(station, firstTime.plusMinutes(930))   // e.g. 22:00
        );
    }
}
