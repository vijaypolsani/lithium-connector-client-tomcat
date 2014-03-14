package com.lithium.integrations;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommbineDataFromCallsTxTest {
	CombineDataFromCallsTx commbineDataFromCallsTx = null;

	@Before
	public void setUp() throws Exception {
		commbineDataFromCallsTx = new CombineDataFromCallsTx();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		/*
				commbineDataFromCallsTx
						.parseJsonBuildAuthor(
								"",
								"{\"response\":{\"status\":\"success\",\"image\":{\"type\":\"image\",\"href\":\"/users/id/1/profiles/avatar\",\"url\":{\"type\":\"string\",\"$\":\"http://ldn.qa.lithium.com/t5/image/serverpage/avatar-name/environment-clouds/avatar-theme/classic/avatar-collection/environment/avatar-display-size/message\"},\"width\":{\"type\":\"integer\",\"null\":true,\"$\":null},\"height\":{\"type\":\"integer\",\"null\":true,\"$\":null}}}}");
				fail("Not yet implemented");
				*/
	}
}
