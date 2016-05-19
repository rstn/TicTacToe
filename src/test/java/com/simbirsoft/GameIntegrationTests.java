package com.simbirsoft;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.simbirsoft.converter.Converter;
import com.simbirsoft.model.Point;
import com.simbirsoft.request.PlayerMove;
import com.simbirsoft.request.PlayerName;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests of application.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GameRunner.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GameIntegrationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private void performPost(String path) throws Exception {
        mvc.perform(post(path)).andExpect(status().isOk());
    }

    private void performPost(String path, Object object, String returnResult) throws Exception {
        mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(Converter.asJsonString(object)))
                .andExpect(status().isOk()).andExpect(content().string(containsString(returnResult)));
    }

    private void performGet(String path, String returnResult) throws Exception {
        mvc.perform(get(path)).andExpect(status().isOk()).andExpect(content().string(containsString(returnResult)));
    }

    @Test
    public void testGameFlow() throws Exception {
        // players registration
        performPost("/player/register", new PlayerName("Molly"), "SUCCESS");
        performPost("/player/register", new PlayerName("Case"), "SUCCESS");

        // check board
        performGet("/gameBoard", "board");

        // do move
        performPost("/move", new PlayerMove(1, new Point(0, 0)), "OK");

        // check cross on board
        performGet("/gameBoard", "CROSS");

        // check game result
        performGet("/gameResult", "PLAYING");

        // bring to a crosses victory
        performPost("/move", new PlayerMove(2, new Point(0, 1)), "OK");
        performPost("/move", new PlayerMove(1, new Point(1, 1)), "OK");
        performPost("/move", new PlayerMove(2, new Point(2, 2)), "OK");
        performPost("/move", new PlayerMove(1, new Point(1, 0)), "OK");
        performPost("/move", new PlayerMove(2, new Point(2, 0)), "OK");
        performPost("/move", new PlayerMove(1, new Point(1, 2)), "OK");

        // check game result
        performGet("/gameResult", "CROSS_WON");
    }

    @Test
    public void testGameIncorrectActions() throws Exception {
        performPost("/move", new PlayerMove(1, new Point(0, 0)), "GAME_OVER");

        performPost("/reset");

        performPost("/move", new PlayerMove(3, new Point(0, 0)), "PLAYER_ONE_NOT_REGISTERED");

        performPost("/player/register", new PlayerName("Molly"), "SUCCESS");

        performPost("/move", new PlayerMove(4, new Point(0, 0)), "PLAYER_TWO_NOT_REGISTERED");

        performPost("/player/register", new PlayerName("Case"), "SUCCESS");
        performPost("/player/register", new PlayerName("Sally"), "ALREADY_REGISTERED");

        performPost("/move", new PlayerMove(5, new Point(0, 0)), "PLAYER_NOT_DEFINED");
        performPost("/move", new PlayerMove(4, new Point(0, 0)), "NOT_THIS_PLAYER_TURN");
        performPost("/move", new PlayerMove(3, new Point(3, 0)), "INVALID_BOARD_MOVE");
    }
}
