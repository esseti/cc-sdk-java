package org.crowdcomputer.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.crowdcomputer.CroCoClient;
import org.crowdcomputer.utils.RestCaller;
import org.crowdcomputer.utils.staticvalues.Operations;
import org.crowdcomputer.utils.staticvalues.Platforms;
import org.hamcrest.Matchers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Test;

public class MyBaseTest {

	private static String app_id = "8fb5f8646b994bb4937bfe55ec934f1d";
	private static String user_id = "a441168a5d6afc6b0432342f55d024ac78b6fbed";

	@Test
	public void testGetCorrect() {
		// http://httpbin.org/ip
		RestCaller rc = new RestCaller(app_id, user_id);
		JSONObject obj = (JSONObject) rc.getCall(UriBuilder.fromUri(
				"http://httpbin.org/get").build());
		JSONObject headers = (JSONObject) obj.get("headers");
		assertEquals("this test for app", headers.get("App-Id"), app_id);
		assertEquals("this test for user", headers.get("Authorization"),
				"Token " + user_id);
		assertEquals("this is the test for Host", obj.get("url"),
				"http://httpbin.org/get");

	}

	@Test
	public void testGetWrong() {
		// http://httpbin.org/ip

		RestCaller rc = new RestCaller(app_id, user_id);
		JSONObject obj = (JSONObject) rc.getCall(UriBuilder.fromUri(
				"http://httpbin.org/get").build());
		assertThat("this test for app", "" + obj.get("error"),
				Matchers.containsString("error"));
	}

	@Test
	public void testPostCorrect() {

		RestCaller rc = new RestCaller(app_id, user_id);
		HashMap<Object, Object> mvhm = new HashMap<Object, Object>();
		mvhm.put("test", "test");
		JSONObject obj = (JSONObject) rc.postCall(
				UriBuilder.fromUri("http://httpbin.org/post/").build(), mvhm);
		JSONObject data = (JSONObject) JSONValue.parse("" + obj.get("data"));
		assertEquals("this is the test for POST", data.get("test"), "test");

	}

	@Test
	public void testPutCorrect() {

		RestCaller rc = new RestCaller(app_id, user_id);
		HashMap<Object, Object> mvhm = new HashMap<Object, Object>();
		mvhm.put("test", "test");
		JSONObject obj = (JSONObject) rc.putCall(
				UriBuilder.fromUri("http://httpbin.org/put/").build(), mvhm);
		JSONObject data = (JSONObject) JSONValue.parse("" + obj.get("data"));
		assertEquals("this is the test for PUT", data.get("test"), "test");

	}

	@Test
	public void testReward() {

		CroCoClient cclient = new CroCoClient(app_id, user_id);
		JSONObject obj = cclient.createReward("CCM", 1.0);
		assertEquals(1.0, Double.parseDouble("" + obj.get("quantity")), 0.0);
	}

	@Test
	public void testTask() {

		CroCoClient cclient = new CroCoClient(app_id, user_id);
		JSONObject obj_process = cclient.createProcess("process_api",
				"description");
		Long pk = (Long) obj_process.get("id");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 7);
		HashMap pars = new HashMap();
		pars.put("test", "test");
		JSONObject obj_task = cclient.createCCTask(pk, "title", "description",
				cal.getTime(), 1, "http://example.com", 1.0,
				Platforms.CROWDCOMPUTER,pars);
		assertEquals("title is the same?", "title", obj_task.get("title"));
	}

	@Test
	public void testAMTTask() {

		CroCoClient cclient = new CroCoClient(app_id, user_id);
		JSONObject obj_process = cclient.createProcess("process_api",
				"description");
		Long pk = (Long) obj_process.get("id");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 7);
		JSONObject obj_task = cclient.createAMTTask(pk, "DEV: CROCO2",
				"description", cal.getTime(), 1,
				"http://disi.unitn.it/~tranquillini/mturk/index.html", 1.0,null);
		// JSONObject start_task =
		// cclient.startTask(Long.getLong(""+obj_task.get("id")),
		// "[]","fakename");
		// System.out.println(start_task);
		// String result = ""+start_task.get("result");
		// System.out.println(result);
		assertEquals("title is the same?", "DEV: CROCO", obj_task.get("title"));

	}

	@Test
	public void testTaskWithoutDeadline() {

		CroCoClient cclient = new CroCoClient(app_id, user_id);
		JSONObject obj_process = cclient.createProcess("process_api",
				"description");
		Long pk = (Long) obj_process.get("id");
		JSONObject obj_task = cclient.createCCTask(pk, "title", "description",
				null, 1, "http://example.com", 1.0, Platforms.CROWDCOMPUTER, null);
		assertEquals("title is the same?", "title", obj_task.get("title"));
	}

	@Test
	public void testStartTask() {

		CroCoClient cclient = new CroCoClient(app_id, user_id);
		JSONObject start_task = cclient.startTask(40L, "[]", "fakename");
		System.out.println(start_task);
		String result = "" + start_task.get("result");
		// JSONObject obj_task = cclient.createCCTask(pk,"title", "description",
		// null , 1, "http://example.com", 1.0, Platforms.CROWDCOMPUTER);
		assertEquals("title is the same?", "ok", result);
	}

	@Test
	public void testSplit() {
		CroCoClient cclient = new CroCoClient(app_id, user_id);
		JSONArray data = new JSONArray();
		for (int i = 0; i < 10; i++) {
			JSONObject jobj = new JSONObject();
			jobj.put("id", i);
			data.add(jobj);
		}
		JSONObject rest = cclient.splitData(data.toJSONString(),
				Operations.SPLIT_N, "2", "0");
		String result = "" + rest.get("result");
		System.out.println(result);
		// JSONObject obj_task = cclient.createCCTask(pk,"title", "description",
		// null , 1, "http://example.com", 1.0, Platforms.CROWDCOMPUTER);
		assertEquals("title is the same?", "ok", "ok");
	}

	@Test
	public void testSplitObject() {
		CroCoClient cclient = new CroCoClient(app_id, user_id);
		JSONArray data = new JSONArray();
		for (int i = 0; i < 10; i++) {
			JSONObject jobj = new JSONObject();
			jobj.put("id", i);
			jobj.put("a", i);
			jobj.put("b", i);
			data.add(jobj);
		}
		List<String> shared = new ArrayList<String>();
		shared.add("id");
		List<String> fields = new ArrayList<String>();
		fields.add("a");

		JSONObject rest = cclient.splitObject(data.toJSONString(),shared,fields);
		String result = "" + rest.get("result");
		// JSONObject obj_task = cclient.createCCTask(pk,"title", "description",
		// null , 1, "http://example.com", 1.0, Platforms.CROWDCOMPUTER);
		assertEquals("no control here", "ok", "ok");
	}
}
