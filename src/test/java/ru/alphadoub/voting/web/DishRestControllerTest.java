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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alphadoub.voting.DishTestData.*;
import static ru.alphadoub.voting.RestaurantTestData.RESTAURANT1_ID;
import static ru.alphadoub.voting.UserTestData.ADMIN;
import static ru.alphadoub.voting.UserTestData.USER1;

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
                .with(httpBasic(ADMIN.getEmail(), ADMIN.getPassword()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(newDish)))
                .andExpect(status().isCreated())
                .andDo(print());

        Dish returned = jacksonObjectMapper.readValue(getContent(action), Dish.class);
        newDish.setId(returned.getId());

        assertMatch(returned, newDish);
        assertMatch(service.getListByDate(RESTAURANT1_ID, of(2118, Month.DECEMBER, 31)), DISH9, DISH7, newDish, DISH8);
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(URL + DISH1_ID, RESTAURANT1_ID)
                .with(httpBasic(USER1.getEmail(), USER1.getPassword())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jacksonObjectMapper.writeValueAsString(DISH1)));
    }

    @Test
    public void testUpdate() throws Exception {
        Dish updated = getUpdated(DISH1);
        mockMvc.perform(put(URL + DISH1_ID, RESTAURANT1_ID)
                .with(httpBasic(ADMIN.getEmail(), ADMIN.getPassword()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andDo(print());

        assertMatch(service.get(DISH1_ID, RESTAURANT1_ID), updated);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(URL + DISH1_ID, RESTAURANT1_ID)
                .with(httpBasic(ADMIN.getEmail(), ADMIN.getPassword())))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(service.getCurrentDayList(RESTAURANT1_ID), DISH3, DISH2);
    }

    @Test
    public void testGetCurrentDayList() throws Exception {
        mockMvc.perform(get(URL, RESTAURANT1_ID)
                .with(httpBasic(USER1.getEmail(), USER1.getPassword())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jacksonObjectMapper.writeValueAsString(RESTAURANT1_MENU), true));
    }

    @Test
    public void test403Forbidden() throws Exception {
        //create
        Dish newDish = new Dish("new dish1", 800, of(2118, Month.DECEMBER, 31));
        mockMvc.perform(post(URL, RESTAURANT1_ID)
                .with(httpBasic(USER1.getEmail(), USER1.getPassword()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(newDish)))
                .andExpect(status().isForbidden());

        //update
        Dish updated = getUpdated(DISH1);
        mockMvc.perform(put(URL + DISH1_ID, RESTAURANT1_ID)
                .with(httpBasic(USER1.getEmail(), USER1.getPassword()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(updated)))
                .andExpect(status().isForbidden());

        //delete
        mockMvc.perform(delete(URL + DISH1_ID, RESTAURANT1_ID)
                .with(httpBasic(USER1.getEmail(), USER1.getPassword())))
                .andExpect(status().isForbidden());
    }

    @Test
    public void test401Unauthorized() throws Exception {
        mockMvc.perform(get(URL + DISH1_ID, RESTAURANT1_ID)).andExpect(status().isUnauthorized());//get
        mockMvc.perform(get(URL, RESTAURANT1_ID)).andExpect(status().isUnauthorized());//get current day list
    }


}