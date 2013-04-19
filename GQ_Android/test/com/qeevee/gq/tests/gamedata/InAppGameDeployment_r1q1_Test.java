package com.qeevee.gq.tests.gamedata;

import static com.qeevee.gq.tests.gamedata.TestGameDataUtil.repoShouldHaveQuests;
import static com.qeevee.gq.tests.gamedata.TestGameDataUtil.shouldHaveRepositories;
import static com.qeevee.gq.tests.util.TestUtils.startApp;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.qeevee.gq.tests.robolectric.GQTestRunner;
import com.qeevee.gq.tests.robolectric.WithAssets;

/**
 * Tests the case when the GeoQuest app does not come with any preloaded quests.
 * In particular when the assets directory is completely empty.
 * 
 * @author muegge
 * 
 */
@RunWith(GQTestRunner.class)
@WithAssets("../GQ_Android/test/testassets/r1q1/")
public class InAppGameDeployment_r1q1_Test {

	// === TESTS FOLLOW =============================================

	@Test
	public void findOneRepo() {
		// GIVEN:
		// nothing
 
		// WHEN:
		startApp();

		// THEN:
		shouldHaveRepositories(1, new String[] { "repo1" });
		repoShouldHaveQuests("repo1", 1, new String[] { "r1q1-test-game" });
	}

	// === HELPERS FOLLOW =============================================

}
