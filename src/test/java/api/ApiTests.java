package api;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.json.JSONObject;

public class ApiTests extends ApiBaseTest {

    @Test
    public void testGetPostById() throws Exception {
        ApiResponse response = apiClient.get("https://jsonplaceholder.typicode.com/posts/1");

        Assert.assertEquals(response.getStatusCode(), 200);

        JSONObject json = new JSONObject(response.getBody());
        Assert.assertEquals(json.getInt("id"), 1);
    }

    @Test
    public void testCreatePost() throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("title", "foo");
        payload.put("body", "bar");
        payload.put("userId", 1);

        ApiResponse response = apiClient.post(
                "https://jsonplaceholder.typicode.com/posts",
                payload.toString()
        );

        Assert.assertEquals(response.getStatusCode(), 201);

        JSONObject json = new JSONObject(response.getBody());
        Assert.assertEquals(json.getString("title"), "foo");
        Assert.assertEquals(json.getString("body"), "bar");
        Assert.assertEquals(json.getInt("userId"), 1);
    }
}