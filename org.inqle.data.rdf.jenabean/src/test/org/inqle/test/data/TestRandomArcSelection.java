package org.inqle.test.data;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.jena.util.ArcLister;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.Persister;
import org.junit.Test;

public class TestRandomArcSelection {

	private static Logger log = Logger.getLogger(TestRandomArcSelection.class);
	
	@Test
	public void testChooseExcludedItems() {
		getPersister();
		Collection<Arc> selectableArcs = new ArrayList<Arc>();
		Collection<Arc> avoidArcs = new ArrayList<Arc>();
		Collection<Arc> selectedArcs;
		
		selectedArcs = (Collection<Arc>) RandomListChooser.chooseRandomItemsAdditively(null, null, 1);
		assertNull(selectedArcs);
		selectedArcs = (Collection<Arc>) RandomListChooser.chooseRandomItemsAdditively(selectableArcs, avoidArcs, 1);
		
		Arc arc1 = new Arc("http://stooge.com/");
		selectableArcs.add(arc1);
		selectedArcs = (Collection<Arc>) RandomListChooser.chooseRandomItemsAdditively(selectableArcs, null, 1);
		assertEquals(1, selectedArcs.size());
		selectedArcs = (Collection<Arc>) RandomListChooser.chooseRandomItemsAdditively(selectableArcs, avoidArcs, 1);
		assertEquals(1, selectedArcs.size());
		selectedArcs = (Collection<Arc>) RandomListChooser.chooseRandomItemsAdditively(selectableArcs, selectableArcs, 1);
		assertEquals(0, selectedArcs.size());
		
		Arc arc2 = new Arc("http://silly.com/");
		selectableArcs.add(arc2);
		selectedArcs = (Collection<Arc>) RandomListChooser.chooseRandomItemsAdditively(selectableArcs, null, 1);
		assertEquals(1, selectedArcs.size());
		assertTrue(selectedArcs.contains(arc1) || selectedArcs.contains(arc2));
		
		selectedArcs = (Collection<Arc>) RandomListChooser.chooseRandomItemsAdditively(selectableArcs, avoidArcs, 1);
		assertEquals(1, selectedArcs.size());
		assertTrue(selectedArcs.contains(arc1) || selectedArcs.contains(arc2));
		
		avoidArcs.add(arc2);
		selectedArcs = (Collection<Arc>) RandomListChooser.chooseRandomItemsAdditively(selectableArcs, avoidArcs, 1);
		assertEquals(1, selectedArcs.size());
		assertTrue(selectedArcs.contains(arc1));
		
		avoidArcs.add(arc1);
		selectedArcs = (Collection<Arc>) RandomListChooser.chooseRandomItemsAdditively(selectableArcs, avoidArcs, 1);
		assertEquals(0, selectedArcs.size());
		
	}

	private void getPersister() {
		Persister persister = Persister.getInstance(AppInfoProvider.getAppInfo());
	}

//	private boolean allSelected(Collection<Arc> selectableArcs,	
//			Collection<Arc> selectedArcs, int numRuns) {
//		List selectableArcList = 
//		for (int i=0; i<numRuns; i++) {
//			
//		}
//		return false;
//	}
}
