package com.qeevee.gq.tests;

import static com.qeevee.gq.tests.TestUtils.getFieldValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.qeevee.gq.history.History;
import com.qeevee.gq.history.TextItem;
import com.qeevee.gq.history.TransitionItem;
import com.qeevee.ui.ZoomImageView;
import com.xtremelabs.robolectric.RobolectricTestRunner;

import edu.bonn.mobilegaming.geoquest.Variables;
import edu.bonn.mobilegaming.geoquest.mission.DialogItem;
import edu.bonn.mobilegaming.geoquest.mission.NPCTalk;

@RunWith(RobolectricTestRunner.class)
public class NPCTalkMissionTests {
    NPCTalk npcTalkM;
    ZoomImageView imageView;
    TextView talkView;
    Button proceedBT;
    CountDownTimer timer;

    public void initTestMission(String missionID) {
	npcTalkM = (NPCTalk) TestUtils.setUpMissionTest("NPCTalk",
							missionID);
	npcTalkM.onCreate(null);
	imageView = (ZoomImageView) getFieldValue(npcTalkM,
						  "charImage");
	talkView = (TextView) getFieldValue(npcTalkM,
					    "dialogText");
	proceedBT = (Button) getFieldValue(npcTalkM,
					   "proceedButton");
	timer = (CountDownTimer) getFieldValue(npcTalkM,
					       "myCountDownTimer");
    }

    @After
    public void cleanUp() {
	// get rid of all variables that have been set, e.g. for checking
	// actions.
	Variables.clean();
	History.getInstance().clear();
    }

    @Test
    public void startEventTriggered() {
	assertFalse("onStart Variable should not be initialized at beginning of test",
		    Variables.isDefined("onStart"));
	initTestMission("With_Defaults");
	assertEquals("onStart should have set variable to 1",
		     1.0,
		     Variables.getValue("onStart"));
    }

    @Test
    public void goThrough() {
	History h = History.getInstance();
	assertEquals("History should be empty before mission is loaded",
		     0,
		     h.numberOfItems());
	initTestMission("Only_Text");
	assertEquals("History should be empty directly after mission is loaded",
		     0,
		     h.numberOfItems());
	letCurrentDialogItemAppearCompletely();
	assertEquals("Talk view should contain last dialog item text",
		     "This NPCTalk mission offers just three only text dialog items.\n",
		     talkView.getText().toString());
	assertEquals("History should contain the first item after it has been shown.",
		     1,
		     h.numberOfItems());
	assertEquals("First element in history should be a text item.",
		     h.getItem(h.numberOfItems() - 1).getClass(),
		     TextItem.class);
	proceedBT.performClick();
	letCurrentDialogItemAppearCompletely();
	assertTrue("Talk view should end with second dialog item text",
		   talkView.getText().toString()
			   .endsWith("This is the second dialog item.\n"));
	assertEquals("History should contain both items now.",
		     2,
		     h.numberOfItems());
	assertEquals("First element in history should be a text item.",
		     h.getItem(h.numberOfItems() - 1).getClass(),
		     TextItem.class);
	proceedBT.performClick();
	letCurrentDialogItemAppearCompletely();
	assertTrue("Talk view should end with third and last dialog item text",
		   talkView.getText()
			   .toString()
			   .endsWith("This is the third and last dialog item.\n"));
	assertEquals("History should contain three items now.",
		     3,
		     h.numberOfItems());
	assertEquals("First element in history should be a text item.",
		     h.getItem(h.numberOfItems() - 1).getClass(),
		     TextItem.class);
	proceedBT.performClick();
	assertEquals("History should contain four items now.",
		     4,
		     h.numberOfItems());
	assertEquals("Last element in history should be a transitional item.",
		     h.getItem(h.numberOfItems() - 1).getClass(),
		     TransitionItem.class);
    }

    /**
     * This helper method emulates the Timer used in the real application. It
     * very specialized according to the implementation, e.g. the delta between
     * word appearance is set to 100ms as in the code.
     */
    public void letCurrentDialogItemAppearCompletely() {
	timer = (CountDownTimer) getFieldValue(npcTalkM,
					       "myCountDownTimer");
	timer.onFinish();
	DialogItem dialogItem = (DialogItem) getFieldValue(npcTalkM,
							   "currItem");
	timer = (CountDownTimer) getFieldValue(npcTalkM,
					       "myCountDownTimer");
	for (int i = 0; i < dialogItem.getNumParts(); i++) {
	    timer.onTick(100l * (dialogItem.getNumParts() - (i + 1)));
	}
	timer.onFinish();
    }

}