package ru.alphadoub.voting.web;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.alphadoub.voting.model.Dish;
import ru.alphadoub.voting.service.DishService;

import java.time.Month;

import static java.time.LocalDate.of;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alphadoub.voting.DishTestData.*;
import static ru.alphadoub.voting.RestaurantTestData.RESTAURANT1_ID;

public class DishRestControllerTest extends AbstractControllerTest {
    private static final String URL = DishRestController.URL + '/';

    @Autowired
    DishService service;

    @Before
    public void clearSpringCache() throws Exception {
        cacheManager.getCache("dishes").clear();
    }


    @Test
    public void testCreate() throws Exception {
        Dish newDish = new Dish("new dish1", 800, of(2118, Month.DECEMBER, 31));
        ResultActions action = mockMvc.perform(post(URL, RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(newDish)))
                .andExpect(status().isOk())
                .andDo(print());

        Dish returned = jacksonObjectMapper.readValue(getContent(action), Dish.class);
        newDish.setId(returned.getId());

        assertMatch(returned, newDish);
        assertMatch(service.getListByDate(RESTAURANT1_ID, of(2118, Month.DECEMBER, 31)), DISH9, DISH7, newDish, DISH8);
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(URL + DISH1_ID, RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jacksonObjectMapper.writeValueAsString(DISH1)));
    }

    @Test
    public void testUpdate() throws Exception {
        Dish updated = getUpdated(DISH1);
        mockMvc.perform(put(URL + DISH1_ID, RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andDo(print());

        assertMatch(service.get(DISH1_ID, RESTAURANT1_ID), updated);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(URL + DISH1_ID, RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isOk());
        assertMatch(service.getCurrentDayList(RESTAURANT1_ID), DISH3, DISH2);
    }

    @Test
    public void testGetCurrentDayList() throws Exception {
        mockMvc.perform(get(URL, RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jacksonObjectMapper.writeValueAsString(RESTAURANT1_MENU), true));
    }
}