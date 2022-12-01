package it.develhope.Unit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.develhope.Unit.Test.controllers.UserController;
import it.develhope.Unit.Test.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class UnitTestApplicationTests {
    @Autowired
    private UserController userController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
        assertThat(userController);
    }

    private User getUserFromId(Long id) throws Exception {
        MvcResult result = this.mockMvc.perform(get("/users/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        try {
            String userJSON = result.getResponse().getContentAsString();
            User user = objectMapper.readValue(userJSON, User.class);
            assertThat(user).isNotNull();
            assertThat(user.getId()).isNotNull();
            return user;
        }catch(Exception e){
            return null;
        }
    }

    private User createAUser() throws Exception {
        User user = new User();
        user.setName("Emi");
        user.setSurname("Fag");
        user.setAge(22);
        user.setActive(true);

        return createAUser(user);
    }

    private User createAUser(User user) throws Exception {
        MvcResult result = createAUserRequest(user);
        User studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        return studentFromResponse;
    }


    private MvcResult createAUserRequest() throws Exception {
        User user = new User();
        user.setName("Emi");
        user.setSurname("Fag");
        user.setAge(22);
        user.setActive(true);

        return createAUserRequest(user);
    }

    private MvcResult createAUserRequest(User user) throws Exception {
        if(user == null)return null;
        String studentJSON = objectMapper.writeValueAsString(user);
        return this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    void createAUserTest() throws Exception{
     User userFromResponse = createAUser();
     assertThat(userFromResponse.getId()).isNotNull();
    }

     @Test
    void readUserList() throws Exception{

        createAUser();
        MvcResult result =  this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

         List<User> userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
         System.out.println("Student in database are: "+ userFromResponse.size());
         assertThat(userFromResponse.size()).isNotZero();
     }

     @Test
    void readSingleUser()throws Exception{
         User user = createAUser();
        User userFromResponse = getUserFromId(user.getId());
        assertThat(userFromResponse.getId()).isEqualTo(user.getId());
     }


    @Test
    void updateUser() throws Exception {
        User user= createAUser();
        assertThat(user.getId()).isNotNull();

        String newName = "Giovanni";
        user.setName(newName);
        String json = objectMapper.writeValueAsString(user);
        //dico di fare il post di user a mockMVC
        MvcResult mvcResult = this.mockMvc.perform((put("/users/"+user.getId())
                        //gli dico che è un json
                        .contentType(MediaType.APPLICATION_JSON)
                        //gli do il contenuto
                        .content(json)))
                //gli dico di stampare tutta la rispons
                .andDo(print())
                //aspettati che sia tutto ok
                .andExpect(status().isOk())
                //fai return
                .andReturn();
        User usersFromResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);

        //qui stiamo controllando che il nostro put sia andato a buon fine
        assertThat(usersFromResponse.getId()).isEqualTo(user.getId());
        assertThat(usersFromResponse.getName()).isEqualTo(newName);

        //prendiamo l 'user con il get
        User userFromResponseGet= getUserFromId(user.getId());
        assertThat(usersFromResponse.getId()).isNotNull();
        assertThat(userFromResponseGet.getId()).isEqualTo(user.getId());
        assertThat(userFromResponseGet.getName()).isEqualTo(newName);
    }

    @Test
    void deleteUser() throws Exception{
        User user= createAUser();
        assertThat(user.getId()).isNotNull();
        //dico di fare il delete di user a mockMVC
        MvcResult mvcResult = this.mockMvc.perform((delete("/users/"+user.getId())))
                //gli dico di stampare tutta la rispons
                .andDo(print())
                //aspettati che sia tutto ok
                .andExpect(status().isOk())
                //fai return
                .andReturn();
        User userFromResponseGet = getUserFromId(user.getId());
        assertThat(userFromResponseGet).isNull();
    }






}
