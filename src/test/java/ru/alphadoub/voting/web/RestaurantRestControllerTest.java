package ru.alphadoub.voting.web;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.service.RestaurantService;

import static java.time.LocalTime.now;
import static java.time.LocalTime.of;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alphadoub.voting.RestaurantTestData.*;
import static ru.alphadoub.voting.UserTestData.ADMIN;
import static ru.alphadoub.voting.UserTestData.USER1;

public class RestaurantRestControllerTest extends AbstractControllerTest {
    private static final String URL = RestaurantRestController.URL + '/';

    @Before
    public void clearSpringCache() throws Exception {
        cacheManager.getCache("restaurants").clear();
    }


    @Autowired
    RestaurantService service;

    @Test
    public void testCreate() throws Exception {
        Restaurant newRestaurant = getCreated();
        ResultActions action = mockMvc.perform(post(URL)
                .with(httpBasic(ADMIN.getEmail(), ADMIN.getPassword()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(newRestaurant)))
                .andExpect(status().isCreated())
                .andDo(print());

        Restaurant returned = jacksonObjectMapper.readValue(getContent(action), Restaurant.class);
        newRestaurant.setId(returned.getId());

        assertMatch(returned, newRestaurant);
        assertMatch(service.getAll(), newRestaurant, RESTAURANT1, RESTAURANT2, RESTAURANT3);
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(URL + RESTAURANT1_ID)
                .with(httpBasic(USER1.getEmail(), USER1.getPassword())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jacksonObjectMapper.writeValueAsString(RESTAURANT1)));
    }

    @Test
    public void testUpdate() throws Exception {
        Restaurant updated = getUpdated(RESTAURANT1);
        mockMvc.perform(put(URL + RESTAURANT1_ID)
                .with(httpBasic(ADMIN.getEmail(), ADMIN.getPassword()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andDo(print());

        assertMatch(service.get(RESTAURANT1_ID), updated);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(URL + RESTAURANT2_ID)
                .with(httpBasic(ADMIN.getEmail(), ADMIN.getPassword())))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(service.getAll(), RESTAURANT1, RESTAURANT3);
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(URL)
                .with(httpBasic(USER1.getEmail(), USER1.getPassword())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jacksonObjectMapper.writeValueAsString(RESTAURANTS), true));
    }

    @Test
    public void testVote() throws Exception {
        /*
               * Пока не добавлена обработка исключений в случае, если текущее время > 11:00, пропустим тест
               * После добавления обработки исключений сделаем вариативность ожидания статуса в зависимости от текущего времени
               */
        Assume.assumeTrue(now().compareTo(of(11, 0)) < 0);
        mockMvc.perform(post(URL + RESTAURANT1_ID + "/vote")
                .with(httpBasic(USER1.getEmail(), USER1.getPassword())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testGetWithCurrentDayVotes() throws Exception {
        mockMvc.perform(get(URL + RESTAURANT1_ID + "/with_votes")
                .with(httpBasic(USER1.getEmail(), USER1.getPassword())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jacksonObjectMapper.writeValueAsString(RESTAURANT1_WITH_VOTES)));
    }

    @Test
    public void testGetAllWithCurrentDayVotes() throws Exception {
        mockMvc.perform(get(URL + "with_votes")
                .with(httpBasic(USER1.getEmail(), USER1.getPassword())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jacksonObjectMapper.writeValueAsString(RESTAURANTS_WITH_VOTES), true));
    }

    @Test
    public void test401Unauthorized() throws Exception {
        mockMvc.perform(post(URL + RESTAURANT1_ID + "/vote")).andExpect(status().isUnauthorized());//vote
        mockMvc.perform(get(URL + RESTAURANT1_ID)).andExpect(status().isUnauthorized());//get
        mockMvc.perform(get(URL + RESTAURANT1_ID + "/with_votes")).andExpect(status().isUnauthorized());//get with votes
        mockMvc.perform(get(URL)).andExpect(status().isUnauthorized());//get all
        mockMvc.perform(get(URL + "with_votes")).andExpect(status().isUnauthorized());//get all with votes
    }

    @Test
    public void test403Forbidden() throws Exception {
        //create
        Restaurant newRestaurant = getCreated();
        mockMvc.perform(post(URL)
                .with(httpBasic(USER1.getEmail(), USER1.getPassword()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(newRestaurant)))
                .andExpect(status().isForbidden());

        //update
        Restaurant updated = getUpdated(RESTAURANT1);
        mockMvc.perform(put(URL + RESTAURANT1_ID)
                .with(httpBasic(USER1.getEmail(), USER1.getPassword()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(updated)))
                .andExpect(status().isForbidden());

        //delete
        mockMvc.perform(delete(URL + RESTAURANT2_ID)
                .with(httpBasic(USER1.getEmail(), USER1.getPassword())))
                .andExpect(status().isForbidden());
    }
}